<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clinical Recovery &amp; Adherence Report | Spring MVC &amp; JPQL</title>
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
            <a href="${pageContext.request.contextPath}/plans/report" class="active">📊 Clinic Report</a>
            <a href="http://localhost:8025/web-portal/" target="_blank">🌐 Web Portal</a>
        </nav>
    </header>

    <div class="container">
        <!-- Header Row -->
        <div class="header-row">
            <div>
                <h1 class="page-title">Clinic Recovery &amp; Protocol Adherence Report</h1>
                <p class="page-subtitle">Unit 3.4: Real-time JPA aggregate statistics computed via JPQL Queries</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/plans" class="btn btn-secondary">⬅ Back to Plans</a>
            </div>
        </div>

        <!-- Metric KPI Cards Grid -->
        <div class="stats-grid">
            <div class="stat-box">
                <div class="stat-label">Total Treatment Plans</div>
                <div class="stat-value">${stats.totalPlans}</div>
            </div>
            <div class="stat-box accent">
                <div class="stat-label">Active Clinical Cases</div>
                <div class="stat-value">${stats.activePlans}</div>
            </div>
            <div class="stat-box success">
                <div class="stat-label">Completed Protocols</div>
                <div class="stat-value">${stats.completedPlans}</div>
            </div>
            <div class="stat-box warning">
                <div class="stat-label">Overall Adherence Rate</div>
                <div class="stat-value">${stats.averageAdherencePercent}%</div>
            </div>
        </div>

        <!-- Adherence Breakdown Card -->
        <div class="card">
            <div class="card-title">
                <span>Total Clinical Session Delivery</span>
            </div>
            <div style="margin-bottom: 0.6rem; font-size: 1rem; color: var(--text-main);">
                <strong>${stats.totalCompletedSessions}</strong> attended out of <strong>${stats.totalTargetSessions}</strong> targeted therapy sessions across all active protocols.
            </div>
            <div class="progress-bar-container" style="height: 16px;">
                <div class="progress-bar-fill" style="width: ${stats.averageAdherencePercent}%;"></div>
            </div>
        </div>

        <!-- Patient Protocol Breakdown Table -->
        <div class="card">
            <div class="card-title">
                <span>Patient Protocol Summary</span>
            </div>
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Patient</th>
                            <th>Lead Physiotherapist</th>
                            <th>Diagnosis</th>
                            <th>Start Date</th>
                            <th>Completed / Target</th>
                            <th>Adherence</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="plan" items="${plans}">
                            <tr>
                                <td><strong>${plan.patient.user.fullName}</strong></td>
                                <td>${plan.therapist.user.fullName}</td>
                                <td>${plan.diagnosis}</td>
                                <td>${plan.startDate}</td>
                                <td>${plan.completedSessions} / ${plan.targetSessions}</td>
                                <td>
                                    <fmt:formatNumber value="${(plan.completedSessions / plan.targetSessions) * 100}" maxFractionDigits="1"/>%
                                </td>
                                <td>
                                    <span class="badge ${plan.status == 'ACTIVE' ? 'badge-active' : 'badge-completed'}">${plan.status}</span>
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
