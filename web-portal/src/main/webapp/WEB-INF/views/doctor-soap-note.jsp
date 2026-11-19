<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clinical SOAP Notes | PhysioCare Doctor Hub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .patient-banner {
            background: #e0f2fe;
            border-left: 4px solid var(--primary);
            padding: 1rem 1.25rem;
            border-radius: var(--radius);
            margin-bottom: 1.5rem;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
        }
        .pain-slider-group {
            display: flex;
            align-items: center;
            gap: 1rem;
        }
        .pain-badge {
            font-size: 1.2rem;
            font-weight: 700;
            padding: 0.2rem 0.8rem;
            border-radius: 6px;
            background: var(--primary-light);
            color: var(--primary-dark);
            min-width: 45px;
            text-align: center;
        }
        .checkbox-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 0.6rem;
            margin-top: 0.5rem;
        }
        .checkbox-item {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            font-size: 0.9rem;
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

    <main class="container" style="max-width: 850px;">
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">📝 Record Digital SOAP Progress Notes</h2>
                <a href="${pageContext.request.contextPath}/doctor/dashboard" style="color:var(--primary); text-decoration:none; font-size:0.9rem;">&larr; Back to Queue</a>
            </div>

            <!-- Patient Information Banner -->
            <div class="patient-banner">
                <div>
                    <span style="font-size:0.8rem; color:var(--text-muted); text-transform:uppercase;">Patient Name</span>
                    <p><strong>${sessionData.patientName}</strong></p>
                </div>
                <div>
                    <span style="font-size:0.8rem; color:var(--text-muted); text-transform:uppercase;">Diagnosis</span>
                    <p><strong>${sessionData.diagnosis}</strong></p>
                </div>
                <div>
                    <span style="font-size:0.8rem; color:var(--text-muted); text-transform:uppercase;">Rehab Goal</span>
                    <p style="font-size:0.9rem;">${sessionData.treatmentGoal}</p>
                </div>
                <div>
                    <span style="font-size:0.8rem; color:var(--text-muted); text-transform:uppercase;">Appointment Slot</span>
                    <p><strong>${sessionData.sessionDate} &bull; ${sessionData.startTime}</strong></p>
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/doctor/soap-note" method="post">
                <input type="hidden" name="sessionId" value="${sessionData.id}">

                <!-- 1. Subjective (S) -->
                <div class="form-group">
                    <label for="subjective">1. Subjective (S) &mdash; Patient Reported Symptoms &amp; Response to Therapy *</label>
                    <textarea id="subjective" name="subjective" rows="3" required placeholder="e.g. Patient reports lower back aching reduced from constant to intermittent. Morning stiffness down to 15 mins."></textarea>
                </div>

                <!-- 2. Objective (O) Metrics -->
                <div style="background:#f8fafc; padding:1.25rem; border-radius:var(--radius); border:1px solid var(--border); margin-bottom:1.25rem;">
                    <h3 style="font-size:1rem; color:var(--primary-dark); margin-bottom:1rem;">2. Objective (O) &mdash; Physical Measurements</h3>
                    
                    <!-- Pain Scale Before & After -->
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem; margin-bottom: 1.25rem;">
                        <div>
                            <label>Pain Score BEFORE Session (VAS 0–10):</label>
                            <div class="pain-slider-group">
                                <input type="range" name="painBefore" min="0" max="10" value="7" style="flex:1;" oninput="document.getElementById('valBefore').innerText = this.value">
                                <span class="pain-badge" id="valBefore">7</span>
                            </div>
                        </div>
                        <div>
                            <label>Pain Score AFTER Session (VAS 0–10):</label>
                            <div class="pain-slider-group">
                                <input type="range" name="painAfter" min="0" max="10" value="3" style="flex:1;" oninput="document.getElementById('valAfter').innerText = this.value">
                                <span class="pain-badge" id="valAfter">3</span>
                            </div>
                        </div>
                    </div>

                    <!-- Range of Motion & Muscle Strength -->
                    <div style="display: grid; grid-template-columns: 1.5fr 1fr; gap: 1rem; margin-bottom: 1rem;">
                        <div class="form-group" style="margin-bottom:0;">
                            <label for="romNotes">Range of Motion (ROM) Degrees</label>
                            <input type="text" id="romNotes" name="romNotes" placeholder="e.g. Lumbar Flexion 65 deg; SLR 60 deg Left" required>
                        </div>
                        <div class="form-group" style="margin-bottom:0;">
                            <label for="muscleGrade">Muscle Strength Grade (MMT)</label>
                            <select id="muscleGrade" name="muscleGrade" required>
                                <option value="5/5 MMT">Grade 5/5 (Normal)</option>
                                <option value="4+/5 MMT">Grade 4+/5 (Good+)</option>
                                <option value="4/5 MMT" selected>Grade 4/5 (Good)</option>
                                <option value="3+/5 MMT">Grade 3+/5 (Fair+)</option>
                                <option value="3/5 MMT">Grade 3/5 (Fair)</option>
                                <option value="2/5 MMT">Grade 2/5 (Poor)</option>
                            </select>
                        </div>
                    </div>

                    <!-- Objective Clinical Findings Text -->
                    <div class="form-group" style="margin-bottom:0; margin-top:1rem;">
                        <label for="objective">Objective Clinical Findings &amp; Palpation</label>
                        <textarea id="objective" name="objective" rows="2" placeholder="e.g. Paraspinal muscle guarding resolved; pain-free extension observed." required></textarea>
                    </div>
                </div>

                <!-- Modalities Used -->
                <div class="form-group">
                    <label>Physical Therapy Modalities Administered Today:</label>
                    <div class="checkbox-grid">
                        <label class="checkbox-item"><input type="checkbox" name="modalities" value="IFT 4-pole" checked> IFT (Interferential Therapy)</label>
                        <label class="checkbox-item"><input type="checkbox" name="modalities" value="Ultrasound 1MHz"> Ultrasound Therapy</label>
                        <label class="checkbox-item"><input type="checkbox" name="modalities" value="Mechanical Traction"> Lumbar / Cervical Traction</label>
                        <label class="checkbox-item"><input type="checkbox" name="modalities" value="Cryotherapy"> Cryotherapy (Cold Pack)</label>
                        <label class="checkbox-item"><input type="checkbox" name="modalities" value="Moist Heat Pack"> Moist Heat Pack</label>
                        <label class="checkbox-item"><input type="checkbox" name="modalities" value="Manual Joint Mobilization" checked> Manual Joint Mobilization</label>
                        <label class="checkbox-item"><input type="checkbox" name="modalities" value="Dry Needling"> Dry Needling</label>
                        <label class="checkbox-item"><input type="checkbox" name="modalities" value="Therapeutic Exercise" checked> Therapeutic Core Drills</label>
                    </div>
                </div>

                <!-- 3. Assessment (A) -->
                <div class="form-group">
                    <label for="assessment">3. Assessment (A) &mdash; Recovery Progress &amp; Clinical Evaluation *</label>
                    <textarea id="assessment" name="assessment" rows="2" required placeholder="e.g. Radicular nerve symptoms centralizing favorably. Ready to progress to dynamic core loading."></textarea>
                </div>

                <!-- 4. Plan (P) -->
                <div class="form-group">
                    <label for="planNext">4. Plan (P) &mdash; Next Session Protocol &amp; Home Exercise Guidance *</label>
                    <textarea id="planNext" name="planNext" rows="2" required placeholder="e.g. Advance home bridging to single-leg variation. Maintain daily pelvic flossing."></textarea>
                </div>

                <div style="margin-top: 2rem;">
                    <button type="submit" class="btn-primary" style="padding: 1rem; font-size:1.05rem;">
                        💾 Save Clinical SOAP Record &amp; Complete Session
                    </button>
                </div>
            </form>
        </div>
    </main>
</body>
</html>
