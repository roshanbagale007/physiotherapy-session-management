<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | Physiotherapy Session Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="login-wrapper">
        <div class="login-card">
            <div class="login-header">
                <h1>🏥 PhysioCare Portal</h1>
                <p>Clinical Rehabilitation &amp; Session Management System</p>
            </div>

            <!-- Error message if authentication failed -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <c:if test="${not empty param.error}">
                <div class="alert alert-danger">${param.error}</div>
            </c:if>

            <c:if test="${not empty param.success}">
                <div class="alert alert-success">${param.success}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" placeholder="e.g. dr.sameer or admin" required autofocus>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="••••••••" required>
                </div>

                <button type="submit" class="btn-primary">Sign In</button>
            </form>

            <!-- Quick Demo Credentials Box for Lab Evaluation & Testing -->
            <div class="credentials-box">
                <h4>🔑 Demo Test Accounts (4 Roles):</h4>
                <table>
                    <tr><td><strong>Admin:</strong></td><td>admin</td><td><code>Admin@123</code></td></tr>
                    <tr><td><strong>Doctor:</strong></td><td>dr.sameer</td><td><code>Doctor@123</code></td></tr>
                    <tr><td><strong>Reception:</strong></td><td>receptionist</td><td><code>Staff@123</code></td></tr>
                    <tr><td><strong>Patient:</strong></td><td>rahul.v</td><td><code>Patient@123</code></td></tr>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
