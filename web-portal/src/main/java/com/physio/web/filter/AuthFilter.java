package com.physio.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Unit 2.5: Servlet Filters for Role-Based Access Control (RBAC).
 * Intercepts requests, protects secured routes, and enforces role boundaries.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // 1. Allow public routes & static assets without authentication
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Unit 2.4: Check active HTTP Session
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=Authentication required. Please log in.");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");

        // 3. Enforce Role-Based Access Boundaries
        if (path.startsWith("/admin") && !"ADMIN".equalsIgnoreCase(userRole)) {
            forward403(req, resp, "Administrator access required.");
            return;
        }

        if (path.startsWith("/doctor") && !"THERAPIST".equalsIgnoreCase(userRole) && !"ADMIN".equalsIgnoreCase(userRole)) {
            forward403(req, resp, "Physiotherapist (Doctor) or Admin access required.");
            return;
        }

        if (path.startsWith("/desk") && !"RECEPTIONIST".equalsIgnoreCase(userRole) && !"ADMIN".equalsIgnoreCase(userRole)) {
            forward403(req, resp, "Front-Desk Receptionist or Admin access required.");
            return;
        }

        if (path.startsWith("/patient") && !"PATIENT".equalsIgnoreCase(userRole) && !"ADMIN".equalsIgnoreCase(userRole) && !"THERAPIST".equalsIgnoreCase(userRole)) {
            forward403(req, resp, "Patient Portal access required.");
            return;
        }

        // Authorized: Proceed through filter chain
        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return path.equals("/") ||
               path.equals("/login") ||
               path.equals("/logout") ||
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.equals("/favicon.ico");
    }

    private void forward403(HttpServletRequest req, HttpServletResponse resp, String reason)
            throws ServletException, IOException {
        req.setAttribute("forbiddenReason", reason);
        req.getRequestDispatcher("/WEB-INF/views/error-403.jsp").forward(req, resp);
    }
}
