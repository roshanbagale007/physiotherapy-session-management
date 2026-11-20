<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tax Invoice - ${inv.invoiceNumber} | PhysioCare</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', Arial, sans-serif;
        }
        body {
            background-color: #f1f5f9;
            color: #0f172a;
            padding: 2rem;
            display: flex;
            justify-content: center;
        }
        .invoice-card {
            background: white;
            width: 100%;
            max-width: 820px;
            padding: 2.5rem;
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
            border: 1px solid #e2e8f0;
        }
        .header-bar {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            border-bottom: 2px solid #0077b6;
            padding-bottom: 1.5rem;
            margin-bottom: 1.5rem;
        }
        .clinic-brand h1 {
            font-size: 1.6rem;
            color: #0077b6;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        .clinic-brand p {
            color: #475569;
            font-size: 0.85rem;
            margin-top: 0.2rem;
            line-height: 1.4;
        }
        .invoice-meta {
            text-align: right;
        }
        .invoice-meta h2 {
            font-size: 1.4rem;
            color: #0f172a;
            letter-spacing: 0.5px;
        }
        .invoice-badge {
            display: inline-block;
            background: #dcfce7;
            color: #15803d;
            border: 1px solid #86efac;
            padding: 0.2rem 0.6rem;
            border-radius: 4px;
            font-weight: 700;
            font-size: 0.8rem;
            margin-top: 0.35rem;
        }
        .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 2rem;
            margin-bottom: 2rem;
            padding: 1.25rem;
            background: #f8fafc;
            border-radius: 6px;
            border: 1px solid #e2e8f0;
        }
        .info-col h3 {
            font-size: 0.82rem;
            text-transform: uppercase;
            color: #64748b;
            letter-spacing: 0.5px;
            margin-bottom: 0.5rem;
        }
        .info-col p {
            font-size: 0.92rem;
            margin-bottom: 0.25rem;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 2rem;
        }
        table th {
            background: #f1f5f9;
            color: #334155;
            text-align: left;
            padding: 0.75rem 1rem;
            font-size: 0.82rem;
            text-transform: uppercase;
            border-bottom: 2px solid #cbd5e1;
        }
        table td {
            padding: 1rem;
            border-bottom: 1px solid #e2e8f0;
            font-size: 0.92rem;
        }
        .totals-section {
            display: flex;
            justify-content: flex-end;
            margin-bottom: 2rem;
        }
        .totals-box {
            width: 320px;
        }
        .totals-row {
            display: flex;
            justify-content: space-between;
            padding: 0.4rem 0;
            font-size: 0.92rem;
            color: #334155;
        }
        .totals-row.grand-total {
            border-top: 2px solid #0077b6;
            font-size: 1.15rem;
            font-weight: 800;
            color: #0f172a;
            padding-top: 0.75rem;
            margin-top: 0.5rem;
        }
        .auth-bar {
            margin-top: 2.5rem;
            padding-top: 1.5rem;
            border-top: 1px dashed #cbd5e1;
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
        }
        .student-seal {
            font-size: 0.78rem;
            color: #64748b;
            line-height: 1.4;
        }
        .signatory {
            text-align: center;
        }
        .signatory-line {
            width: 180px;
            border-bottom: 1px solid #0f172a;
            margin-bottom: 0.35rem;
        }
        .btn-actions {
            margin-top: 1.5rem;
            display: flex;
            gap: 1rem;
            justify-content: center;
        }
        .btn {
            padding: 0.65rem 1.4rem;
            border-radius: 6px;
            font-weight: 600;
            font-size: 0.9rem;
            cursor: pointer;
            text-decoration: none;
            border: none;
        }
        .btn-print-cta {
            background: #0077b6;
            color: white;
        }
        .btn-close {
            background: #e2e8f0;
            color: #334155;
        }

        /* --- Print Styles --- */
        @media print {
            body {
                background: white;
                padding: 0;
            }
            .invoice-card {
                box-shadow: none;
                border: none;
                padding: 0;
                max-width: 100%;
            }
            .btn-actions {
                display: none;
            }
        }
    </style>
