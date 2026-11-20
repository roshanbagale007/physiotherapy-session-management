<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Treatment Plans | Spring MVC & JPA</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <!-- Navigation Bar -->
    <header class="navbar">
        <a href="${pageContext.request.contextPath}/plans" class="brand">
            🌿 <span>PhysioCare Spring MVC Service</span>
        </a>
        <nav class="nav-links">
            <a href="${pageContext.request.contextPath}/plans" class="active">Treatment Plans</a>
            <a href="${pageContext.request.contextPath}/plans/new">➕ New Plan</a>
            <a href="${pageContext.request.contextPath}/plans/report">📊 Clinic Report</a>
            <a href="http://localhost:8025/web-portal/" target="_blank">🌐 Web Portal</a>
        </nav>
    </header>

    <div class="container">
        <!-- Header Row -->
        <div class="header-row">
            <div>
                <h1 class="page-title">Clinical Treatment Plans</h1>
                <p class="page-subtitle">Unit 3: Managed with Spring 6 MVC, JPA 3.1 ORM & JPQL Queries</p>
            </div>
            <div style="display: flex; gap: 0.8rem;">
                <a href="${pageContext.request.contextPath}/plans/new" class="btn btn-primary">➕ Create Treatment Plan</a>
                <a href="${pageContext.request.contextPath}/plans/report" class="btn btn-secondary">📈 View Analytics</a>
            </div>
        </div>

        <!-- Success/Error Feedback Alerts -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                ✅ <span>${successMessage}</span>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                ⚠️ <span>${errorMessage}</span>
            </div>
        </c:if>

        <!-- Plans Table Card -->
        <div class="card">
            <div class="card-title">
                <span>Active & Historic Treatment Plans</span>
                <span style="font-size: 0.85rem; font-weight: normal; color: var(--text-muted);">
                    Total: <strong>${plans.size()}</strong> plans loaded via JPQL
                </span>
            </div>

            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Plan ID</th>
                            <th>Patient Name</th>
                            <th>Assigned Physiotherapist</th>
                            <th>Diagnosis & Goals</th>
                            <th>Timeline</th>
                            <th>Session Progress</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="plan" items="${plans}">
                            <tr>
                                <td><strong>#${plan.id}</strong></td>
                                <td>
                                    <strong>${plan.patient.user.fullName}</strong>
                                    <div style="font-size: 0.8rem; color: var(--text-muted);">
                                        DOB: ${plan.patient.dateOfBirth} (${plan.patient.gender})
                                    </div>
                                </td>
                                <td>
                                    ${plan.therapist.user.fullName}
                                    <div style="font-size: 0.8rem; color: var(--text-muted);">
                                        ${plan.therapist.specialization}
                                    </div>
                                </td>
                                <td>
                                    <strong style="color: var(--primary-dark);">${plan.diagnosis}</strong>
                                    <div style="font-size: 0.82rem; color: var(--text-muted); max-width: 280px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                                        Goal: ${plan.treatmentGoal}
                                    </div>
                                </td>
                                <td>
                                    <div style="font-size: 0.85rem;">Start: <strong>${plan.startDate}</strong></div>
                                    <c:if test="${not empty plan.estimatedEndDate}">
                                        <div style="font-size: 0.8rem; color: var(--text-muted);">Est: ${plan.estimatedEndDate}</div>
                                    </c:if>
                                </td>
                                <td style="min-width: 140px;">
                                    <div style="display: flex; justify-content: space-between; font-size: 0.8rem; font-weight: 600;">
                                        <span>${plan.completedSessions} / ${plan.targetSessions} Sessions</span>
                                        <span>
                                            <fmt:formatNumber value="${(plan.completedSessions / plan.targetSessions) * 100}" maxFractionDigits="0"/>%
                                        </span>
                                    </div>
                                    <c:set var="progressPct" value="${plan.targetSessions > 0 ? (plan.completedSessions * 100 / plan.targetSessions) : 0}"/>
                                    <div class="progress-bar-container">
                                        <div class="progress-bar-fill" style="width: ${progressPct > 100 ? 100 : progressPct}%;"></div>
                                    </div>
                                </td>
                                <td>
                                    <span class="badge ${plan.status == 'ACTIVE' ? 'badge-active' : 'badge-completed'}">
                                        ${plan.status}
                                    </span>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/plans/${plan.id}" class="btn btn-secondary btn-sm">
                                        View Details 🔍
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <footer>
        Physiotherapy Session Management &bull; MCA Sem III (24MCAC302) &bull; Spring 6 MVC + JPA 3.1 ORM
    </footer>
</body>
</html>
