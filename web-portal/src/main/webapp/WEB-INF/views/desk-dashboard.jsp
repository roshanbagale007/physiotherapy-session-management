<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Front Desk Dashboard | PhysioCare Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header class="navbar">
        <a href="${pageContext.request.contextPath}/desk/dashboard" class="brand">
            📋 <span>PhysioCare Front Desk</span>
        </a>
        <div class="user-info">
            <span>Staff: <strong>${sessionScope.fullName}</strong></span>
            <span class="role-badge role-${sessionScope.userRole}">RECEPTIONIST</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <main class="container">
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Front Desk Session Schedule &amp; Attendance</h2>
            </div>
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Time</th>
                            <th>Patient Name</th>
                            <th>Assigned Specialist</th>
                            <th>Attendance Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty todaySessions}">
                                <c:forEach var="s" items="${todaySessions}">
                                    <tr>
                                        <td><strong>${s.sessionDate}</strong></td>
                                        <td>${s.startTime} - ${s.endTime}</td>
                                        <td><strong>${s.patientName}</strong></td>
                                        <td>${s.therapistName}</td>
                                        <td>
                                            <span class="status-tag status-${s.status}">${s.status}</span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="5" style="text-align:center; color:var(--text-muted);">No clinic sessions found.</td>
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