</head>
<body>
    <div class="invoice-card">
        <!-- Clinic Header -->
        <div class="header-bar">
            <div class="clinic-brand">
                <h1>🏥 PhysioCare Rehabilitation Hub</h1>
                <p>
                    MIT-ADT University Health Sciences Complex &bull; Loni Kalbhor, Pune - 412201<br>
                    GSTIN: <strong>27AABCU9603R1ZM</strong> &bull; State Code: 27 (Maharashtra)<br>
                    Phone: +91 20 6819 9900 &bull; Email: billing@physiocare-mit.edu.in
                </p>
            </div>
            <div class="invoice-meta">
                <h2>TAX INVOICE</h2>
                <p style="font-weight: 700; color: #0077b6; margin-top: 0.2rem;">${inv.invoiceNumber}</p>
                <p style="font-size: 0.85rem; color: #64748b; margin-top: 0.2rem;">Date: ${inv.createdAt}</p>
                <div class="invoice-badge">✓ ${inv.paymentStatus} via ${inv.paymentMethod}</div>
            </div>
        </div>

        <!-- Billed To & Clinical Info -->
        <div class="info-grid">
            <div class="info-col">
                <h3>Billed To (Patient)</h3>
                <p><strong>${inv.patientName}</strong></p>
                <p>Phone: ${inv.patientPhone}</p>
                <p>Email: ${inv.patientEmail}</p>
                <p>Gender / Blood: ${inv.gender} &bull; ${inv.bloodGroup}</p>
            </div>
            <div class="info-col">
                <h3>Clinical &amp; Treatment Details</h3>
                <p><strong>Diagnosis:</strong> ${inv.diagnosis}</p>
                <p><strong>Supervising Specialist:</strong> ${inv.therapistName}</p>
                <p><strong>Package Validity:</strong> 90 Days from Purchase Date</p>
                <p><strong>Available Balance:</strong> <strong style="color: #15803d;">${inv.remainingSessions} of ${inv.totalSessions} Sessions</strong> remaining</p>
            </div>
        </div>

        <!-- Itemized Table -->
        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Package Description</th>
                    <th style="text-align: center;">Total Sessions</th>
                    <th style="text-align: right;">Gross Rate</th>
                    <th style="text-align: right;">Discount</th>
                    <th style="text-align: right;">Net Amount</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>1</td>
                    <td>
                        <strong>${inv.packageName}</strong><br>
                        <small style="color: #64748b;">Comprehensive physiotherapy care, modalities, manual therapy, and HEP exercise monitoring.</small>
                    </td>
                    <td style="text-align: center;"><strong>${inv.totalSessions}</strong></td>
                    <td style="text-align: right;">₹${inv.totalAmount}</td>
                    <td style="text-align: right; color: #b91c1c;">- ₹${inv.discountAmount}</td>
                    <td style="text-align: right; font-weight: 700;">₹${inv.netAmount}</td>
                </tr>
            </tbody>
        </table>

        <!-- Tax & Totals Breakdown -->
        <div class="totals-section">
            <div class="totals-box">
                <div class="totals-row">
                    <span>Taxable Base Value:</span>
                    <span>₹${inv.baseTaxable}</span>
                </div>
                <div class="totals-row">
                    <span>CGST (9.0%):</span>
                    <span>₹${inv.cgst}</span>
                </div>
                <div class="totals-row">
                    <span>SGST (9.0%):</span>
                    <span>₹${inv.sgst}</span>
                </div>
                <div class="totals-row grand-total">
                    <span>Total Paid (INR):</span>
                    <span>₹${inv.netAmount}</span>
                </div>
            </div>
        </div>

        <!-- Signatures & Academic Project Watermark -->
        <div class="auth-bar">
            <div class="student-seal">
                <p><strong>24MCAC302 - Advance Java Programming Project</strong></p>
                <p>Student: <strong>ROSHAN NAMDEO BAGALE</strong> (Roll No: 50, Div D)</p>
                <p>Faculty Guide: <strong>Dr. Netra Patil</strong> | MIT-ADT University, Pune</p>
            </div>
            <div class="signatory">
                <div class="signatory-line"></div>
                <p style="font-size: 0.82rem; font-weight: 700;">Authorized Signatory</p>
                <p style="font-size: 0.75rem; color: #64748b;">PhysioCare Clinical Billing</p>
            </div>
        </div>

        <!-- Action CTAs (Hidden during Print) -->
        <div class="btn-actions">
            <button onclick="window.print()" class="btn btn-print-cta">
                🖨️ Print Tax Invoice (PDF / A4)
            </button>
            <button onclick="window.close()" class="btn btn-close">
                Close Window
            </button>
        </div>
    </div>
</body>
</html>
