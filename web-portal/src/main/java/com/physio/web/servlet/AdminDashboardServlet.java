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
 * Unit 2.1: Admin Dashboard Servlet.
 * Aggregates clinic operational KPIs and renders executive view.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int patientCount = 0;
        int therapistCount = 0;
        int sessionCount = 0;
        double totalRevenue = 0.0;

        try (Connection conn = DatabaseConnectionPool.getConnection();
             Statement stmt = conn.createStatement()) {

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.setAttribute("patientCount", patientCount);
        req.setAttribute("therapistCount", therapistCount);
        req.setAttribute("sessionCount", sessionCount);
        req.setAttribute("totalRevenue", totalRevenue);

        req.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp").forward(req, resp);
    }
}
