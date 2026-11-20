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

/**
 * Unit 2.1 & Phase 9: Admin Executive Dashboard Servlet with Chart.js Analytics.
 * Aggregates clinic revenue, attendance velocity, and clinical diagnosis distribution.
 * Author: ROSHAN NAMDEO BAGALE | Roll No: 50 | Div D | 24MCAC302
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int patientCount = 0;
        int therapistCount = 0;
        int sessionCount = 0;
        double totalRevenue = 0.0;

        StringBuilder pkgLabelsJson = new StringBuilder("[");
        StringBuilder pkgRevJson = new StringBuilder("[");

        StringBuilder statusLabelsJson = new StringBuilder("[");
        StringBuilder statusCountJson = new StringBuilder("[");

        try (Connection conn = DatabaseConnectionPool.getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. KPI Counts
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM patients")) {
                if (rs.next()) patientCount = rs.getInt(1);
            }
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM therapists")) {
                if (rs.next()) therapistCount = rs.getInt(1);
            }
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM sessions")) {
                if (rs.next()) sessionCount = rs.getInt(1);
            }
            try (ResultSet rs = stmt.executeQuery("SELECT COALESCE(SUM(net_amount), 0) FROM packages_invoices")) {
                if (rs.next()) totalRevenue = rs.getDouble(1);
            }

            // 2. Revenue by Package Category
            String pkgSql = """
                SELECT package_name, SUM(net_amount) AS rev
                FROM packages_invoices
                GROUP BY package_name
                ORDER BY rev DESC
            """;
            try (ResultSet rs = stmt.executeQuery(pkgSql)) {
                boolean first = true;
                while (rs.next()) {
                    if (!first) {
                        pkgLabelsJson.append(", ");
                        pkgRevJson.append(", ");
                    }
                    pkgLabelsJson.append("'").append(rs.getString("package_name").replace("'", "\\'")).append("'");
                    pkgRevJson.append(rs.getDouble("rev"));
                    first = false;
                }
            }

            // 3. Attendance Status Velocity
            String statusSql = """
                SELECT status, COUNT(*) AS count
                FROM sessions
                GROUP BY status
                ORDER BY count DESC
            """;
            try (ResultSet rs = stmt.executeQuery(statusSql)) {
                boolean first = true;
                while (rs.next()) {
                    if (!first) {
                        statusLabelsJson.append(", ");
                        statusCountJson.append(", ");
                    }
                    statusLabelsJson.append("'").append(rs.getString("status")).append("'");
                    statusCountJson.append(rs.getInt("count"));
                    first = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        pkgLabelsJson.append("]");
        pkgRevJson.append("]");
        statusLabelsJson.append("]");
        statusCountJson.append("]");

        req.setAttribute("patientCount", patientCount);
        req.setAttribute("therapistCount", therapistCount);
        req.setAttribute("sessionCount", sessionCount);
        req.setAttribute("totalRevenue", String.format("%.2f", totalRevenue));

        req.setAttribute("pkgLabelsJson", pkgLabelsJson.toString());
        req.setAttribute("pkgRevJson", pkgRevJson.toString());
        req.setAttribute("statusLabelsJson", statusLabelsJson.toString());
        req.setAttribute("statusCountJson", statusCountJson.toString());

        req.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp").forward(req, resp);
    }
}
