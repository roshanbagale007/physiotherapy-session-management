package com.physio.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Unit 2.1 - 2.4: Patient Exercise Adherence Tracking Servlet.
 */
@WebServlet("/patient/log-exercise")
public class ExerciseAdherenceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String exerciseName = req.getParameter("exerciseName");
        if (exerciseName == null) exerciseName = "Home Rehabilitation Exercise";

        // Post-Redirect-Get
        String encoded = URLEncoder.encode(exerciseName, StandardCharsets.UTF_8);
        resp.sendRedirect(req.getContextPath() + "/patient/dashboard?success=Great+job!+You+logged+practice+for:+" + encoded);
    }
}
