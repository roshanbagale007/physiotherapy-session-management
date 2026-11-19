<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | PhysioCare Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <!-- Top Navigation -->
    <header class="navbar">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="brand">
            🏥 <span>PhysioCare Management</span>
        </a>
        <div class="user-info">
            <span>Welcome, <strong>${sessionScope.fullName}</strong></span>
            <span class="role-badge role-${sessionScope.userRole}">${sessionScope.userRole}</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <main class="container">
        <!-- Metric Cards -->
        <div class="metrics-grid">
            <div class="metric-card">
                <h3>Registered Patients</h3>
                <div class="value">${patientCount}</div>
            </div>
            <div class="metric-card green">
                <h3>Active Specialists</h3>
                <div class="value">${therapistCount}</div>
            </div>
            <div class="metric-card orange">
                <h3>Total Sessions</h3>
                <div class="value">${sessionCount}</div>
            </div>
            <div class="metric-card purple">
                <h3>Package Revenue</h3>
                <div class="value">₹${totalRevenue}</div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Clinic Operations Overview</h2>
            </div>
            <p>Welcome to the executive administration console. As clinic administrator, you can oversee therapist rosters, review department revenue, and audit clinical attendance records.</p>
            <div style="margin-top: 1.5rem; display: flex; gap: 1rem; flex-wrap: wrap;">
                <a href="${pageContext.request.contextPath}/desk/dashboard" class="btn-primary" style="width: auto; text-decoration: none; padding: 0.6rem 1.2rem;">View Front Desk Schedule</a>
            </div>
        </div>
    </main>

    <footer>
        <p>Physiotherapy Session Management System &bull; 24MCAC302 Advance Java Programming &bull; Student: ROSHAN NAMDEO BAGALE (Roll No: 50, Div D)</p>
    </footer>
</body>
</html>
