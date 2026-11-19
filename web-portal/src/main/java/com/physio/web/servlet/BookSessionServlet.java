package com.physio.web.servlet;

import com.physio.web.config.DatabaseConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit 2.1 - 2.4: Receptionist Appointment Booking Servlet.
 * Implements slot conflict detection and Post-Redirect-Get (PRG) pattern.
 */
@WebServlet("/desk/book-session")
public class BookSessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        loadFormData(req);
        req.getRequestDispatcher("/WEB-INF/views/desk-book-session.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String planIdStr = req.getParameter("planId");
        String therapistIdStr = req.getParameter("therapistId");
        String patientIdStr = req.getParameter("patientId");
        String sessionDateStr = req.getParameter("sessionDate");
        String startTimeStr = req.getParameter("startTime");
        String packageIdStr = req.getParameter("packageId");

        if (planIdStr == null || therapistIdStr == null || sessionDateStr == null || startTimeStr == null) {
            req.setAttribute("errorMessage", "All required fields must be selected.");
            loadFormData(req);
            req.getRequestDispatcher("/WEB-INF/views/desk-book-session.jsp").forward(req, resp);
            return;
        }

        try {
            Long planId = Long.parseLong(planIdStr);
            Long therapistId = Long.parseLong(therapistIdStr);
            Long patientId = Long.parseLong(patientIdStr);
            Date sessionDate = Date.valueOf(sessionDateStr);
            Time startTime = Time.valueOf(startTimeStr + (startTimeStr.length() == 5 ? ":00" : ""));

            // Calculate 45-minute slot end time
            LocalTime startLocal = startTime.toLocalTime();
            LocalTime endLocal = startLocal.plusMinutes(45);
            Time endTime = Time.valueOf(endLocal);

            Long packageId = (packageIdStr != null && !packageIdStr.isEmpty() && !packageIdStr.equals("0"))
                    ? Long.parseLong(packageIdStr) : null;

            try (Connection conn = DatabaseConnectionPool.getConnection()) {
                // 1. Conflict Detection: Prevent double-booking therapist
                String checkSql = """
                    SELECT COUNT(*) FROM sessions
                    WHERE therapist_id = ? AND session_date = ? AND start_time = ?
                    AND status NOT IN ('CANCELLED', 'RESCHEDULED')
                """;

                try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                    psCheck.setLong(1, therapistId);
                    psCheck.setDate(2, sessionDate);
                    psCheck.setTime(3, startTime);

                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            req.setAttribute("errorMessage", "Slot Conflict: The selected therapist already has an appointment at " + startTimeStr + " on " + sessionDateStr + ".");
                            loadFormData(req);
                            req.getRequestDispatcher("/WEB-INF/views/desk-book-session.jsp").forward(req, resp);
                            return;
                        }
                    }
                }

                // 2. Insert Session Record
                String insertSql = """
                    INSERT INTO sessions (plan_id, therapist_id, patient_id, session_date, start_time, end_time, status, package_invoice_id)
                    VALUES (?, ?, ?, ?, ?, ?, 'SCHEDULED', ?)
                """;

                try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                    psInsert.setLong(1, planId);
                    psInsert.setLong(2, therapistId);
                    psInsert.setLong(3, patientId);
                    psInsert.setDate(4, sessionDate);
                    psInsert.setTime(5, startTime);
                    psInsert.setTime(6, endTime);
                    if (packageId != null) {
                        psInsert.setLong(7, packageId);
                    } else {
                        psInsert.setNull(7, Types.BIGINT);
                    }
                    psInsert.executeUpdate();
                }
            }

            // 3. Post-Redirect-Get (PRG) Pattern: Redirect prevents duplicate booking on refresh
            resp.sendRedirect(req.getContextPath() + "/desk/dashboard?success=Session+scheduled+successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Error booking session: " + e.getMessage());
            loadFormData(req);
            req.getRequestDispatcher("/WEB-INF/views/desk-book-session.jsp").forward(req, resp);
        }
    }

    private void loadFormData(HttpServletRequest req) {
        List<Map<String, Object>> plans = new ArrayList<>();
        List<Map<String, Object>> therapists = new ArrayList<>();
        List<Map<String, Object>> packages = new ArrayList<>();

        String plansSql = """
            SELECT tp.id, tp.diagnosis, tp.patient_id, u.full_name AS patient_name, tp.therapist_id
            FROM treatment_plans tp
            JOIN patients p ON tp.patient_id = p.id
            JOIN users u ON p.user_id = u.id
            WHERE tp.status = 'ACTIVE'
            ORDER BY u.full_name ASC
        """;

        String therapistsSql = """
            SELECT t.id, u.full_name, t.specialization, t.consultation_fee
            FROM therapists t
            JOIN users u ON t.user_id = u.id
            ORDER BY u.full_name ASC
        """;

        String packagesSql = """
            SELECT id, patient_id, package_name, remaining_sessions
            FROM packages_invoices
            WHERE remaining_sessions > 0
            ORDER BY package_name ASC
        """;

        try (Connection conn = DatabaseConnectionPool.getConnection()) {
            try (Statement s1 = conn.createStatement(); ResultSet rs1 = s1.executeQuery(plansSql)) {
                while (rs1.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs1.getLong("id"));
                    map.put("patientId", rs1.getLong("patient_id"));
                    map.put("patientName", rs1.getString("patient_name"));
                    map.put("diagnosis", rs1.getString("diagnosis"));
                    map.put("therapistId", rs1.getLong("therapist_id"));
                    plans.add(map);
                }
            }
            try (Statement s2 = conn.createStatement(); ResultSet rs2 = s2.executeQuery(therapistsSql)) {
                while (rs2.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs2.getLong("id"));
                    map.put("name", rs2.getString("full_name"));
                    map.put("specialization", rs2.getString("specialization"));
                    map.put("fee", rs2.getBigDecimal("consultation_fee"));
                    therapists.add(map);
                }
            }
            try (Statement s3 = conn.createStatement(); ResultSet rs3 = s3.executeQuery(packagesSql)) {
                while (rs3.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs3.getLong("id"));
                    map.put("patientId", rs3.getLong("patient_id"));
                    map.put("name", rs3.getString("package_name"));
                    map.put("remaining", rs3.getInt("remaining_sessions"));
                    packages.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        req.setAttribute("plansList", plans);
        req.setAttribute("therapistsList", therapists);
        req.setAttribute("packagesList", packages);
    }
}
