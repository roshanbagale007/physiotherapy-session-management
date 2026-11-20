<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Front Desk Dashboard | PhysioCare Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .action-btn-sm {
            padding: 0.3rem 0.6rem;
            border-radius: 4px;
            font-size: 0.78rem;
            font-weight: 600;
            border: none;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        .btn-attend { background: #dcfce7; color: #15803d; border: 1px solid #bbf7d0; }
        .btn-cancel { background: #fee2e2; color: #b91c1c; border: 1px solid #fecaca; }
        .btn-attend:hover { background: #bbf7d0; }
        .btn-cancel:hover { background: #fecaca; }
    </style>
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
        <!-- Success / Error Alert Messages from PRG Redirect -->
        <c:if test="${not empty param.success}">
            <div class="alert alert-success">✅ ${param.success}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">⚠️ ${param.error}</div>
        </c:if>

        <div class="card">
            <div class="card-header" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.75rem;">
                <h2 class="card-title">Front Desk Session Schedule &amp; Attendance</h2>
                <div style="display: flex; gap: 0.75rem;">
                    <a href="${pageContext.request.contextPath}/desk/invoices" class="btn-primary" style="background: #0284c7; width: auto; text-decoration: none; padding: 0.55rem 1.1rem; font-size: 0.9rem;">
                        💳 Package Billing &amp; Invoices
                    </a>
                    <a href="${pageContext.request.contextPath}/desk/book-session" class="btn-primary" style="width: auto; text-decoration: none; padding: 0.55rem 1.1rem; font-size: 0.9rem;">
                        ➕ Book New Appointment
                    </a>
                </div>
            </div>
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Time</th>
                            <th>Patient Name</th>
                            <th>Assigned Specialist</th>
                            <th>Status</th>
                            <th>Quick Attendance Action</th>
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
                                        <td>
                                            <c:if test="${s.status eq 'SCHEDULED'}">
                                                <div style="display: flex; gap: 0.4rem;">
                                                    <!-- Mark Present / Attended Form -->
                                                    <form action="${pageContext.request.contextPath}/desk/mark-attendance" method="post" style="display:inline;">
                                                        <input type="hidden" name="sessionId" value="${s.id}">
                                                        <input type="hidden" name="status" value="ATTENDED">
                                                        <button type="submit" class="action-btn-sm btn-attend" title="Check-in patient">✓ Mark Present</button>
                                                    </form>

                                                    <!-- Cancel Form -->
                                                    <form action="${pageContext.request.contextPath}/desk/mark-attendance" method="post" style="display:inline;">
                                                        <input type="hidden" name="sessionId" value="${s.id}">
                                                        <input type="hidden" name="status" value="CANCELLED">
                                                        <input type="hidden" name="reason" value="Cancelled by patient request">
                                                        <button type="submit" class="action-btn-sm btn-cancel" onclick="return confirm('Cancel this appointment?');">✕ Cancel</button>
                                                    </form>
                                                </div>
                                            </c:if>
                                            <c:if test="${s.status eq 'ATTENDED'}">
                                                <span style="color:var(--success); font-size:0.85rem; font-weight:600;">✓ Checked-In</span>
                                            </c:if>
                                            <c:if test="${s.status eq 'CANCELLED'}">
                                                <span style="color:var(--text-muted); font-size:0.85rem;">Cancelled</span>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" style="text-align:center; color:var(--text-muted);">No clinic sessions found.</td>
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
