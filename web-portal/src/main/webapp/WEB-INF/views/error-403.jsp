<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>403 Forbidden | PhysioCare Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .error-wrapper {
            min-height: 80vh;
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
            padding: 2rem;
        }
        .error-card {
            background: white;
            padding: 3rem 2.5rem;
            border-radius: 12px;
            box-shadow: var(--shadow);
            max-width: 500px;
        }
        .error-code {
            font-size: 5rem;
            font-weight: 800;
            color: var(--danger);
            line-height: 1;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <div class="error-wrapper">
        <div class="error-card">
            <div class="error-code">403</div>
            <h1 style="font-size:1.5rem; color:var(--text-main); margin-bottom:0.75rem;">Access Denied / Forbidden</h1>
            <p style="color:var(--text-muted); margin-bottom:1.5rem;">
                <c:choose>
                    <c:when test="${not empty forbiddenReason}">
                        ${forbiddenReason}
                    </c:when>
                    <c:otherwise>
                        You do not have the required role-based permissions to access this clinical resource.
                    </c:otherwise>
                </c:choose>
            </p>
            <a href="${pageContext.request.contextPath}/login" class="btn-primary" style="display:inline-block; text-decoration:none; padding:0.75rem 1.5rem;">Return to Safe Portal</a>
        </div>
    </div>
</body>
</html>
