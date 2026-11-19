<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Session | PhysioCare Front Desk</title>
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

    <main class="container" style="max-width: 700px;">
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">📅 Schedule New Therapy Appointment</h2>
                <a href="${pageContext.request.contextPath}/desk/dashboard" style="color:var(--primary); text-decoration:none; font-size:0.9rem;">&larr; Back to Schedule</a>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/desk/book-session" method="post">
                <!-- Patient & Treatment Plan Selection -->
                <div class="form-group">
                    <label for="planId">Select Patient &amp; Treatment Plan *</label>
                    <select id="planId" name="planId" required onchange="updatePatientInfo(this)">
                        <option value="">-- Choose Patient / Active Plan --</option>
                        <c:forEach var="p" items="${plansList}">
                            <option value="${p.id}" data-patient-id="${p.patientId}" data-therapist-id="${p.therapistId}">
                                ${p.patientName} &bull; ${p.diagnosis}
                            </option>
                        </c:forEach>
                    </select>
                    <input type="hidden" id="patientId" name="patientId" value="">
                </div>

                <!-- Assigned Specialist -->
                <div class="form-group">
                    <label for="therapistId">Assign Specialist (Physiotherapist) *</label>
                    <select id="therapistId" name="therapistId" required>
                        <option value="">-- Choose Therapist --</option>
                        <c:forEach var="t" items="${therapistsList}">
                            <option value="${t.id}">
                                ${t.name} (${t.specialization} &bull; ₹${t.fee})
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Date & Slot Time -->
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                    <div class="form-group">
                        <label for="sessionDate">Session Date *</label>
                        <input type="date" id="sessionDate" name="sessionDate" required>
                    </div>
                    <div class="form-group">
                        <label for="startTime">Time Slot (45 Mins) *</label>
                        <select id="startTime" name="startTime" required>
                            <option value="">-- Choose Slot --</option>
                            <option value="09:00:00">09:00 AM - 09:45 AM</option>
                            <option value="10:00:00">10:00 AM - 10:45 AM</option>
                            <option value="11:00:00">11:00 AM - 11:45 AM</option>
                            <option value="12:00:00">12:00 PM - 12:45 PM</option>
                            <option value="14:00:00">02:00 PM - 02:45 PM</option>
                            <option value="15:00:00">03:00 PM - 03:45 PM</option>
                            <option value="16:00:00">04:00 PM - 04:45 PM</option>
                            <option value="17:00:00">05:00 PM - 05:45 PM</option>
                        </select>
                    </div>
                </div>

                <!-- Package Linkage (Optional) -->
                <div class="form-group">
                    <label for="packageId">Link Prepaid Package (Deduct 1 credit)</label>
                    <select id="packageId" name="packageId">
                        <option value="0">-- None (Pay-per-Session) --</option>
                        <c:forEach var="pkg" items="${packagesList}">
                            <option value="${pkg.id}">
                                ${pkg.name} (${pkg.remaining} sessions remaining)
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div style="margin-top: 1.5rem;">
                    <button type="submit" class="btn-primary">Confirm &amp; Book Appointment</button>
                </div>
            </form>
        </div>
    </main>

    <script>
        // Auto-set minimum date to today
        document.getElementById('sessionDate').min = new Date().toISOString().split('T')[0];

        function updatePatientInfo(selectElem) {
            const selectedOpt = selectElem.options[selectElem.selectedIndex];
            const patientId = selectedOpt.getAttribute('data-patient-id');
            const therapistId = selectedOpt.getAttribute('data-therapist-id');

            document.getElementById('patientId').value = patientId || '';
            if (therapistId) {
                document.getElementById('therapistId').value = therapistId;
            }
        }
    </script>
</body>
</html>
