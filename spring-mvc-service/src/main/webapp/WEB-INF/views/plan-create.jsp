<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Treatment Plan | Spring 6 MVC & JSR-380</title>
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
            <a href="${pageContext.request.contextPath}/plans/new" class="active">➕ New Plan</a>
            <a href="${pageContext.request.contextPath}/plans/report">📊 Clinic Report</a>
            <a href="http://localhost:8025/web-portal/" target="_blank">🌐 Web Portal</a>
        </nav>
    </header>

    <div class="container" style="max-width: 860px;">
        <!-- Header Row -->
        <div class="header-row">
            <div>
                <h1 class="page-title">Initiate New Treatment Plan</h1>
                <p class="page-subtitle">Unit 3.2.4: Spring Form Binding with JSR-380 Validation (@Valid &amp; BindingResult)</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/plans" class="btn btn-secondary">⬅ Back to List</a>
            </div>
        </div>

        <!-- Validation Error Alert Banner -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                ⚠️ <span>${errorMessage}</span>
            </div>
        </c:if>

        <div class="card">
            <div class="card-title">
                <span>Clinical Rehabilitation Protocol Details</span>
                <span style="font-size: 0.8rem; font-weight: normal; color: var(--text-muted);">* All marked fields are mandatory</span>
            </div>

            <!-- Spring Form Binding to TreatmentPlanForm DTO -->
            <form:form method="POST" action="${pageContext.request.contextPath}/plans" modelAttribute="planForm">
                
                <!-- Patient & Therapist Dropdowns Row -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="patientId">Select Patient *</label>
                        <form:select path="patientId" id="patientId" cssClass="form-control">
                            <form:option value="" label="-- Choose Patient --"/>
                            <c:forEach var="p" items="${patients}">
                                <form:option value="${p.id}" label="${p.user.fullName} (${p.gender}, Blood: ${p.bloodGroup})"/>
                            </c:forEach>
                        </form:select>
                        <form:errors path="patientId" cssClass="form-error"/>
                    </div>

                    <div class="form-group">
                        <label for="therapistId">Assign Lead Physiotherapist *</label>
                        <form:select path="therapistId" id="therapistId" cssClass="form-control">
                            <form:option value="" label="-- Choose Physiotherapist --"/>
                            <c:forEach var="t" items="${therapists}">
                                <form:option value="${t.id}" label="${t.user.fullName} [${t.specialization} - ${t.experienceYears} yrs exp]"/>
                            </c:forEach>
                        </form:select>
                        <form:errors path="therapistId" cssClass="form-error"/>
                    </div>
                </div>

                <!-- Diagnosis Field -->
                <div class="form-group">
                    <label for="diagnosis">Clinical Diagnosis * (e.g. Lumbar Disc Herniation, ACL Post-Op)</label>
                    <form:input path="diagnosis" id="diagnosis" cssClass="form-control" placeholder="Enter formal medical diagnosis (5-200 characters)"/>
                    <form:errors path="diagnosis" cssClass="form-error"/>
                </div>

                <!-- Treatment Goal Textarea -->
                <div class="form-group">
                    <label for="treatmentGoal">Rehabilitation Goal &amp; Milestones *</label>
                    <form:textarea path="treatmentGoal" id="treatmentGoal" rows="4" cssClass="form-control" 
                                   placeholder="Describe functional targets (e.g. Restore knee flexion to 120 deg, eliminate sciatic radiculopathy)..."/>
                    <form:errors path="treatmentGoal" cssClass="form-error"/>
                </div>

                <!-- Dates & Sessions Row -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="startDate">Rehabilitation Start Date *</label>
                        <form:input type="date" path="startDate" id="startDate" cssClass="form-control"/>
                        <form:errors path="startDate" cssClass="form-error"/>
                    </div>

                    <div class="form-group">
                        <label for="estimatedEndDate">Estimated Completion Date (Optional)</label>
                        <form:input type="date" path="estimatedEndDate" id="estimatedEndDate" cssClass="form-control"/>
                        <form:errors path="estimatedEndDate" cssClass="form-error"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="targetSessions">Target Planned Sessions * (Min 1, Max 60)</label>
                    <form:input type="number" path="targetSessions" id="targetSessions" min="1" max="60" cssClass="form-control" style="max-width: 200px;"/>
                    <small style="color: var(--text-muted); display: block; margin-top: 0.2rem;">
                        Standard clinical package: 10 to 20 sessions depending on pathology severity.
                    </small>
                    <form:errors path="targetSessions" cssClass="form-error"/>
                </div>

                <!-- Action Buttons -->
                <div style="display: flex; gap: 1rem; margin-top: 2rem; border-top: 1px solid var(--border); padding-top: 1.5rem;">
                    <button type="submit" class="btn btn-primary" style="padding: 0.75rem 2rem; font-size: 1rem;">
                        💾 Save &amp; Persist Treatment Plan
                    </button>
                    <a href="${pageContext.request.contextPath}/plans" class="btn btn-secondary" style="padding: 0.75rem 1.5rem;">
                        Cancel
                    </a>
                </div>
            </form:form>
        </div>
    </div>

    <footer>
        Physiotherapy Session Management &bull; MCA Sem III (24MCAC302) &bull; Spring 6 MVC + JPA 3.1 ORM
    </footer>
</body>
</html>
