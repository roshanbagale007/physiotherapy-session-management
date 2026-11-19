package com.physio.web.servlet;

import com.physio.web.config.DatabaseConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit 2.1: Patient Portal Dashboard Servlet.
 * Displays appointments, rehabilitation plans, and prescribed exercises.
 */
@WebServlet("/patient/dashboard")
public class PatientDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Long userId = (Long) session.getAttribute("userId");

        List<Map<String, Object>> mySessions = new ArrayList<>();
        List<Map<String, Object>> myExercises = new ArrayList<>();
        String currentDiagnosis = "General Consultation";
        int completedSessions = 0;
        int targetSessions = 0;

        String sessionsSql = """
            SELECT s.id, s.session_date, s.start_time, s.end_time, s.status,
                   ut.full_name AS therapist_name
            FROM sessions s
            JOIN patients p ON s.patient_id = p.id
            JOIN therapists t ON s.therapist_id = t.id
            JOIN users ut ON t.user_id = ut.id
            WHERE p.user_id = ?
            ORDER BY s.session_date ASC, s.start_time ASC
        """;

        String planSql = """
            SELECT tp.diagnosis, tp.completed_sessions, tp.target_sessions,
                   e.name AS exercise_name, e.target_muscle_group,
                   pe.prescribed_sets, pe.prescribed_reps, pe.special_notes
            FROM treatment_plans tp
            JOIN patients p ON tp.patient_id = p.id
            LEFT JOIN plan_exercises pe ON tp.id = pe.plan_id
            LEFT JOIN exercises e ON pe.exercise_id = e.id
            WHERE p.user_id = ? AND tp.status = 'ACTIVE'
        """;

        try (Connection conn = DatabaseConnectionPool.getConnection()) {

            try (PreparedStatement ps1 = conn.prepareStatement(sessionsSql)) {
                ps1.setLong(1, userId);
                try (ResultSet rs1 = ps1.executeQuery()) {
                    while (rs1.next()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", rs1.getLong("id"));
                        map.put("sessionDate", rs1.getDate("session_date"));
                        map.put("startTime", rs1.getTime("start_time"));
                        map.put("endTime", rs1.getTime("end_time"));
                        map.put("status", rs1.getString("status"));
                        map.put("therapistName", rs1.getString("therapist_name"));
                        mySessions.add(map);
                    }
                }
            }

            try (PreparedStatement ps2 = conn.prepareStatement(planSql)) {
                ps2.setLong(1, userId);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    while (rs2.next()) {
                        currentDiagnosis = rs2.getString("diagnosis");
                        completedSessions = rs2.getInt("completed_sessions");
                        targetSessions = rs2.getInt("target_sessions");

                        if (rs2.getString("exercise_name") != null) {
                            Map<String, Object> ex = new HashMap<>();
                            ex.put("name", rs2.getString("exercise_name"));
                            ex.put("muscle", rs2.getString("target_muscle_group"));
                            ex.put("sets", rs2.getInt("prescribed_sets"));
                            ex.put("reps", rs2.getInt("prescribed_reps"));
                            ex.put("notes", rs2.getString("special_notes"));
                            myExercises.add(ex);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.setAttribute("mySessions", mySessions);
        req.setAttribute("myExercises", myExercises);
        req.setAttribute("diagnosis", currentDiagnosis);
        req.setAttribute("completedSessions", completedSessions);
        req.setAttribute("targetSessions", targetSessions);

        req.getRequestDispatcher("/WEB-INF/views/patient-dashboard.jsp").forward(req, resp);
    }
}
