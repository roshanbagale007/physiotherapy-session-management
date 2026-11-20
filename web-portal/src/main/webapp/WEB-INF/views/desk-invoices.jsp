<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Package Billing &amp; Invoices | PhysioCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .invoice-kpi-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(230px, 1fr));
            gap: 1.25rem;
            margin-bottom: 2rem;
        }
        .invoice-kpi-card {
            background: white;
            padding: 1.25rem;
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            border-left: 5px solid #0077b6;
        }
        .invoice-kpi-card.green { border-left-color: #10b981; }
        .invoice-kpi-card.purple { border-left-color: #8b5cf6; }
        .invoice-kpi-val {
            font-size: 2rem;
            font-weight: 800;
            color: #0f172a;
            margin: 0.25rem 0;
        }
        .badge-credits {
            display: inline-block;
            padding: 0.25rem 0.65rem;
            border-radius: 12px;
            font-size: 0.8rem;
            font-weight: 700;
        }
        .badge-credits.active { background: #dcfce7; color: #15803d; border: 1px solid #86efac; }
        .badge-credits.exhausted { background: #fee2e2; color: #b91c1c; border: 1px solid #fca5a5; }
        .btn-print {
            background: #f1f5f9;
            color: #0f172a;
            border: 1px solid #cbd5e1;
            padding: 0.35rem 0.75rem;
            border-radius: 4px;
            font-size: 0.82rem;
            font-weight: 600;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 0.35rem;
            transition: all 0.2s;
        }
        .btn-print:hover {
            background: #e2e8f0;
            border-color: #94a3b8;
        }
    </style>
</head>
<body>
    <header class="navbar">
        <a href="${pageContext.request.contextPath}/desk/dashboard" class="brand">
            💳 <span>PhysioCare Billing &amp; Accounts</span>
        </a>
        <div class="user-info">
            <span>Staff: <strong>${sessionScope.fullName}</strong></span>
            <span class="role-badge role-${sessionScope.userRole}">${sessionScope.userRole}</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <main class="container">
        <!-- PRG Alerts -->
        <c:if test="${not empty param.success}">
            <div class="alert alert-success">✅ ${param.success}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">⚠️ ${param.error}</div>
        </c:if>

        <!-- KPI Metrics -->
        <div class="invoice-kpi-grid">
            <div class="invoice-kpi-card green">
                <span style="font-size: 0.82rem; color: #64748b; font-weight: 600; text-transform: uppercase;">Total Package Collections</span>
                <div class="invoice-kpi-val" style="color: #10b981;">₹<c:out value="${totalRevenue}"/></div>
                <small style="color: #64748b;">Cumulative revenue across all packages</small>
            </div>
            <div class="invoice-kpi-card">
                <span style="font-size: 0.82rem; color: #64748b; font-weight: 600; text-transform: uppercase;">Active Subscribed Packages</span>
                <div class="invoice-kpi-val" style="color: #0284c7;"><c:out value="${activePackagesCount}"/></div>
                <small style="color: #64748b;">Patients with remaining session balance</small>
            </div>
            <div class="invoice-kpi-card purple">
                <span style="font-size: 0.82rem; color: #64748b; font-weight: 600; text-transform: uppercase;">Prepaid Session Pool</span>
                <div class="invoice-kpi-val" style="color: #8b5cf6;"><c:out value="${totalRemainingSessions}"/> <span style="font-size: 1rem; color: #64748b;">Sessions</span></div>
                <small style="color: #64748b;">Available to be scheduled &amp; completed</small>
            </div>
        </div>

        <!-- Issue New Package Invoice Form -->
        <div class="card" style="margin-bottom: 2rem;">
            <div class="card-header">
                <h2 class="card-title">➕ Issue New Clinical Package &amp; Tax Invoice</h2>
            </div>
            <form action="${pageContext.request.contextPath}/desk/invoices" method="POST" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 1rem; margin-top: 1rem;">
                
                <div>
                    <label style="display: block; font-size: 0.85rem; font-weight: 600; margin-bottom: 0.35rem;">Select Patient *</label>
                    <select name="patientId" required style="width: 100%; padding: 0.6rem; border: 1px solid #cbd5e1; border-radius: 6px;">
                        <option value="">-- Choose Patient --</option>
                        <c:forEach var="p" items="${patientsList}">
                            <option value="${p.id}">${p.fullName} (${p.phone})</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label style="display: block; font-size: 0.85rem; font-weight: 600; margin-bottom: 0.35rem;">Package Plan *</label>
                    <select name="packageName" id="pkgSelect" onchange="autoFillPackage()" required style="width: 100%; padding: 0.6rem; border: 1px solid #cbd5e1; border-radius: 6px;">
                        <option value="Acute Pain Relief Protocol (5 Sessions)" data-sessions="5" data-amount="2500">Acute Pain Relief (5 Sessions) — ₹2,500</option>
                        <option value="Spine & Posture Rehabilitation (10 Sessions)" data-sessions="10" data-amount="5000" selected>Spine &amp; Posture Rehab (10 Sessions) — ₹5,000</option>
                        <option value="Sports Injury Return-to-Play (15 Sessions)" data-sessions="15" data-amount="7500">Sports Injury Return-to-Play (15 Sessions) — ₹7,500</option>
                        <option value="Post-Surgical Joint Mobilization (20 Sessions)" data-sessions="20" data-amount="9500">Post-Surgical Joint Mobilization (20 Sessions) — ₹9,500</option>
                    </select>
                </div>

                <div>
                    <label style="display: block; font-size: 0.85rem; font-weight: 600; margin-bottom: 0.35rem;">Total Sessions *</label>
                    <input type="number" name="totalSessions" id="pkgSessions" value="10" min="1" required style="width: 100%; padding: 0.6rem; border: 1px solid #cbd5e1; border-radius: 6px;">
                </div>

                <div>
                    <label style="display: block; font-size: 0.85rem; font-weight: 600; margin-bottom: 0.35rem;">Gross Fee (₹) *</label>
                    <input type="number" step="0.01" name="totalAmount" id="pkgAmount" value="5000.00" required style="width: 100%; padding: 0.6rem; border: 1px solid #cbd5e1; border-radius: 6px;">
                </div>

                <div>
                    <label style="display: block; font-size: 0.85rem; font-weight: 600; margin-bottom: 0.35rem;">Discount Concession (₹)</label>
                    <input type="number" step="0.01" name="discountAmount" value="0.00" min="0" style="width: 100%; padding: 0.6rem; border: 1px solid #cbd5e1; border-radius: 6px;">
                </div>

                <div>
                    <label style="display: block; font-size: 0.85rem; font-weight: 600; margin-bottom: 0.35rem;">Payment Method *</label>
                    <select name="paymentMethod" required style="width: 100%; padding: 0.6rem; border: 1px solid #cbd5e1; border-radius: 6px;">
                        <option value="UPI" selected>UPI (Google Pay / PhonePe / Paytm)</option>
                        <option value="CARD">Debit / Credit Card</option>
                        <option value="CASH">Cash Counter</option>
                        <option value="NET_BANKING">Net Banking / NEFT</option>
                    </select>
                </div>

                <div style="grid-column: 1 / -1; display: flex; justify-content: flex-end; margin-top: 0.5rem;">
                    <button type="submit" class="btn-primary" style="width: auto; padding: 0.65rem 1.5rem; font-size: 0.95rem;">
                        🧾 Generate Tax Invoice &amp; Allocate Credits
                    </button>
                </div>
            </form>
        </div>

        <!-- Invoices Register Table -->
        <div class="card">
            <div class="card-header" style="display: flex; justify-content: space-between; align-items: center;">
                <h2 class="card-title">📜 Clinical Invoices &amp; Package Register</h2>
                <a href="${pageContext.request.contextPath}/desk/dashboard" class="btn-primary" style="background:#64748b; width:auto; text-decoration:none; padding:0.45rem 0.9rem; font-size:0.85rem;">
                    ⬅ Front Desk Queue
                </a>
            </div>

            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>Invoice #</th>
                            <th>Date</th>
                            <th>Patient Name</th>
                            <th>Package Name</th>
                            <th>Session Balance</th>
                            <th>Net Paid</th>
                            <th>Mode</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty invoicesList}">
                                <c:forEach var="inv" items="${invoicesList}">
                                    <tr>
                                        <td><strong>INV-2026-${inv.id}</strong></td>
                                        <td><small>${inv.createdAt}</small></td>
                                        <td>
                                            <strong>${inv.patientName}</strong><br>
                                            <small style="color: #64748b;">${inv.patientPhone}</small>
                                        </td>
                                        <td>${inv.packageName}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${inv.remainingSessions > 0}">
                                                    <span class="badge-credits active">
                                                        ${inv.remainingSessions} / ${inv.totalSessions} Left
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge-credits exhausted">
                                                        Completed (0/${inv.totalSessions})
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><strong style="color: #0f172a;">₹${inv.netAmount}</strong></td>
                                        <td>
                                            <span style="font-size: 0.8rem; font-weight: 600; color: #0284c7;">
                                                ${inv.paymentStatus} (${inv.paymentMethod})
                                            </span>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/desk/invoice-print?id=${inv.id}" target="_blank" class="btn-print">
                                                🖨️ Print Tax Invoice
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="8" style="text-align: center; color: var(--text-muted); padding: 2rem;">
                                        No invoices generated yet.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <footer>
        <p>Physiotherapy Session Management System &bull; 24MCAC302 Advance Java Programming &bull; Student: ROSHAN NAMDEO BAGALE (Roll No: 50, Div D | MIT-ADT University)</p>
    </footer>

    <script>
        function autoFillPackage() {
            const select = document.getElementById('pkgSelect');
            const selectedOpt = select.options[select.selectedIndex];
            const sessions = selectedOpt.getAttribute('data-sessions');
            const amount = selectedOpt.getAttribute('data-amount');
            if (sessions) document.getElementById('pkgSessions').value = sessions;
            if (amount) document.getElementById('pkgAmount').value = parseFloat(amount).toFixed(2);
        }
    </script>
</body>
</html>
