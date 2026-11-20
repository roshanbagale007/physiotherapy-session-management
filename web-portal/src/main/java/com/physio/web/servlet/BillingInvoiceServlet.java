package com.physio.web.servlet;

import com.physio.web.config.DatabaseConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Phase 9 Deliverable 2: Clinical Invoicing & Package Billing Engine Servlet.
 * Handles package purchases, remaining session credit balance, and financial summaries.
 * Author: ROSHAN NAMDEO BAGALE | Roll No: 50 | Div D | 24MCAC302
 */
@WebServlet({"/desk/invoices", "/admin/invoices"})
public class BillingInvoiceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Map<String, Object>> invoicesList = new ArrayList<>();
        List<Map<String, Object>> patientsList = new ArrayList<>();

        double totalRevenue = 0.0;
        int activePackagesCount = 0;
        int totalRemainingSessions = 0;

        String invoicesSql = """
            SELECT pi.id, pi.package_name, pi.total_sessions, pi.remaining_sessions,
                   pi.total_amount, pi.discount_amount, pi.net_amount,
                   pi.payment_status, pi.payment_method, pi.created_at,
                   p.id AS patient_id, u.full_name AS patient_name, u.phone AS patient_phone
            FROM packages_invoices pi
            JOIN patients p ON pi.patient_id = p.id
            JOIN users u ON p.user_id = u.id
            ORDER BY pi.id DESC
        """;

        String patientsSql = """
            SELECT p.id, u.full_name, u.phone
            FROM patients p
            JOIN users u ON p.user_id = u.id
            ORDER BY u.full_name ASC
        """;

        try (Connection conn = DatabaseConnectionPool.getConnection()) {

            // 1. Fetch Invoices
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(invoicesSql)) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    double net = rs.getDouble("net_amount");
                    int rem = rs.getInt("remaining_sessions");

                    totalRevenue += net;
                    if (rem > 0) {
                        activePackagesCount++;
                        totalRemainingSessions += rem;
                    }

                    map.put("id", rs.getLong("id"));
                    map.put("packageName", rs.getString("package_name"));
                    map.put("totalSessions", rs.getInt("total_sessions"));
                    map.put("remainingSessions", rem);
                    map.put("totalAmount", rs.getDouble("total_amount"));
                    map.put("discountAmount", rs.getDouble("discount_amount"));
                    map.put("netAmount", net);
                    map.put("paymentStatus", rs.getString("payment_status"));
                    map.put("paymentMethod", rs.getString("payment_method"));
                    map.put("createdAt", rs.getTimestamp("created_at"));
                    map.put("patientId", rs.getLong("patient_id"));
                    map.put("patientName", rs.getString("patient_name"));
                    map.put("patientPhone", rs.getString("patient_phone"));

                    invoicesList.add(map);
                }
            }

            // 2. Fetch Patients for Dropdown
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(patientsSql)) {
                while (rs.next()) {
                    Map<String, Object> p = new HashMap<>();
                    p.put("id", rs.getLong("id"));
                    p.put("fullName", rs.getString("full_name"));
                    p.put("phone", rs.getString("phone"));
                    patientsList.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.setAttribute("invoicesList", invoicesList);
        req.setAttribute("patientsList", patientsList);
        req.setAttribute("totalRevenue", totalRevenue);
        req.setAttribute("activePackagesCount", activePackagesCount);
        req.setAttribute("totalRemainingSessions", totalRemainingSessions);

        req.getRequestDispatcher("/WEB-INF/views/desk-invoices.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long patientId = Long.parseLong(req.getParameter("patientId"));
            String packageName = req.getParameter("packageName");
            int totalSessions = Integer.parseInt(req.getParameter("totalSessions"));
            double totalAmount = Double.parseDouble(req.getParameter("totalAmount"));
            double discountAmount = 0.0;
            String discountStr = req.getParameter("discountAmount");
            if (discountStr != null && !discountStr.isBlank()) {
                discountAmount = Double.parseDouble(discountStr);
            }
            double netAmount = Math.max(0.0, totalAmount - discountAmount);
            String paymentMethod = req.getParameter("paymentMethod");
            if (paymentMethod == null || paymentMethod.isBlank()) paymentMethod = "UPI";

            String insertSql = """
                INSERT INTO packages_invoices
                (patient_id, package_name, total_sessions, remaining_sessions, total_amount, discount_amount, net_amount, payment_status, payment_method)
                VALUES (?, ?, ?, ?, ?, ?, ?, 'PAID', ?)
            """;

            try (Connection conn = DatabaseConnectionPool.getConnection();
                 PreparedStatement ps = conn.prepareStatement(insertSql)) {

                ps.setLong(1, patientId);
                ps.setString(2, packageName);
                ps.setInt(3, totalSessions);
                ps.setInt(4, totalSessions); // Initial remaining = total
                ps.setBigDecimal(5, BigDecimal.valueOf(totalAmount));
                ps.setBigDecimal(6, BigDecimal.valueOf(discountAmount));
                ps.setBigDecimal(7, BigDecimal.valueOf(netAmount));
                ps.setString(8, paymentMethod.toUpperCase());

                ps.executeUpdate();
            }

            resp.sendRedirect(req.getContextPath() + "/desk/invoices?success=Package+invoice+generated+and+credits+allocated+successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/desk/invoices?error=" + e.getMessage());
        }
    }
}
