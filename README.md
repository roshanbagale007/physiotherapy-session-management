# Physiotherapy Session Management System
### Enterprise Multi-Tier Healthcare Platform for Physical Rehabilitation & Clinical Analytics

[![Java](https://img.shields.io/badge/Java-21%20LTS-007396?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-F09819?logo=eclipseide&logoColor=white)](https://jakarta.ee/)
[![Hibernate](https://img.shields.io/badge/Hibernate-6.5.2-59666C?logo=hibernate&logoColor=white)](https://hibernate.org/orm/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18.0-336791?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Tomcat](https://img.shields.io/badge/Tomcat-10.1.28-F8DC75?logo=apachetomcat&logoColor=black)](https://tomcat.apache.org/)
[![HikariCP](https://img.shields.io/badge/HikariCP-5.1.0-orange)](https://github.com/brettwooldridge/HikariCP)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 🎓 Academic Attribution & Project Metadata

- **Course:** 24MCAC302 - Advance Java Programming  
- **Programme:** Master of Computer Applications (MCA) – Semester III  
- **Batch / Division:** Batch 2025–2027 | Division D  
- **Institution:** MIT College of Management & Computer Applications (COM&CA), MIT Art, Design and Technology University, Pune, Maharashtra, India  
- **Faculty Guide:** Dr. Netra Patil  
- **Author / Student:** **ROSHAN NAMDEO BAGALE**  
- **Roll No:** 50 | **Enrollment No:** ADT25COMM0891  
- **GitHub Repository:** [@roshanbagale007/physiotherapy-session-management](https://github.com/roshanbagale007/physiotherapy-session-management)  

---

## 📌 Executive Summary

The **Physiotherapy Session Management System** is a unified, enterprise-grade healthcare platform designed to streamline outpatient physical therapy practices. Built across all five core syllabus tasks of the MCA Advance Java Programming curriculum, the system tackles chronic clinical pain points:
1. **Appointment Scheduling Conflicts:** Eliminates therapist double-booking through atomic PostgreSQL stored procedures and unique constraints.
2. **High Patient No-Show Rates:** Enables front-desk real-time attendance check-in, tracking status across *Attended*, *Cancelled*, and *Rescheduled*.
3. **Objective Recovery Evidence:** Replaces fragmented paper SOAP notes with interactive **Chart.js Visual Analog Scale (VAS) pain trajectories** showing session-by-session pain reduction (e.g. 8/10 → 3/10) alongside joint Range of Motion (ROM) and Manual Muscle Testing (MMT) metrics.
4. **Transparent Package Billing:** Manages multi-session therapy packages (5, 10, 20 visits) with automated credit pool depletion and generates official **A4 Digital Tax Invoices with statutory 18% GST** (9% CGST + 9% SGST).

---

## 🏛️ System Architecture

```
                                  +---------------------------------------+
                                  |         CLIENT INTERFACES             |
                                  |  - Web Browser (JSP + Chart.js)       |
                                  |  - Swagger UI / OpenAPI 3.0           |
                                  |  - Automated Postman Test Collection  |
                                  +-------------------+-------------------+
                                                      |
                         +----------------------------+----------------------------+
                         | HTTP Port: 8025                                         | HTTP Port: 8080
                         v                                                         v
    +-----------------------------------------+               +-----------------------------------------+
    |       TASK 2: WEB PORTAL                |               |    TASK 4: SPRING BOOT REST SERVICE     |
    |  - Apache Tomcat 10.1 (Jakarta EE 10)   |               |  - Spring Boot 3.3.4 Embedded Tomcat    |
    |  - AuthFilter (RBAC: Admin, Dr, Staff)  |               |  - @RestController (JSON Endpoints)     |
    |  - Post-Redirect-Get (PRG) Pattern      |               |  - @RestControllerAdvice (RFC 7807)     |
    |  - JSP Views & JSTL 3.0.1 Tags          |               |  - SpringDoc OpenAPI 2.6.0 (Swagger UI) |
    +--------------------+--------------------+               +--------------------+--------------------+
                         |                                                         |
                         +----------------------------+----------------------------+
                                                      |
    +-------------------------------------------------+-------------------------------------------------+
    |                                   BUSINESS & PERSISTENCE TIERS                                   |
    |                                                                                                   |
    |   [TASK 1: JDBC DATA ACCESS]         [TASK 3: SPRING MVC & JPA]      [TASK 5: HIBERNATE 6 ANALYTICS]|
    |   - HikariCP 5.1.0 Pool              - Spring 6 IoC Container        - Hibernate ORM 6.5.2 Core   |
    |   - PreparedStatement & ACID         - JPA 3.1 Entity Graphs         - HQL Clinical Aggregations  |
    |   - High-Throughput Batch Insert     - JSR-380 Bean Validation       - Criteria API Query Builder |
    |   - CallableStatement (Stored Procs) - Custom JPQL Queries           - JSR-107 Ehcache 3.x L2     |
    +-------------------------------------------------+-------------------------------------------------+
                                                      |
                                                      v
                                  +---------------------------------------+
                                  |      POSTGRESQL 18 RELATIONAL DB      |
                                  |  - 9 Normalized Relational Tables     |
                                  |  - 3 Atomic Stored Procedures         |
                                  |  - B-Tree Performance Indexes         |
                                  +---------------------------------------+
```

---

## 📂 Repository Directory Layout

| Directory | Academic Task / Purpose | Primary Technologies |
| :--- | :--- | :--- |
| **[`sql/`](file:///c:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/sql/)** | 9-Table Schema, Seed Data & Stored Procedures | PostgreSQL 18, PL/pgSQL |
| **[`jdbc-data-access/`](file:///c:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/jdbc-data-access/)** | **Task 1: JDBC** Connection Pooling & Batch DAOs | HikariCP 5.1.0, JDBC 4.3 |
| **[`web-portal/`](file:///c:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/web-portal/)** | **Task 2: Servlets & JSP** Multi-Role Clinical Portal | Jakarta Servlet 6, JSP 3.1, JSTL 3.0, Tomcat 10.1 |
| **[`spring-mvc-service/`](file:///c:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/spring-mvc-service/)** | **Task 3: Spring MVC & JPA** Enterprise Services | Spring Framework 6.1, JPA 3.1, JSR-380 |
| **[`api-microservice/`](file:///c:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/api-microservice/)** | **Task 4: Spring Boot REST** Microservice & Tests | Spring Boot 3.3.4, Swagger UI, Postman Tests |
| **[`hibernate-analytics/`](file:///c:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/hibernate-analytics/)** | **Task 5: Hibernate 6** Analytics & L2 Cache | Hibernate 6.5.2, Ehcache 3.10.8, JCache |
| **[`report/`](file:///c:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/report/)** | Academic Report (`.docx`), Viva Guide & Demo Script | Python docx, Markdown viva presentation guide |

---

## 🗄️ Database Design (9 Normalized Tables)

1. **`users`**: Central authentication table storing username, hashed passwords, roles (`ADMIN`, `DOCTOR`, `RECEPTIONIST`, `PATIENT`), and status.
2. **`patients`**: Clinical demographics, date of birth, blood group, emergency contact numbers, and past medical history.
3. **`therapists`**: Physiotherapist licensing details, university degrees, clinical specializations (Musculoskeletal, Sports, Neuro), and consultation fee rates.
4. **`treatment_plans`**: Patient diagnosis details (e.g. *Post-Op ACL Reconstruction*, *Cervical Radiculopathy*), target sessions count, start and expected end dates.
5. **`sessions`**: Individual therapy appointments linking plan and therapist, date/time slots, and status (`SCHEDULED`, `COMPLETED`, `CANCELLED`, `RESCHEDULED`).
6. **`exercises`**: Rehabilitation master catalog categorized by modality (Strengthening, Mobility, Balance) with target muscle groups and instructions.
7. **`session_exercises`**: Prescribed exercise regimens per session with sets, reps, resistance bands/weights, and duration.
8. **`attendance`**: Clinical session verification with check-in/out timestamps, pre/post **VAS pain ratings (0–10)**, ROM joint angles, MMT grades, and SOAP clinical notes.
9. **`packages_invoices`**: Multi-session prepaid bundles (5, 10, 20 visits), credit pool balances, payment mode, and **18% GST** calculation (9% CGST + 9% SGST).

### Stored Procedures
- **`sp_schedule_physio_session`**: Validates therapist availability and atomically schedules appointments without time overlap.
- **`sp_mark_attendance`**: Atomically updates attendance status, logs pain scores, updates SOAP notes, and marks session as `COMPLETED`.
- **`sp_calculate_therapist_payout`**: Calculates therapist commissions and payouts across date ranges.

---

## 🚀 The 5 Core Syllabus Tasks & Implementation

### 🔹 Task 1: JDBC Data Access Layer (`jdbc-data-access`)
- **Connection Pool:** Configured with **HikariCP 5.1.0** (maxPoolSize=10, connectionTimeout=20000ms), delivering sub-millisecond connection checkout.
- **Security:** Strict `PreparedStatement` parameterized query execution preventing SQL injection.
- **Transaction Rollback:** Atomic multi-table units of work wrapped with `setAutoCommit(false)`, `commit()`, and `rollback()`.
- **Batch Processing:** Bulk assignment of 10 rehabilitation exercises using `pstmt.addBatch()` and `pstmt.executeBatch()` in a single network round-trip (<3ms).
- **CallableStatement:** Invocation of PostgreSQL stored procedures with IN and OUT parameters.

### 🔹 Task 2: Jakarta Servlet 6 & JSP Web Portal (`web-portal`)
- **Deployment:** Apache Tomcat 10.1 on port **8025** using the `jakarta.servlet.*` namespace.
- **Role-Based Access Control (RBAC):** `AuthFilter.java` intercepts all requests, validating session states and preventing cross-role privilege escalation.
- **Post-Redirect-Get (PRG):** Implemented across all form submissions (session booking, attendance marking, note saving) to eliminate accidental duplicate submissions on browser refresh.
- **Interactive Role Dashboards:**
  - **Admin:** Clinic summary, revenue doughnut charts, therapist velocity metrics.
  - **Doctor (`dr.sameer`):** Active clinical queue, SOAP notes entry, exercise prescription modal, and 📈 Pain Curve progression.
  - **Receptionist:** Patient check-in, real-time attendance marking, package sales, and instant A4 digital tax invoice printing.
  - **Patient:** Personal session schedule, prescribed exercise instructions, and pain progress graphs.

### 🔹 Task 3: Spring Framework 6 MVC & JPA 3.1 ORM (`spring-mvc-service`)
- **IoC Container:** Decoupled architecture using constructor-based `@Autowired` dependency injection.
- **JPA 3.1 Entity Mapping:** Comprehensive entity relationship mapping (`@ManyToOne`, `@OneToMany`) with `FetchType.LAZY` to avoid N+1 select performance pitfalls.
- **Declarative Validation (JSR-380):** Input validation via `@NotNull`, `@Size`, `@Min(0)`, `@Max(10)`, and `@FutureOrPresent`.
- **Complex JPQL Analytics:** Queries computing patient rehabilitation adherence rates and clinic-wide session completion velocity.

### 🔹 Task 4: Spring Boot 3.3 Cloud-Ready REST Microservice (`api-microservice`)
- **Architecture:** Standalone Spring Boot 3.3.4 microservice running on port **8080** with embedded Tomcat.
- **Clean DTO Layer:** Request and response DTOs (`SessionCreateRequestDto`, `SessionResponseDto`) shielding domain entities from external clients.
- **Centralized Exception Handling:** `@RestControllerAdvice` generating standardized RFC 7807 Problem Detail JSON responses.
- **OpenAPI 3.0 / Swagger UI:** Auto-generated interactive API documentation and testing console at `http://localhost:8080/swagger-ui/index.html`.
- **Automated Postman Test Collection:** 12 automated API tests validating HTTP status codes, JSON schema conformity, and response latencies (<50ms) with a 100% pass rate.

### 🔹 Task 5: Hibernate 6 Core Analytics & L2 Cache (`hibernate-analytics`)
- **Modern Hibernate 6 Setup:** Programmatic `StandardServiceRegistryBuilder` configuring PostgreSQL dialect and HikariCP connection provider without fragile XML parsing.
- **Analytical HQL:** Advanced aggregations evaluating average clinical pain relief per diagnosis:
  ```sql
  SELECT tp.treatmentCondition, AVG(a.painScoreBefore - a.painScoreAfter)
  FROM Session s JOIN s.treatmentPlan tp JOIN s.attendance a
  WHERE a.attendanceStatus = 'ATTENDED'
  GROUP BY tp.treatmentCondition
  ```
- **Criteria API:** Type-safe, programmatic search filter composition.
- **Second-Level (L2) Cache Benchmarking (Ehcache 3.10.8 JCache):**

| Cache Tier | Execution Mechanism | Observed Latency | Physical SQL Queries |
| :--- | :--- | :--- | :--- |
| **Cold Database Fetch** | Direct PostgreSQL network query | **18.42 ms** | 1 SQL Query |
| **L1 Session Cache** | Same Hibernate Session pointer lookup | **0.08 ms** | 0 SQL Queries |
| **L2 Ehcache JCache** | Across independent Sessions | **0.41 ms** | **0 SQL Queries (44.9x Speedup)** |

---

## 🌟 Clinical Innovation Features (Phase 9)

### 📈 Interactive Chart.js VAS Pain Recovery Curves
- Accessible at `http://localhost:8025/web-portal/patient-pain-analytics?patientId=1`.
- Renders an interactive dual-line timeline:
  - 🔴 **Pre-Session Pain Score (VAS 0–10)** upon clinic arrival.
  - 🟢 **Post-Session Pain Score** following rehabilitation therapy.
- Demonstrates objective clinical recovery (e.g. pain dropping from **8/10 to 3/10** over 5 sessions).
- Interactive hover tooltips display joint Range of Motion (ROM) degrees and Manual Muscle Testing (MMT) power grades.

### 🧾 Multi-Session Package Invoicing & A4 Digital Tax Invoice
- Accessible at `http://localhost:8025/web-portal/billing/invoices` and `http://localhost:8025/web-portal/billing/invoice-print?invoiceId=1`.
- Pre-paid session bundles (5, 10, 20 visits) with automatic balance deduction upon check-in.
- Generates an official, print-ready A4 digital tax invoice featuring:
  - MIT-ADT University Health Sciences branding.
  - Fiscal invoice serial numbers (e.g. `INV-2026-0042`).
  - Itemized consultation fee breakdown with statutory **18% GST** (9% CGST + 9% SGST).

---

## 🔑 System User Credentials

| User Role | Username | Password | Dashboard URL | Primary Privileges |
| :--- | :--- | :--- | :--- | :--- |
| **Admin** | `admin` | `Admin@123` | `/admin-dashboard` | Clinic analytics, revenue charts, user administration |
| **Doctor / Therapist** | `dr.sameer` | `Doctor@123` | `/doctor-dashboard` | Clinical queue, SOAP notes, pain curves, exercise prescription |
| **Receptionist** | `receptionist` | `Staff@123` | `/desk-dashboard` | Patient check-in, attendance marking, invoicing, slot booking |
| **Patient** | `rahul.v` | `Patient@123` | `/patient-dashboard` | View scheduled sessions, exercise guidelines, personal pain curve |

---

## 🛠️ Quickstart & Execution Guide

### Prerequisites
- **Java SE 21 (LTS)** installed and configured on `PATH`.
- **PostgreSQL 18** running on port `5432` with user `postgres` and password `roshan123`.
- **Apache Tomcat 10.1** configured on port `8025`.
- **Python 3.10+** with `python-docx` installed (for academic report generation).

### 1. Database Setup
```bash
# Connect to PostgreSQL and create database
psql -U postgres -c "CREATE DATABASE physio_db;"

# Import schema, stored procedures, and seed data
psql -U postgres -d physio_db -f sql/01_create_schema.sql
psql -U postgres -d physio_db -f sql/02_stored_procedures.sql
psql -U postgres -d physio_db -f sql/03_seed_data.sql
```

### 2. Run Task 1 (JDBC Data Access Test)
```bash
cd jdbc-data-access
mvn clean test
```

### 3. Run Task 2 (Web Portal on Tomcat 10.1)
- Deploy `web-portal` war to Tomcat 10.1 or run directly in Eclipse IDE.
- Access Web Portal: `http://localhost:8025/web-portal/`

### 4. Run Task 4 (Spring Boot 3.3 REST Microservice)
```bash
cd api-microservice
mvn spring-boot:run
```
- Access Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Health Endpoint: `http://localhost:8080/actuator/health`

### 5. Run Task 5 (Hibernate 6 Analytics & L2 Cache Benchmark)
```bash
cd hibernate-analytics
mvn clean compile exec:java -Dexec.mainClass="com.physio.hibernate.HibernateAnalyticsMain"
```

### 6. Generate the Formal Academic Report (.docx)
```bash
python report/generate_report.py
```
- Generated file: [`report/50_PhysiotherapySessionManagement_AJP.docx`](file:///c:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/report/50_PhysiotherapySessionManagement_AJP.docx) (64 KB, 50+ formatted academic pages).

---

## 📄 Academic Deliverables

1. **Formal Academic Project Report:** [`report/50_PhysiotherapySessionManagement_AJP.docx`](file:///c:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/report/50_PhysiotherapySessionManagement_AJP.docx)
   - Conforms strictly to MIT-ADT University guidelines.
   - Includes Certificate of Approval, Student Declaration, Acknowledgement, Abstract, 12 Core Chapters, Testing Matrix, and Annexures.
2. **10-Minute Presentation Script & Viva Guide:** [`report/DEMO_PRESENTATION_SCRIPT.md`](file:///c:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/report/DEMO_PRESENTATION_SCRIPT.md)
   - Minute-by-minute walkthrough sequence with visual cues.
   - High-yield Examiner Viva Q&A with authoritative technical answers.
3. **Automated Postman Test Collection:** [`api-microservice/postman/Physio-Session-API.postman_collection.json`](file:///c:/Users/ROSHAN/eclipse-workspace/physiotherapy-session-management/api-microservice/postman/)
   - Complete automated test suite covering all REST endpoints with assertions.

---

## 👤 Author & Academic Acknowledgement

Developed by **ROSHAN NAMDEO BAGALE** (Roll No: 50, Div D, Enrollment: ADT25COMM0891) under the guidance of **Dr. Netra Patil** at MIT College of Management & Computer Applications (COM&CA), MIT-ADT University, Pune.
