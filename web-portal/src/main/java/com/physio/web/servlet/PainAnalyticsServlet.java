package com.physio.web.servlet;

import com.physio.web.config.DatabaseConnectionPool;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Phase 9 Deliverable 1: Visual Pain Recovery Trajectory & VAS Analytics Servlet.
 * Provides chronological pain score delta curves, ROM progression, and recovery milestones.
 * Author: ROSHAN NAMDEO BAGALE | Roll No: 50 | Div D | 24MCAC302
 */
@WebServlet({"/patient/pain-analytics", "/doctor/patient-analytics"})
public class PainAnalyticsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String userRole = (String) session.getAttribute("userRole");
        Long userId = (Long) session.getAttribute("userId");

        Long patientId = null;

        // If patient is viewing, resolve patientId from their own user account
        if ("PATIENT".equalsIgnoreCase(userRole)) {
            try (Connection conn = DatabaseConnectionPool.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT id FROM patients WHERE user_id = ?")) {
                ps.setLong(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        patientId = rs.getLong("id");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Therapist or Admin viewing a specific patient
            String paramId = req.getParameter("patientId");
            if (paramId != null && !paramId.isBlank()) {
                try {
                    patientId = Long.parseLong(paramId);
                } catch (NumberFormatException ignored) {}
            }
            if (patientId == null) {
                // Default to first patient
                try (Connection conn = DatabaseConnectionPool.getConnection();
                     PreparedStatement ps = conn.prepareStatement("SELECT id FROM patients ORDER BY id ASC LIMIT 1");
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        patientId = rs.getLong("id");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (patientId == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=Patient+record+not+found.");
            return;
        }

        Map<String, Object> patientInfo = new HashMap<>();
        Map<String, Object> planInfo = new HashMap<>();
        List<Map<String, Object>> trajectoryList = new ArrayList<>();

        StringBuilder labelsJson = new StringBuilder("[");
        StringBuilder beforeJson = new StringBuilder("[");
        StringBuilder afterJson = new StringBuilder("[");
        StringBuilder deltaJson = new StringBuilder("[");

        int baselinePain = 0;
        int latestPain = 0;
        boolean firstRecord = true;

        try (Connection conn = DatabaseConnectionPool.getConnection()) {

            // 1. Fetch Patient Profile
            String patientSql = """
                SELECT p.id, u.full_name, u.email, u.phone, p.gender, p.blood_group,
                       p.date_of_birth, p.primary_complaint, p.medical_history
                FROM patients p
                JOIN users u ON p.user_id = u.id
                WHERE p.id = ?
            """;
            try (PreparedStatement ps = conn.prepareStatement(patientSql)) {
                ps.setLong(1, patientId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        patientInfo.put("id", rs.getLong("id"));
                        patientInfo.put("fullName", rs.getString("full_name"));
                        patientInfo.put("email", rs.getString("email"));
                        patientInfo.put("phone", rs.getString("phone"));
                        patientInfo.put("gender", rs.getString("gender"));
                        patientInfo.put("bloodGroup", rs.getString("blood_group"));
                        patientInfo.put("dateOfBirth", rs.getDate("date_of_birth"));
                        patientInfo.put("primaryComplaint", rs.getString("primary_complaint"));
                        patientInfo.put("medicalHistory", rs.getString("medical_history"));
                    }
                }
            }

            // 2. Fetch Active Treatment Plan
            String planSql = """
                SELECT tp.id, tp.diagnosis, tp.treatment_goal, tp.target_sessions,
                       tp.completed_sessions, tp.status, tp.start_date,
                       ut.full_name AS therapist_name, t.specialization
                FROM treatment_plans tp
                JOIN therapists t ON tp.therapist_id = t.id
                JOIN users ut ON t.user_id = ut.id
                WHERE tp.patient_id = ? AND tp.status = 'ACTIVE'
                ORDER BY tp.id DESC LIMIT 1
            """;
            try (PreparedStatement ps = conn.prepareStatement(planSql)) {
                ps.setLong(1, patientId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        planInfo.put("id", rs.getLong("id"));
                        planInfo.put("diagnosis", rs.getString("diagnosis"));
                        planInfo.put("treatmentGoal", rs.getString("treatment_goal"));
                        planInfo.put("targetSessions", rs.getInt("target_sessions"));
                        planInfo.put("completedSessions", rs.getInt("completed_sessions"));
                        planInfo.put("status", rs.getString("status"));
                        planInfo.put("startDate", rs.getDate("start_date"));
                        planInfo.put("therapistName", rs.getString("therapist_name"));
                        planInfo.put("specialization", rs.getString("specialization"));
                    }
                }
            }

            // 3. Fetch Chronological SOAP Pain Trajectory
            String soapSql = """
                SELECT sn.id, s.id AS session_id, s.session_date, s.start_time,
                       sn.pain_score_before, sn.pain_score_after,
                       (sn.pain_score_before - sn.pain_score_after) AS pain_delta,
                       sn.range_of_motion_notes, sn.muscle_strength_grade,
                       sn.subjective_notes, sn.objective_findings,
                       sn.assessment_summary, sn.modalities_used,
                       ut.full_name AS therapist_name
                FROM soap_notes sn
                JOIN sessions s ON sn.session_id = s.id
                JOIN therapists t ON s.therapist_id = t.id
                JOIN users ut ON t.user_id = ut.id
                WHERE s.patient_id = ?
                ORDER BY s.session_date ASC, s.start_time ASC
            """;
            try (PreparedStatement ps = conn.prepareStatement(soapSql)) {
                ps.setLong(1, patientId);
                try (ResultSet rs = ps.executeQuery()) {
                    int visitIndex = 1;
                    while (rs.next()) {
                        Map<String, Object> map = new HashMap<>();
                        int pBefore = rs.getInt("pain_score_before");
                        int pAfter = rs.getInt("pain_score_after");
                        int delta = rs.getInt("pain_delta");

                        if (firstRecord) {
                            baselinePain = pBefore;
                            firstRecord = false;
                        }
                        latestPain = pAfter;

                        map.put("visitNo", visitIndex);
                        map.put("sessionId", rs.getLong("session_id"));
                        map.put("sessionDate", rs.getDate("session_date"));
                        map.put("startTime", rs.getTime("start_time"));
                        map.put("painBefore", pBefore);
                        map.put("painAfter", pAfter);
                        map.put("painDelta", delta);
                        map.put("romNotes", rs.getString("range_of_motion_notes"));
                        map.put("muscleGrade", rs.getString("muscle_strength_grade"));
                        map.put("subjective", rs.getString("subjective_notes"));
                        map.put("objective", rs.getString("objective_findings"));
                        map.put("assessment", rs.getString("assessment_summary"));
                        map.put("modalities", rs.getString("modalities_used"));
                        map.put("therapistName", rs.getString("therapist_name"));

                        trajectoryList.add(map);

                        // Build JSON for Chart.js
                        if (visitIndex > 1) {
                            labelsJson.append(", ");
                            beforeJson.append(", ");
                            afterJson.append(", ");
                            deltaJson.append(", ");
                        }
                        labelsJson.append("'S").append(visitIndex).append(" (").append(rs.getDate("session_date")).append(")'");
                        beforeJson.append(pBefore);
                        afterJson.append(pAfter);
                        deltaJson.append(delta);

                        visitIndex++;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        labelsJson.append("]");
        beforeJson.append("]");
        afterJson.append("]");
        deltaJson.append("]");

        int totalRelief = Math.max(0, baselinePain - latestPain);
        double reliefPct = baselinePain > 0 ? ((double) totalRelief / baselinePain) * 100.0 : 0.0;

        String recoveryStatus;
        String statusColor;
        if (reliefPct >= 65.0) {
            recoveryStatus = "Optimal Recovery Trajectory — Excellent functional load adaptation (Pain reduced > 65%)";
            statusColor = "success";
        } else if (reliefPct >= 35.0) {
            recoveryStatus = "Progressive Recovery — Favorable pain centralization; continuing core stability protocol";
            statusColor = "warning";
        } else if (!trajectoryList.isEmpty()) {
            recoveryStatus = "Acute Phase — Managing inflammation and neuromuscular inhibition";
            statusColor = "primary";
        } else {
            recoveryStatus = "Initial Consultation Pending — No recorded SOAP evaluations yet";
            statusColor = "secondary";
        }

        req.setAttribute("patientInfo", patientInfo);
        req.setAttribute("planInfo", planInfo);
        req.setAttribute("trajectoryList", trajectoryList);
        req.setAttribute("baselinePain", baselinePain);
        req.setAttribute("latestPain", latestPain);
        req.setAttribute("totalRelief", totalRelief);
        req.setAttribute("reliefPct", String.format("%.1f", reliefPct));
        req.setAttribute("recoveryStatus", recoveryStatus);
        req.setAttribute("statusColor", statusColor);

        req.setAttribute("chartLabelsJson", labelsJson.toString());
        req.setAttribute("chartBeforeJson", beforeJson.toString());
        req.setAttribute("chartAfterJson", afterJson.toString());
        req.setAttribute("chartDeltaJson", deltaJson.toString());

        req.getRequestDispatcher("/WEB-INF/views/patient-pain-analytics.jsp").forward(req, resp);
    }
}
