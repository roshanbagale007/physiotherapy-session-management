<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient Portal | PhysioCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .progress-bar-container {
            width: 100%;
            background: #e2e8f0;
            border-radius: 12px;
            height: 18px;
            overflow: hidden;
            margin: 0.5rem 0 1rem 0;
        }
        .progress-bar {
            height: 100%;
            background: linear-gradient(90deg, var(--primary), var(--success));
            border-radius: 12px;
            transition: width 0.5s ease;
        }
        .btn-check-done {
            background: #dcfce7;
            color: #15803d;
            border: 1px solid #86efac;
            padding: 0.3rem 0.7rem;
            border-radius: 4px;
            font-size: 0.8rem;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s;
        }
        .btn-check-done:hover {
            background: #bbf7d0;
        }
    </style>
</head>
<body>
    <header class="navbar">
        <a href="${pageContext.request.contextPath}/patient/dashboard" class="brand">
            👤 <span>PhysioCare Patient Portal</span>
        </a>
        <div class="user-info">
            <span>Welcome, <strong>${sessionScope.fullName}</strong></span>
            <span class="role-badge role-${sessionScope.userRole}">PATIENT</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <main class="container">
        <!-- PRG Success Alert -->
        <c:if test="${not empty param.success}">
            <div class="alert alert-success">🎉 ${param.success}</div>
        </c:if>

        <!-- Recovery Plan Overview Card -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">My Rehabilitation Journey</h2>
            </div>
            <p><strong>Primary Diagnosis:</strong> ${diagnosis}</p>
            <p style="margin-top: 0.5rem;">
                <strong>Session Milestones:</strong> ${completedSessions} of ${targetSessions} sessions completed
            </p>
            <div class="progress-bar-container">
                <div class="progress-bar" style="width: ${targetSessions > 0 ? (completedSessions * 100 / targetSessions) : 0}%;"></div>
            </div>
        </div>

        <!-- Appointments Card -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">My Appointments</h2>
            </div>
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Time</th>
                            <th>Therapist</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty mySessions}">
                                <c:forEach var="s" items="${mySessions}">
                                    <tr>
                                        <td><strong>${s.sessionDate}</strong></td>
                                        <td>${s.startTime} - ${s.endTime}</td>
                                        <td>${s.therapistName}</td>
                                        <td>
                                            <span class="status-tag status-${s.status}">${s.status}</span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="4" style="text-align:center; color:var(--text-muted);">No appointments booked.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Prescribed Home Exercises Card with Interactive Adherence -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Prescribed Home Exercises (HEP)</h2>
            </div>
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Exercise Name</th>
                            <th>Target Muscle</th>
                            <th>Prescription</th>
                            <th>Clinician Instructions</th>
                            <th>Daily Log</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty myExercises}">
                                <c:forEach var="e" items="${myExercises}">
                                    <tr>
                                        <td><strong>${e.name}</strong></td>
                                        <td>${e.muscle}</td>
                                        <td><strong>${e.sets} sets &times; ${e.reps} reps</strong></td>
                                        <td style="color:var(--text-muted); font-size:0.9rem;">${e.notes}</td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/patient/log-exercise" method="post" style="display:inline;">
                                                <input type="hidden" name="exerciseName" value="${e.name}">
                                                <button type="submit" class="btn-check-done">✓ Mark Done</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="5" style="text-align:center; color:var(--text-muted);">No home exercises assigned currently.</td>
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
