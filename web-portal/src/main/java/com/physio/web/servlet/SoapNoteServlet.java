package com.physio.web.servlet;

import com.physio.web.config.DatabaseConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit 2.1 - 2.4: Doctor Clinical SOAP Note Recording Servlet.
 * Connects the web form to the stored procedure sp_complete_session_and_deduct.
 */
@WebServlet("/doctor/soap-note")
public class SoapNoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionIdStr = req.getParameter("sessionId");
        if (sessionIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/doctor/dashboard");
            return;
        }

        try {
            Long sessionId = Long.parseLong(sessionIdStr);
            Map<String, Object> sessionData = new HashMap<>();

            String sql = """
                SELECT s.id, s.session_date, s.start_time, s.end_time, s.status,
                       u.full_name AS patient_name, tp.diagnosis, tp.treatment_goal
                FROM sessions s
                JOIN patients p ON s.patient_id = p.id
                JOIN users u ON p.user_id = u.id
                JOIN treatment_plans tp ON s.plan_id = tp.id
                WHERE s.id = ?
            """;

            try (Connection conn = DatabaseConnectionPool.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setLong(1, sessionId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        sessionData.put("id", rs.getLong("id"));
                        sessionData.put("sessionDate", rs.getDate("session_date"));
                        sessionData.put("startTime", rs.getTime("start_time"));
                        sessionData.put("endTime", rs.getTime("end_time"));
                        sessionData.put("status", rs.getString("status"));
                        sessionData.put("patientName", rs.getString("patient_name"));
                        sessionData.put("diagnosis", rs.getString("diagnosis"));
                        sessionData.put("treatmentGoal", rs.getString("treatment_goal"));
                    } else {
                        resp.sendRedirect(req.getContextPath() + "/doctor/dashboard?error=Session+not+found.");
                        return;
                    }
                }
            }

            req.setAttribute("sessionData", sessionData);
            req.getRequestDispatcher("/WEB-INF/views/doctor-soap-note.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/doctor/dashboard?error=" + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long sessionId = Long.parseLong(req.getParameter("sessionId"));
            int painBefore = Integer.parseInt(req.getParameter("painBefore"));
            int painAfter = Integer.parseInt(req.getParameter("painAfter"));
            String romNotes = req.getParameter("romNotes");
            String muscleGrade = req.getParameter("muscleGrade");
            String subjective = req.getParameter("subjective");
            String objective = req.getParameter("objective");
            String assessment = req.getParameter("assessment");
            String planNext = req.getParameter("planNext");

            String[] modalitiesArr = req.getParameterValues("modalities");
            String modalities = (modalitiesArr != null) ? String.join(", ", modalitiesArr) : "None specified";

            // Execute Stored Procedure sp_complete_session_and_deduct via CallableStatement
            String callSql = "CALL sp_complete_session_and_deduct(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnectionPool.getConnection();
                 CallableStatement cs = conn.prepareCall(callSql)) {

                cs.setLong(1, sessionId);
                cs.setInt(2, painBefore);
                cs.setInt(3, painAfter);
                cs.setString(4, romNotes != null ? romNotes : "Within normal limits");
                cs.setString(5, muscleGrade != null ? muscleGrade : "4/5 MMT");
                cs.setString(6, subjective != null ? subjective : "No specific notes");
                cs.setString(7, objective != null ? objective : "Clinical examination conducted");
                cs.setString(8, assessment != null ? assessment : "Favorable recovery trajectory");
                cs.setString(9, planNext != null ? planNext : "Continue scheduled protocol");
                cs.setString(10, modalities);

                cs.registerOutParameter(11, Types.VARCHAR);
                cs.execute();

                String statusMsg = cs.getString(11);
                System.out.println("Stored Procedure Result: " + statusMsg);
            }

            // Post-Redirect-Get
            resp.sendRedirect(req.getContextPath() + "/doctor/dashboard?success=SOAP+note+recorded+and+session+completed+successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/doctor/dashboard?error=Error+saving+SOAP+note:+" + e.getMessage());
        }
    }
}
