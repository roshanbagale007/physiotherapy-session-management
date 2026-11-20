<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Doctor Dashboard | PhysioCare Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .btn-soap {
            background: #0284c7;
            color: white;
            padding: 0.35rem 0.75rem;
            border-radius: 4px;
            font-size: 0.82rem;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            transition: background 0.2s;
        }
        .btn-soap:hover {
            background: #0369a1;
        }
    </style>
</head>
<body>
    <header class="navbar">
        <a href="${pageContext.request.contextPath}/doctor/dashboard" class="brand">
            🩺 <span>PhysioCare Clinical Hub</span>
        </a>
        <div class="user-info">
            <span>Dr. <strong>${sessionScope.fullName}</strong></span>
            <span class="role-badge role-${sessionScope.userRole}">PHYSIOTHERAPIST</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <main class="container">
        <!-- PRG Alerts -->
        <c:if test="${not empty param.success}">
            <div class="alert alert-success">✅ ${param.success}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">⚠️ ${param.error}</div>
        </c:if>

        <div class="card">
            <div class="card-header">
                <h2 class="card-title">My Patient Appointment Queue</h2>
            </div>
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Time</th>
                            <th>Patient Name</th>
                            <th>Diagnosis / Condition</th>
                            <th>Session Status</th>
                            <th>Clinical Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty sessionsList}">
                                <c:forEach var="s" items="${sessionsList}">
                                    <tr>
                                        <td><strong>${s.sessionDate}</strong></td>
                                        <td>${s.startTime} - ${s.endTime}</td>
                                        <td><strong>${s.patientName}</strong></td>
                                        <td>${s.diagnosis}</td>
                                        <td>
                                            <span class="status-tag status-${s.status}">${s.status}</span>
                                        </td>
                                        <td>
                                            <div style="display: flex; gap: 0.5rem; align-items: center; flex-wrap: wrap;">
                                                <c:if test="${s.status eq 'SCHEDULED'}">
                                                    <a href="${pageContext.request.contextPath}/doctor/soap-note?sessionId=${s.id}" class="btn-soap">
                                                        📝 Record SOAP
                                                    </a>
                                                </c:if>
                                                <c:if test="${s.status eq 'ATTENDED'}">
                                                    <span style="color:var(--success); font-weight:600; font-size:0.82rem;">✓ Completed</span>
                                                </c:if>
                                                <c:if test="${s.status eq 'CANCELLED'}">
                                                    <span style="color:var(--text-muted); font-size:0.82rem;">Cancelled</span>
                                                </c:if>
                                                <a href="${pageContext.request.contextPath}/doctor/patient-analytics?patientId=${s.patientId}" class="btn-soap" style="background: #0d9488;">
                                                    📈 Pain Curve
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" style="text-align:center; color:var(--text-muted);">No sessions scheduled currently.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <footer>
        <p>Physiotherapy Session Management System &bull; 24MCAC302 Advance Java Programming &bull; Student: ROSHAN NAMDEO BAGALE (Roll No: 50, Div D)</p>
    </footer>
</body>
</html>
