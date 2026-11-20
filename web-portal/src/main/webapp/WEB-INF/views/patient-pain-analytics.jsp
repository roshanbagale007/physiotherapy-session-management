<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Visual Pain Trajectory &amp; Recovery Curves | PhysioCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <!-- Chart.js 4.x CDN -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
    <style>
        .analytics-hero {
            background: linear-gradient(135deg, #023e8a, #0077b6);
            color: white;
            border-radius: var(--radius);
            padding: 1.75rem;
            margin-bottom: 2rem;
            box-shadow: 0 8px 24px rgba(2, 62, 138, 0.2);
        }
        .analytics-hero h1 {
            font-size: 1.6rem;
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }
        .hero-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.25rem;
            margin-top: 1.25rem;
        }
        .hero-item {
            background: rgba(255, 255, 255, 0.12);
            padding: 0.85rem 1.1rem;
            border-radius: 6px;
            border: 1px solid rgba(255, 255, 255, 0.2);
        }
        .hero-item small {
            display: block;
            opacity: 0.8;
            font-size: 0.75rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .hero-item strong {
            font-size: 1.05rem;
        }
        .kpi-row {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 1.25rem;
            margin-bottom: 2rem;
        }
        .kpi-box {
            background: white;
            padding: 1.25rem;
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            border-left: 5px solid var(--primary);
        }
        .kpi-box.green { border-left-color: var(--success); }
        .kpi-box.orange { border-left-color: var(--warning); }
        .kpi-box.purple { border-left-color: #8b5cf6; }
        .kpi-label {
            font-size: 0.82rem;
            color: var(--text-muted);
            text-transform: uppercase;
            font-weight: 600;
        }
        .kpi-val {
            font-size: 2rem;
            font-weight: 800;
            color: var(--text-main);
            margin: 0.25rem 0;
        }
        .kpi-sub {
            font-size: 0.85rem;
            color: var(--text-muted);
        }
        .chart-container {
            position: relative;
            height: 380px;
            width: 100%;
            margin-top: 1rem;
        }
        .delta-badge {
            background: #e0f2fe;
            color: #0369a1;
            padding: 0.2rem 0.5rem;
            border-radius: 4px;
            font-weight: 700;
            font-size: 0.85rem;
        }
        .status-pill {
            padding: 0.4rem 0.9rem;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
            display: inline-block;
        }
        .status-pill.success { background: #dcfce7; color: #15803d; border: 1px solid #86efac; }
        .status-pill.warning { background: #fef3c7; color: #b45309; border: 1px solid #fde68a; }
        .status-pill.primary { background: #e0f2fe; color: #0369a1; border: 1px solid #bae6fd; }
    </style>
</head>
<body>
    <header class="navbar">
        <a href="${pageContext.request.contextPath}/patient/dashboard" class="brand">
            📊 <span>PhysioCare Clinical Intelligence</span>
        </a>
        <div class="user-info">
            <span>Logged in: <strong>${sessionScope.fullName}</strong></span>
            <span class="role-badge role-${sessionScope.userRole}">${sessionScope.userRole}</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <main class="container">
        <!-- Patient Banner Header -->
        <div class="analytics-hero">
            <h1>
                📈 Patient Visual Recovery Trajectory &amp; VAS Pain Progression
            </h1>
            <p>Objective clinical progression tracked across consecutive physiotherapy treatments using standardized Visual Analog Scale (VAS 0-10) metrics.</p>

            <div class="hero-grid">
                <div class="hero-item">
                    <small>Patient Name</small>
                    <strong>${patientInfo.fullName}</strong> (${patientInfo.gender}, Blood: ${patientInfo.bloodGroup})
                </div>
                <div class="hero-item">
                    <small>Primary Diagnosis</small>
                    <strong>${planInfo.diagnosis}</strong>
                </div>
                <div class="hero-item">
                    <small>Supervising Physiotherapist</small>
                    <strong>${planInfo.therapistName}</strong> (${planInfo.specialization})
                </div>
                <div class="hero-item">
                    <small>Treatment Milestones</small>
                    <strong>${planInfo.completedSessions} / ${planInfo.targetSessions} Sessions</strong> (${planInfo.status})
                </div>
            </div>
        </div>

        <!-- 4 Recovery Metric Cards -->
        <div class="kpi-row">
            <div class="kpi-box orange">
                <div class="kpi-label">Baseline Pain (Session 1)</div>
                <div class="kpi-val">${baselinePain} <span style="font-size: 1.1rem; color: #94a3b8;">/ 10</span></div>
                <div class="kpi-sub">Severe clinical impairment recorded on intake</div>
            </div>
            <div class="kpi-box green">
                <div class="kpi-label">Latest Evaluation VAS</div>
                <div class="kpi-val">${latestPain} <span style="font-size: 1.1rem; color: #94a3b8;">/ 10</span></div>
                <div class="kpi-sub">Post-treatment score at most recent visit</div>
            </div>
            <div class="kpi-box">
                <div class="kpi-label">Overall Pain Reduction</div>
                <div class="kpi-val" style="color: #0284c7;">-${totalRelief} pts</div>
                <div class="kpi-sub"><strong style="color: #16a34a;">${reliefPct}% cumulative relief</strong> achieved</div>
            </div>
            <div class="kpi-box purple">
                <div class="kpi-label">Recovery Assessment</div>
                <div style="margin-top: 0.6rem;">
                    <span class="status-pill ${statusColor}">${recoveryStatus}</span>
                </div>
                <div class="kpi-sub" style="margin-top: 0.5rem;">Based on ${trajectoryList.size()} clinical SOAP evaluations</div>
            </div>
        </div>

        <!-- Interactive Chart.js Card -->
        <div class="card" style="margin-bottom: 2rem;">
            <div class="card-header" style="display: flex; justify-content: space-between; align-items: center;">
                <h2 class="card-title">📉 VAS Pain Score Trajectory Curve (Pre-Session vs Post-Session)</h2>
                <span style="font-size: 0.85rem; color: var(--text-muted); font-weight: 600;">
                    Visual Analog Scale: 0 (No Pain) to 10 (Maximum Intolerable Pain)
                </span>
            </div>
            <div class="chart-container">
                <canvas id="painChart"></canvas>
            </div>
        </div>

        <!-- Chronological SOAP Note History Table -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">📋 Longitudinal SOAP Evaluation &amp; Treatment Notes</h2>
            </div>
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Visit</th>
                            <th>Date &amp; Time</th>
                            <th>Pre-Tx VAS</th>
                            <th>Post-Tx VAS</th>
                            <th>Relief Delta</th>
                            <th>Strength (MMT)</th>
                            <th>Range of Motion (ROM)</th>
                            <th>Modalities Applied</th>
                            <th>Doctor Assessment</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty trajectoryList}">
                                <c:forEach var="v" items="${trajectoryList}">
                                    <tr>
                                        <td><strong>S#${v.visitNo}</strong></td>
                                        <td>${v.sessionDate} <br><small style="color: #64748b;">${v.startTime}</small></td>
                                        <td>
                                            <span style="display: inline-block; width: 28px; height: 28px; line-height: 28px; text-align: center; border-radius: 50%; background: #fee2e2; color: #b91c1c; font-weight: 700;">
                                                ${v.painBefore}
                                            </span>
                                        </td>
                                        <td>
                                            <span style="display: inline-block; width: 28px; height: 28px; line-height: 28px; text-align: center; border-radius: 50%; background: #dcfce7; color: #15803d; font-weight: 700;">
                                                ${v.painAfter}
                                            </span>
                                        </td>
                                        <td>
                                            <span class="delta-badge">-${v.painDelta} pts</span>
                                        </td>
                                        <td><strong>${v.muscleGrade}</strong></td>
                                        <td><small>${v.romNotes}</small></td>
                                        <td><small style="color: #475569;">${v.modalities}</small></td>
                                        <td><small style="font-style: italic;">"${v.assessment}"</small></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="9" style="text-align: center; color: var(--text-muted); padding: 2rem;">
                                        No SOAP evaluations recorded for this patient yet.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>

            <div style="margin-top: 1.5rem; display: flex; gap: 1rem;">
                <c:choose>
                    <c:when test="${sessionScope.userRole eq 'PATIENT'}">
                        <a href="${pageContext.request.contextPath}/patient/dashboard" class="btn-primary" style="width: auto; text-decoration: none; padding: 0.6rem 1.2rem;">
                            ⬅ Back to Patient Portal
                        </a>
                    </c:when>
                    <c:when test="${sessionScope.userRole eq 'THERAPIST'}">
                        <a href="${pageContext.request.contextPath}/doctor/dashboard" class="btn-primary" style="width: auto; text-decoration: none; padding: 0.6rem 1.2rem;">
                            ⬅ Back to Doctor Dashboard
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn-primary" style="width: auto; text-decoration: none; padding: 0.6rem 1.2rem;">
                            ⬅ Back to Admin Dashboard
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>

    <footer>
        <p>Physiotherapy Session Management System &bull; 24MCAC302 Advance Java Programming &bull; Student: ROSHAN NAMDEO BAGALE (Roll No: 50, Div D | MIT-ADT University)</p>
    </footer>

    <!-- Chart.js Initialization Script -->
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const labels = ${chartLabelsJson};
            const beforeData = ${chartBeforeJson};
            const afterData = ${chartAfterJson};
            const deltaData = ${chartDeltaJson};

            const ctx = document.getElementById('painChart').getContext('2d');

            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [
                        {
                            label: 'Pre-Treatment Pain (VAS Before)',
                            data: beforeData,
                            borderColor: '#ea580c',
                            backgroundColor: 'rgba(234, 88, 12, 0.1)',
                            borderWidth: 3,
                            pointBackgroundColor: '#ea580c',
                            pointRadius: 6,
                            pointHoverRadius: 9,
                            tension: 0.3,
                            fill: false
                        },
                        {
                            label: 'Post-Treatment Pain (VAS After)',
                            data: afterData,
                            borderColor: '#059669',
                            backgroundColor: 'rgba(5, 150, 105, 0.15)',
                            borderWidth: 3,
                            pointBackgroundColor: '#059669',
                            pointRadius: 6,
                            pointHoverRadius: 9,
                            tension: 0.3,
                            fill: true
                        },
                        {
                            label: 'Single-Session Pain Relief (Delta)',
                            data: deltaData,
                            type: 'bar',
                            backgroundColor: 'rgba(2, 132, 199, 0.45)',
                            borderColor: '#0284c7',
                            borderWidth: 1.5,
                            borderRadius: 6,
                            barPercentage: 0.4
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'top',
                            labels: {
                                font: { size: 13, weight: '600' },
                                boxWidth: 16
                            }
                        },
                        tooltip: {
                            backgroundColor: 'rgba(15, 23, 42, 0.95)',
                            titleFont: { size: 14, weight: '700' },
                            bodyFont: { size: 13 },
                            padding: 12,
                            cornerRadius: 8,
                            callbacks: {
                                label: function(context) {
                                    if (context.dataset.type === 'bar') {
                                        return ' Relief Delta: -' + context.parsed.y + ' VAS points';
                                    }
                                    return ' ' + context.dataset.label + ': ' + context.parsed.y + ' / 10';
                                }
                            }
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            max: 10,
                            title: {
                                display: true,
                                text: 'Visual Analog Scale (VAS 0 - 10)',
                                font: { size: 13, weight: '700' },
                                color: '#475569'
                            },
                            ticks: {
                                stepSize: 1,
                                callback: function(value) {
                                    if (value === 0) return '0 (No Pain)';
                                    if (value === 5) return '5 (Moderate)';
                                    if (value === 10) return '10 (Worst)';
                                    return value;
                                }
                            },
                            grid: {
                                color: 'rgba(226, 232, 240, 0.8)'
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: 'Rehabilitation Visit Chronology',
                                font: { size: 13, weight: '700' },
                                color: '#475569'
                            },
                            grid: {
                                display: false
                            }
                        }
                    }
                }
            });
        });
    </script>
</body>
</html>
