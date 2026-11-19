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

/**
 * Unit 2.1 - 2.4: Receptionist Attendance Marking Servlet.
 * Updates appointment status (Attended, Cancelled, Rescheduled) with Post-Redirect-Get.
 */
@WebServlet("/desk/mark-attendance")
public class AttendanceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionIdStr = req.getParameter("sessionId");
        String status = req.getParameter("status");
        String reason = req.getParameter("reason");

        if (sessionIdStr == null || status == null) {
            resp.sendRedirect(req.getContextPath() + "/desk/dashboard?error=Missing+session+parameters.");
            return;
        }

        try {
            Long sessionId = Long.parseLong(sessionIdStr);

            try (Connection conn = DatabaseConnectionPool.getConnection()) {
                conn.setAutoCommit(false); // Atomic transaction

                // 1. Update Session Status
                String updateSql = "UPDATE sessions SET status = ?, cancellation_reason = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setString(1, status);
                    ps.setString(2, reason != null && !reason.trim().isEmpty() ? reason.trim() : null);
                    ps.setLong(3, sessionId);
                    ps.executeUpdate();
                }

                // 2. If marked ATTENDED directly from front desk, update plan & package
                if ("ATTENDED".equalsIgnoreCase(status)) {
                    String getPlanSql = "SELECT plan_id, package_invoice_id FROM sessions WHERE id = ?";
                    Long planId = null;
                    Long packageId = null;

                    try (PreparedStatement psGet = conn.prepareStatement(getPlanSql)) {
                        psGet.setLong(1, sessionId);
                        try (ResultSet rs = psGet.executeQuery()) {
                            if (rs.next()) {
                                planId = rs.getLong("plan_id");
                                packageId = rs.getObject("package_invoice_id") != null ? rs.getLong("package_invoice_id") : null;
                            }
                        }
                    }

                    if (planId != null) {
                        try (PreparedStatement psPlan = conn.prepareStatement(
                                "UPDATE treatment_plans SET completed_sessions = completed_sessions + 1 WHERE id = ?")) {
                            psPlan.setLong(1, planId);
                            psPlan.executeUpdate();
                        }
                    }

                    if (packageId != null) {
                        try (PreparedStatement psPkg = conn.prepareStatement(
                                "UPDATE packages_invoices SET remaining_sessions = GREATEST(0, remaining_sessions - 1) WHERE id = ?")) {
                            psPkg.setLong(1, packageId);
                            psPkg.executeUpdate();
                        }
                    }
                }

                conn.commit();
            }

            // Post-Redirect-Get
            resp.sendRedirect(req.getContextPath() + "/desk/dashboard?success=Session+%23" + sessionId + "+marked+as+" + status + "!");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/desk/dashboard?error=Failed+to+update+attendance:+" + e.getMessage());
        }
    }
}
