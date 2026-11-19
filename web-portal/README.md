# Task 2: Jakarta Servlets 6 & JSP Web Application

**Module Directory:** `web-portal/`  
**Course:** 24MCAC302 - Advance Java Programming (Unit 2)  
**Server Target:** Apache Tomcat 10.1 (Port 8025)  
**Student:** ROSHAN NAMDEO BAGALE (Roll No: 50, Div D)  

---

## 1. Syllabus Unit 2 Architecture & Workflows

```
                           Clinical Web Portal Architecture
   ┌─────────────────────────────────────────────────────────────────────────────┐
   │                  Apache Tomcat 10.1 (Port 8025) - Context: /web-portal       │
   └──────────────────────────────────────┬──────────────────────────────────────┘
                                          │
                                 [ AuthFilter (/*) ]
                                          │
                   ┌──────────────────────┼──────────────────────┐
                   ▼                      ▼                      ▼
         [Front Desk Portal]      [Doctor Hub Portal]    [Patient Portal]
         /desk/dashboard          /doctor/dashboard      /patient/dashboard
         /desk/book-session       /doctor/soap-note      /patient/log-exercise
         /desk/mark-attendance            │                      │
                   │                      │                      │
                   └──────────────────────┼──────────────────────┘
                                          ▼
                         [ Post-Redirect-Get (PRG) Pattern ]
                                          │
                                          ▼
                      [ PostgreSQL 18+ (physio_db) Via HikariCP ]
```

---

## 2. Interactive Clinical Workflows

### A. Front Desk (Receptionist) Workflows (`/desk/*`)
* **Live Schedule & Attendance:** Lists upcoming appointments with one-click **"✓ Mark Present"** and **"✕ Cancel"** buttons.
* **Appointment Booking (`/desk/book-session`):**
  * Selects Patient, Active Treatment Plan, Specialist, Date, and 45-minute Time Slot.
  * Links prepaid therapy package (auto-deducts session credit).
  * **Real-time Conflict Detection:** Prevents double-booking therapist slots.
  * **PRG Pattern:** Redirects after booking to eliminate duplicate form submissions on browser refresh.

### B. Doctor Clinical Workflows (`/doctor/*`)
* **Patient Appointment Queue:** Shows scheduled visits.
* **Digital SOAP Notes Form (`/doctor/soap-note`):**
  * Patient intake summary (Diagnosis, Rehab Goals, Slot).
  * **Interactive Pain Sliders (VAS 0–10):** Visual sliders tracking pain before vs after therapy.
  * **Range of Motion (ROM):** Joint flexibility degrees (e.g. *Shoulder Abduction 145 deg*).
  * **Manual Muscle Testing (MMT):** Strength grade (1/5 to 5/5).
  * **Therapy Modalities Checkboxes:** IFT, Ultrasound, Mechanical Traction, Cryotherapy, Manual Mobilization, Dry Needling.
  * **Stored Procedure Integration:** Invokes `sp_complete_session_and_deduct` via CallableStatement to atomically save SOAP notes, mark attendance, and update plan milestones.

### C. Patient Portal Workflows (`/patient/*`)
* **Rehabilitation Milestone Tracker:** Progress bar showing completed sessions vs target (e.g. *4 of 10 sessions completed*).
* **Upcoming Appointments:** Dates, times, and therapist names.
* **Home Exercise Program (HEP):** Prescribed exercises with sets, reps, clinician notes, and interactive **"✓ Mark Done"** daily adherence logging.

---

## 3. Test User Accounts

| Role | Username | Password | Dedicated Route |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin` | `Admin@123` | `http://localhost:8025/web-portal/admin/dashboard` |
| **THERAPIST (Doctor)** | `dr.sameer` | `Doctor@123` | `http://localhost:8025/web-portal/doctor/dashboard` |
| **RECEPTIONIST** | `receptionist` | `Staff@123` | `http://localhost:8025/web-portal/desk/dashboard` |
| **PATIENT** | `rahul.v` | `Patient@123` | `http://localhost:8025/web-portal/patient/dashboard` |
