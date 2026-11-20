<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Executive Admin Dashboard | PhysioCare Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <!-- Chart.js 4.x CDN -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
    <style>
        .charts-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(360px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }
        .chart-box {
            background: white;
            padding: 1.5rem;
            border-radius: var(--radius);
            box-shadow: var(--shadow);
        }
        .chart-title {
            font-size: 1.05rem;
            font-weight: 700;
            color: #0f172a;
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        .chart-wrapper {
            position: relative;
            height: 280px;
            width: 100%;
        }
    </style>
</head>
<body>
    <!-- Top Navigation -->
    <header class="navbar">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="brand">
            🏥 <span>PhysioCare Executive Administration</span>
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
                <small style="color: var(--text-muted);">Active clinical intake</small>
            </div>
            <div class="metric-card green">
                <h3>Active Specialists</h3>
                <div class="value">${therapistCount}</div>
                <small style="color: var(--text-muted);">Licensed physiotherapists</small>
            </div>
            <div class="metric-card orange">
                <h3>Total Sessions Booked</h3>
                <div class="value">${sessionCount}</div>
                <small style="color: var(--text-muted);">Cumulative scheduled visits</small>
            </div>
            <div class="metric-card purple">
                <h3>Package Collections</h3>
                <div class="value">₹${totalRevenue}</div>
                <small style="color: var(--text-muted);">Prepaid treatment revenue</small>
            </div>
        </div>

        <!-- Interactive Charts Grid (Phase 9) -->
        <div class="charts-grid">
            <!-- Chart 1: Revenue by Package Plan -->
            <div class="chart-box">
                <div class="chart-title">
                    💰 Revenue by Package Protocol
                </div>
                <div class="chart-wrapper">
                    <canvas id="revenueChart"></canvas>
                </div>
            </div>

            <!-- Chart 2: Attendance Velocity -->
            <div class="chart-box">
                <div class="chart-title">
                    ⏱️ Clinical Attendance &amp; Caseload Velocity
                </div>
                <div class="chart-wrapper">
                    <canvas id="velocityChart"></canvas>
                </div>
            </div>
        </div>

        <!-- Management Actions -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Clinic Control Center &amp; Invoicing</h2>
            </div>
            <p>Select a departmental module below to oversee patient schedules, manage packaged billing plans, or print formal tax invoices.</p>
            <div style="margin-top: 1.5rem; display: flex; gap: 1rem; flex-wrap: wrap;">
                <a href="${pageContext.request.contextPath}/desk/invoices" class="btn-primary" style="width: auto; text-decoration: none; padding: 0.65rem 1.3rem; background: #0077b6;">
                    💳 Clinical Package Billing &amp; Invoices
                </a>
                <a href="${pageContext.request.contextPath}/desk/dashboard" class="btn-primary" style="width: auto; text-decoration: none; padding: 0.65rem 1.3rem; background: #059669;">
                    📋 Front Desk Appointments &amp; Attendance
                </a>
            </div>
        </div>
    </main>

    <footer>
        <p>Physiotherapy Session Management System &bull; 24MCAC302 Advance Java Programming &bull; Student: ROSHAN NAMDEO BAGALE (Roll No: 50, Div D | MIT-ADT University)</p>
    </footer>

    <!-- Chart.js Render Script -->
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            // 1. Revenue Doughnut Chart
            const pkgLabels = ${pkgLabelsJson};
            const pkgRevs = ${pkgRevJson};

            const revCtx = document.getElementById('revenueChart').getContext('2d');
            new Chart(revCtx, {
                type: 'doughnut',
                data: {
                    labels: pkgLabels,
                    datasets: [{
                        data: pkgRevs,
                        backgroundColor: [
                            '#0077b6',
                            '#059669',
                            '#d97706',
                            '#8b5cf6',
                            '#0284c7'
                        ],
                        borderWidth: 2,
                        borderColor: '#ffffff'
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'bottom',
                            labels: {
                                boxWidth: 12,
                                font: { size: 11 }
                            }
                        },
                        tooltip: {
                            callbacks: {
                                label: function(c) {
                                    return ' ' + c.label + ': ₹' + c.parsed.toLocaleString('en-IN');
                                }
                            }
                        }
                    }
                }
            });

            // 2. Attendance Velocity Bar Chart
            const statusLabels = ${statusLabelsJson};
            const statusCounts = ${statusCountJson};

            const velCtx = document.getElementById('velocityChart').getContext('2d');
            new Chart(velCtx, {
                type: 'bar',
                data: {
                    labels: statusLabels,
                    datasets: [{
                        label: 'Number of Sessions',
                        data: statusCounts,
                        backgroundColor: [
                            'rgba(16, 185, 129, 0.85)', // ATTENDED - Green
                            'rgba(2, 132, 199, 0.85)',  // SCHEDULED - Blue
                            'rgba(239, 68, 68, 0.85)',  // CANCELLED - Red
                            'rgba(245, 158, 11, 0.85)'  // NO_SHOW - Yellow
                        ],
                        borderColor: [
                            '#10b981',
                            '#0284c7',
                            '#ef4444',
                            '#f59e0b'
                        ],
                        borderWidth: 1.5,
                        borderRadius: 6
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            callbacks: {
                                label: function(c) {
                                    return ' Sessions: ' + c.parsed.y;
                                }
                            }
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: { stepSize: 2 }
                        },
                        x: {
                            grid: { display: false }
                        }
                    }
                }
            });
        });
    </script>
</body>
</html>
