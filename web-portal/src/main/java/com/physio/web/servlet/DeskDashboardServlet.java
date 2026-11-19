package com.physio.web.servlet;

import com.physio.web.config.DatabaseConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit 2.1: Receptionist / Front-Desk Dashboard Servlet.
 * Lists clinic appointment schedules and one-click attendance queues.
 */
@WebServlet("/desk/dashboard")
public class DeskDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Map<String, Object>> todaySessions = new ArrayList<>();

        String sql = """
            SELECT s.id, s.session_date, s.start_time, s.end_time, s.status,
                   up.full_name AS patient_name, ut.full_name AS therapist_name
            FROM sessions s
            JOIN patients p ON s.patient_id = p.id
            JOIN users up ON p.user_id = up.id
            JOIN therapists t ON s.therapist_id = t.id
            JOIN users ut ON t.user_id = ut.id
            ORDER BY s.session_date DESC, s.start_time ASC
            LIMIT 15
        """;

        try (Connection conn = DatabaseConnectionPool.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getLong("id"));
                map.put("sessionDate", rs.getDate("session_date"));
                map.put("startTime", rs.getTime("start_time"));
                map.put("endTime", rs.getTime("end_time"));
                map.put("status", rs.getString("status"));
                map.put("patientName", rs.getString("patient_name"));
                map.put("therapistName", rs.getString("therapist_name"));
                todaySessions.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.setAttribute("todaySessions", todaySessions);
        req.getRequestDispatcher("/WEB-INF/views/desk-dashboard.jsp").forward(req, resp);
    }
}
