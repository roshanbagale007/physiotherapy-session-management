<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Treatment Plan #${plan.id} Details | Spring MVC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <!-- Navigation Bar -->
    <header class="navbar">
        <a href="${pageContext.request.contextPath}/plans" class="brand">
            🌿 <span>PhysioCare Spring MVC Service</span>
        </a>
        <nav class="nav-links">
            <a href="${pageContext.request.contextPath}/plans">Treatment Plans</a>
            <a href="${pageContext.request.contextPath}/plans/new">➕ New Plan</a>
            <a href="${pageContext.request.contextPath}/plans/report">📊 Clinic Report</a>
            <a href="http://localhost:8025/web-portal/" target="_blank">🌐 Web Portal</a>
        </nav>
    </header>

    <div class="container">
        <!-- Header Row -->
        <div class="header-row">
            <div>
                <h1 class="page-title">Treatment Plan #${plan.id}</h1>
                <p class="page-subtitle">Patient: <strong>${plan.patient.user.fullName}</strong> &bull; Therapist: <strong>${plan.therapist.user.fullName}</strong></p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/plans" class="btn btn-secondary">⬅ Back to Plans List</a>
            </div>
        </div>

        <!-- Plan Summary Grid -->
        <div class="stats-grid">
            <div class="stat-box">
                <div class="stat-label">Plan Status</div>
                <div class="stat-value" style="font-size: 1.4rem;">
                    <span class="badge ${plan.status == 'ACTIVE' ? 'badge-active' : 'badge-completed'}">${plan.status}</span>
                </div>
            </div>
            <div class="stat-box success">
                <div class="stat-label">Completed Sessions</div>
                <div class="stat-value">${plan.completedSessions} <span style="font-size: 1rem; color: var(--text-muted); font-weight: normal;">/ ${plan.targetSessions}</span></div>
            </div>
            <div class="stat-box accent">
                <div class="stat-label">Rehabilitation Adherence</div>
                <div class="stat-value">
                    <fmt:formatNumber value="${(plan.completedSessions / plan.targetSessions) * 100}" maxFractionDigits="0"/>%
                </div>
            </div>
            <div class="stat-box warning">
                <div class="stat-label">Start Date</div>
                <div class="stat-value" style="font-size: 1.3rem;">${plan.startDate}</div>
            </div>
        </div>

        <!-- Clinical Details Card -->
        <div class="card">
            <div class="card-title">
                <span>Clinical Diagnosis &amp; Rehabilitation Protocol</span>
            </div>
            <div style="margin-bottom: 1rem;">
                <label style="color: var(--text-muted); font-size: 0.85rem;">PRIMARY DIAGNOSIS</label>
                <div style="font-size: 1.15rem; font-weight: 700; color: var(--primary-dark);">${plan.diagnosis}</div>
            </div>
            <div>
                <label style="color: var(--text-muted); font-size: 0.85rem;">TREATMENT GOAL &amp; FUNCTIONAL TARGETS</label>
                <p style="background: #f8fafc; padding: 1rem; border-radius: 6px; border: 1px solid var(--border); margin-top: 0.3rem;">
                    ${plan.treatmentGoal}
                </p>
            </div>
        </div>

        <!-- Linked Sessions & SOAP Notes Timeline Card -->
        <div class="card">
            <div class="card-title">
                <span>Scheduled &amp; Attended Sessions Timeline</span>
                <span style="font-size: 0.85rem; font-weight: normal; color: var(--text-muted);">
                    ${sessions.size()} Sessions Recorded
                </span>
            </div>

            <c:choose>
                <c:when test="${empty sessions}">
                    <div style="text-align: center; padding: 2rem; color: var(--text-muted);">
                        No sessions recorded yet under this treatment plan.
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table>
                            <thead>
                                <tr>
                                    <th>Session ID</th>
                                    <th>Date &amp; Slot</th>
                                    <th>Status</th>
                                    <th>Pain (Before ➔ After)</th>
                                    <th>ROM &amp; Strength Grade</th>
                                    <th>Clinical SOAP Assessment</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="s" items="${sessions}">
                                    <tr>
                                        <td><strong>#${s.id}</strong></td>
                                        <td>
                                            <strong>${s.sessionDate}</strong>
                                            <div style="font-size: 0.8rem; color: var(--text-muted);">${s.startTime} - ${s.endTime}</div>
                                        </td>
                                        <td>
                                            <span class="badge ${s.status == 'ATTENDED' ? 'badge-attended' : (s.status == 'SCHEDULED' ? 'badge-scheduled' : 'badge-cancelled')}">
                                                ${s.status}
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty s.soapNote}">
                                                    <span style="color: var(--danger); font-weight: 700;">${s.soapNote.painScoreBefore}/10</span>
                                                    ➔
                                                    <span style="color: var(--success); font-weight: 700;">${s.soapNote.painScoreAfter}/10</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: var(--text-muted); font-size: 0.85rem;">Pending</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty s.soapNote}">
                                                    <div style="font-size: 0.85rem;"><strong>ROM:</strong> ${s.soapNote.rangeOfMotionNotes}</div>
                                                    <div style="font-size: 0.8rem; color: var(--text-muted);">Strength: <strong>${s.soapNote.muscleStrengthGrade}</strong></div>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: var(--text-muted); font-size: 0.85rem;">-</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty s.soapNote}">
                                                    <div style="font-size: 0.85rem; max-width: 320px;">
                                                        <strong>Assessment:</strong> ${s.soapNote.assessmentSummary}
                                                    </div>
                                                    <div style="font-size: 0.8rem; color: var(--text-muted); margin-top: 0.2rem;">
                                                        <em>Modalities:</em> ${s.soapNote.modalitiesUsed}
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: var(--text-muted); font-size: 0.85rem;">No clinical notes recorded</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <footer>
        Physiotherapy Session Management &bull; MCA Sem III (24MCAC302) &bull; Spring 6 MVC + JPA 3.1 ORM
    </footer>
</body>
</html>
