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
 * Unit 2.1: Doctor (Physiotherapist) Dashboard Servlet.
 * Fetches today's appointments and patient queues for the logged-in therapist.
 */
@WebServlet("/doctor/dashboard")
public class DoctorDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Long userId = (Long) session.getAttribute("userId");

        List<Map<String, Object>> sessionsList = new ArrayList<>();

        String sql = """
            SELECT s.id, s.session_date, s.start_time, s.end_time, s.status,
                   u.full_name AS patient_name, tp.diagnosis
            FROM sessions s
            JOIN therapists t ON s.therapist_id = t.id
            JOIN patients p ON s.patient_id = p.id
            JOIN users u ON p.user_id = u.id
            JOIN treatment_plans tp ON s.plan_id = tp.id
            WHERE t.user_id = ?
            ORDER BY s.session_date DESC, s.start_time ASC
        """;

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs.getLong("id"));
                    map.put("sessionDate", rs.getDate("session_date"));
                    map.put("startTime", rs.getTime("start_time"));
                    map.put("endTime", rs.getTime("end_time"));
                    map.put("status", rs.getString("status"));
                    map.put("patientName", rs.getString("patient_name"));
                    map.put("diagnosis", rs.getString("diagnosis"));
                    sessionsList.add(map);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.setAttribute("sessionsList", sessionsList);
        req.getRequestDispatcher("/WEB-INF/views/doctor-dashboard.jsp").forward(req, resp);
    }
}
