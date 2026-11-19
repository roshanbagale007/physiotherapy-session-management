package com.physio.web.servlet;

import com.physio.web.config.DatabaseConnectionPool;
import com.physio.web.util.PasswordHasher;
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
import java.sql.SQLException;

/**
 * Unit 2.1 - 2.4: Servlet for User Authentication and Session Creation.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // If user already logged in, redirect to their role dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userRole") != null) {
            String role = (String) session.getAttribute("userRole");
            redirectToDashboard(req, resp, role);
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Username and password cannot be empty.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        String passwordHash = PasswordHasher.hash(password.trim());

        String sql = "SELECT id, username, full_name, email, role, is_active FROM users WHERE username = ? AND password_hash = ?";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ps.setString(2, passwordHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean active = rs.getBoolean("is_active");
                    if (!active) {
                        req.setAttribute("errorMessage", "Account is deactivated. Contact clinic administrator.");
                        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                        return;
                    }

                    // Unit 2.4: Create new HTTP Session & Store Principal Data
                    HttpSession session = req.getSession(true);
                    session.setAttribute("userId", rs.getLong("id"));
                    session.setAttribute("username", rs.getString("username"));
                    session.setAttribute("fullName", rs.getString("full_name"));
                    session.setAttribute("email", rs.getString("email"));
                    String role = rs.getString("role");
                    session.setAttribute("userRole", role);
                    session.setAttribute("user", rs.getString("full_name"));

                    // Unit 2.3: Post-Redirect-Get to appropriate Role Dashboard
                    redirectToDashboard(req, resp, role);
                    return;

                } else {
                    req.setAttribute("errorMessage", "Invalid username or password.");
                    req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Database connection error: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }

    private void redirectToDashboard(HttpServletRequest req, HttpServletResponse resp, String role) throws IOException {
        String ctx = req.getContextPath();
        switch (role.toUpperCase()) {
            case "ADMIN" -> resp.sendRedirect(ctx + "/admin/dashboard");
            case "THERAPIST" -> resp.sendRedirect(ctx + "/doctor/dashboard");
            case "RECEPTIONIST" -> resp.sendRedirect(ctx + "/desk/dashboard");
            case "PATIENT" -> resp.sendRedirect(ctx + "/patient/dashboard");
            default -> resp.sendRedirect(ctx + "/login");
        }
    }
}
