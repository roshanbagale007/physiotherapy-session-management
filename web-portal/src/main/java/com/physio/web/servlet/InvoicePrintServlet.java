package com.physio.web.servlet;

import com.physio.web.config.DatabaseConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Phase 9 Deliverable 2: Digital Printable Medical Tax Invoice Servlet.
 * Generates official clinic invoice with tax breakdown and student metadata.
 * Author: ROSHAN NAMDEO BAGALE | Roll No: 50 | Div D | 24MCAC302
 */
@WebServlet({"/desk/invoice-print", "/admin/invoice-print"})
public class InvoicePrintServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/desk/invoices?error=Missing+invoice+ID.");
            return;
        }

        try {
            Long invoiceId = Long.parseLong(idStr);
            Map<String, Object> inv = new HashMap<>();

            String sql = """
                SELECT pi.id, pi.package_name, pi.total_sessions, pi.remaining_sessions,
                       pi.total_amount, pi.discount_amount, pi.net_amount,
                       pi.payment_status, pi.payment_method, pi.created_at,
                       p.id AS patient_id, u.full_name AS patient_name, u.email AS patient_email,
                       u.phone AS patient_phone, p.gender, p.blood_group, p.date_of_birth,
                       tp.diagnosis, ut.full_name AS therapist_name
                FROM packages_invoices pi
                JOIN patients p ON pi.patient_id = p.id
                JOIN users u ON p.user_id = u.id
                LEFT JOIN treatment_plans tp ON p.id = tp.patient_id AND tp.status = 'ACTIVE'
                LEFT JOIN therapists t ON tp.therapist_id = t.id
                LEFT JOIN users ut ON t.user_id = ut.id
                WHERE pi.id = ?
            """;

            try (Connection conn = DatabaseConnectionPool.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setLong(1, invoiceId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double totalAmt = rs.getDouble("total_amount");
                        double discountAmt = rs.getDouble("discount_amount");
                        double netAmt = rs.getDouble("net_amount");

                        // 18% GST Breakdown (Inclusive calculation)
                        double baseTaxable = netAmt / 1.18;
                        double totalGst = netAmt - baseTaxable;
                        double cgst = totalGst / 2.0;
                        double sgst = totalGst / 2.0;

                        inv.put("id", rs.getLong("id"));
                        inv.put("invoiceNumber", String.format("INV-2026-%04d", rs.getLong("id")));
                        inv.put("packageName", rs.getString("package_name"));
                        inv.put("totalSessions", rs.getInt("total_sessions"));
                        inv.put("remainingSessions", rs.getInt("remaining_sessions"));
                        inv.put("usedSessions", rs.getInt("total_sessions") - rs.getInt("remaining_sessions"));
                        inv.put("totalAmount", String.format("%.2f", totalAmt));
                        inv.put("discountAmount", String.format("%.2f", discountAmt));
                        inv.put("netAmount", String.format("%.2f", netAmt));
                        inv.put("baseTaxable", String.format("%.2f", baseTaxable));
                        inv.put("cgst", String.format("%.2f", cgst));
                        inv.put("sgst", String.format("%.2f", sgst));
                        inv.put("paymentStatus", rs.getString("payment_status"));
                        inv.put("paymentMethod", rs.getString("payment_method"));
                        inv.put("createdAt", rs.getTimestamp("created_at"));

                        inv.put("patientName", rs.getString("patient_name"));
                        inv.put("patientEmail", rs.getString("patient_email"));
                        inv.put("patientPhone", rs.getString("patient_phone"));
                        inv.put("gender", rs.getString("gender"));
                        inv.put("bloodGroup", rs.getString("blood_group"));

                        inv.put("diagnosis", rs.getString("diagnosis") != null ? rs.getString("diagnosis") : "Physiotherapy & Musculoskeletal Rehabilitation");
                        inv.put("therapistName", rs.getString("therapist_name") != null ? rs.getString("therapist_name") : "Dr. Sameer Deshmukh, MPT");
                    } else {
                        resp.sendRedirect(req.getContextPath() + "/desk/invoices?error=Invoice+not+found.");
                        return;
                    }
                }
            }

            req.setAttribute("inv", inv);
            req.getRequestDispatcher("/WEB-INF/views/invoice-print.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/desk/invoices?error=" + e.getMessage());
        }
    }
}
