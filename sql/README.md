# Database Architecture: Physiotherapy Session Management

**Course:** 24MCAC302 - Advance Java Programming  
**Student:** ROSHAN NAMDEO BAGALE (Roll No: 50, Div D)  
**Database Name:** `physio_db`  

---

## 1. Schema Overview (9 Normalized Tables)

```
                            Entity-Relationship Model
  ┌───────────┐         ┌───────────┐         ┌───────────────────┐
  │   users   │◄────────┤ therapists│◄────────┤     sessions      │
  └─────┬─────┘ 1     1 └─────┬─────┘ 1     * └─────────┬─────────┘
        │                     │                         │ 1
        │ 1                   │ 1                       │
        ▼ 1                   ▼ *                       ▼ 1
  ┌───────────┐         ┌──────────────────┐  ┌───────────────────┐
  │ patients  │◄────────┤ treatment_plans  │  │    soap_notes     │
  └─────┬─────┘ 1     * └─────────┬────────┘  └───────────────────┘
        │                         │ 1
        │ 1                       ▼ *
        │ 1             ┌──────────────────┐
        ▼ *             │  plan_exercises  │
  ┌───────────────────┐ └─────────┬────────┘
  │ packages_invoices │           │ *
  └───────────────────┘           ▼ 1
                        ┌──────────────────┐
                        │    exercises     │
                        └──────────────────┘
```

| Table Name | Primary Key | Foreign Keys | Clinical & Architectural Purpose |
| :--- | :--- | :--- | :--- |
| **`users`** | `id` | None | Central authentication and Role-Based Access Control (RBAC: `ADMIN`, `THERAPIST`, `RECEPTIONIST`, `PATIENT`). |
| **`therapists`** | `id` | `user_id` $\rightarrow$ `users(id)` | Specialist clinical profiles, license numbers, consultation fees, and available days. |
| **`patients`** | `id` | `user_id` $\rightarrow$ `users(id)` | Patient demographic, clinical intake, medical history, and emergency contact details. |
| **`treatment_plans`**| `id` | `patient_id` $\rightarrow$ `patients(id)`, `therapist_id` $\rightarrow$ `therapists(id)` | Rehabilitation prescriptions, target sessions vs completed sessions, and diagnosis milestones. |
| **`exercises`** | `id` | None | Clinical exercise directory with target muscle groups, categories, and instruction sets. |
| **`plan_exercises`** | `id` | `plan_id` $\rightarrow$ `treatment_plans(id)`, `exercise_id` $\rightarrow$ `exercises(id)` | Prescribed Home Exercise Programs (HEP) with prescribed dosage (sets, reps, frequency). |
| **`packages_invoices`**| `id` | `patient_id` $\rightarrow$ `patients(id)` | Multi-session packages (e.g. 10 sessions) with remaining session credits and billing status. |
| **`sessions`** | `id` | `plan_id`, `therapist_id`, `patient_id`, `package_invoice_id` | Appointment slots and real-time attendance (`SCHEDULED`, `ATTENDED`, `CANCELLED`). Enforces no double-booking via unique constraint `(therapist_id, session_date, start_time)`. |
| **`soap_notes`** | `id` | `session_id` $\rightarrow$ `sessions(id)` | Digital progress documentation: Subjective complaint, Objective metrics (VAS pain scale 0–10, Range of Motion degrees), Assessment, Plan, and modalities used. |

---

## 2. SQL Files Included

1. **`schema_postgresql.sql`**: PostgreSQL 18 schema using `BIGSERIAL`, `TIMESTAMP`, and `CHECK` constraints.
2. **`schema_mysql.sql`**: MySQL 8 schema using `AUTO_INCREMENT`, `ENGINE=InnoDB`, and `ENUM` types.
3. **`procedure_postgresql.sql`**: PostgreSQL stored procedure `sp_complete_session_and_deduct` for atomic session attendance, SOAP notes insertion, and package session deduction.
4. **`procedure_mysql.sql`**: MySQL equivalent stored procedure with transaction rollback handling.
5. **`data.sql`**: Clinical seed data with 7 users, 2 therapists, 3 patients with realistic conditions (Lumbar Radiculopathy, ACL Reconstruction, Frozen Shoulder), 8 exercises, active packages, scheduled sessions, and historical SOAP notes.

---

## 3. How to Execute

### Option A: In PostgreSQL (Using pgAdmin 4 or psql)
1. Open **pgAdmin 4** (or open `psql`).
2. Create a new database named `physio_db`:
   ```sql
   CREATE DATABASE physio_db;
   ```
3. Connect to `physio_db` and open Query Tool.
4. Run in order:
   - Run `schema_postgresql.sql`
   - Run `procedure_postgresql.sql`
   - Run `data.sql`

### Option B: In MySQL (Using MySQL Workbench or CLI)
1. Open MySQL Workbench / Command Line:
   ```bash
   mysql -u root -p
   ```
2. Run in order:
   ```sql
   source C:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/sql/schema_mysql.sql;
   source C:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/sql/procedure_mysql.sql;
   source C:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/sql/data.sql;
   ```

---

## 4. Verification Queries

After loading the scripts, run these queries to verify data integrity:

```sql
-- 1. Check all tables populated
SELECT 'users' AS tbl, COUNT(*) FROM users
UNION ALL SELECT 'therapists', COUNT(*) FROM therapists
UNION ALL SELECT 'patients', COUNT(*) FROM patients
UNION ALL SELECT 'treatment_plans', COUNT(*) FROM treatment_plans
UNION ALL SELECT 'exercises', COUNT(*) FROM exercises
UNION ALL SELECT 'plan_exercises', COUNT(*) FROM plan_exercises
UNION ALL SELECT 'packages_invoices', COUNT(*) FROM packages_invoices
UNION ALL SELECT 'sessions', COUNT(*) FROM sessions
UNION ALL SELECT 'soap_notes', COUNT(*) FROM soap_notes;

-- 2. Verify Patient Recovery Progress Curve (Pain Reduction)
SELECT 
    s.id AS session_id,
    s.session_date,
    p.user_id,
    u.full_name AS patient_name,
    sn.pain_score_before,
    sn.pain_score_after,
    (sn.pain_score_before - sn.pain_score_after) AS pain_relief,
    sn.range_of_motion_notes
FROM sessions s
JOIN soap_notes sn ON s.id = sn.session_id
JOIN patients p ON s.patient_id = p.id
JOIN users u ON p.user_id = u.id
ORDER BY s.session_date ASC;
```
