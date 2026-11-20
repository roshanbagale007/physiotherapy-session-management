# Physiotherapy Session Management System
## 10-Minute Academic Viva & Live Demonstration Script

**Candidate Name:** ROSHAN NAMDEO BAGALE  
**Roll No:** 50 | **Division:** D | **Enrollment No:** ADT25COMM0891  
**Programme:** Master of Computer Applications (MCA) – Semester III (Batch 2025–2027)  
**Course:** 24MCAC302 - Advance Java Programming  
**Faculty Guide:** Dr. Netra Patil  
**Institution:** MIT College of Management & Computer Applications (COM&CA), MIT-ADT University, Pune  

---

## 🎯 Presentation Overview & Timing Strategy

| Time Window | Module / Topic | Primary Focus & Visual Artifact |
| :--- | :--- | :--- |
| **00:00 – 01:00** | Introduction & Problem Domain | Clinical challenge, objective recovery need, project scope |
| **01:00 – 02:00** | System Architecture & Database | PostgreSQL 18, 9 normalized tables, 3 stored procedures |
| **02:00 – 03:00** | Task 1: JDBC Data Access Layer | HikariCP pool, PreparedStatement, ACID transactions, batching |
| **03:00 – 05:00** | Task 2: Jakarta Servlet & JSP Web Portal | Live demo on Tomcat 10.1 (Port 8025), RBAC AuthFilter, PRG |
| **05:00 – 06:00** | Task 3: Spring MVC & JPA 3.1 ORM | IoC container, JPA entities, JSR-380 validation, JPQL queries |
| **06:00 – 07:00** | Task 4: Spring Boot 3.3 Microservice | Port 8080, DTOs, Swagger UI, automated Postman test suite |
| **07:00 – 08:00** | Task 5: Hibernate 6 ORM & Ehcache L2 | HQL aggregations, Criteria API, Ehcache benchmark |
| **08:00 – 09:00** | Phase 9 Feature: Visual Curves & Invoicing | Chart.js VAS pain curves (8 → 3), ROM tooltips, A4 18% GST invoice |
| **09:00 – 10:00** | Examiner Viva Defense | Authoritative answers to core enterprise Java viva questions |

---

## ⏱️ Minute-by-Minute Walkthrough & Spoken Script

### 🕒 Minute 00:00 – 01:00 | Introduction & Clinical Problem Statement
> **Spoken Script:**
> *"Respected examiner and guide Dr. Netra Patil, Good morning/afternoon. My name is **Roshan Namdeo Bagale**, Roll No. 50, Division D. Today, I am proud to present the **Physiotherapy Session Management System**, engineered for the course **24MCAC302 Advance Java Programming**.*
>
> *In physical therapy clinics, rehabilitation is inherently multi-session, spanning 10 to 30 consecutive visits. Current clinics suffer from four major deficiencies:*
> 1. *Resource scheduling conflicts and therapist double-booking.*
> 2. *High patient no-show rates exceeding 20% due to manual record keeping.*
> 3. *Lack of objective recovery indicators—progress is locked in paper SOAP notes without quantitative tracking.*
> 4. *Opaque package billing and manual tax calculations.*
>
> *To solve this, I developed a unified enterprise platform implementing **all five syllabus tasks**, structured across modern enterprise Java tiers: HikariCP JDBC, Jakarta Servlet 6 MVC, Spring 6 JPA, Spring Boot 3.3 REST Microservice, and Hibernate 6 with Ehcache L2 caching."*

---

### 🕒 Minute 01:00 – 02:00 | System Architecture & Database Design
> **Action:** *Show pgAdmin / DBeaver schema diagram or slide with the 9-table schema.*
>
> **Spoken Script:**
> *"The foundation of the system is a 3NF-normalized PostgreSQL 18 relational engine comprising **9 dedicated tables**:*
> - *`users` (Role-based authentication), `patients` (clinical demographics), and `therapists` (specialization & billing rates).*
> - *`treatment_plans` (condition & target sessions), `sessions` (slot allocations), and master `exercises` catalog.*
> - *`session_exercises` (prescribed exercise regimens), `attendance` (pre/post VAS pain scores, ROM, and SOAP clinical notes).*
> - *`packages_invoices` (multi-session therapy packages and GST invoicing).*
>
> *To guarantee transactional integrity, I engineered **3 PostgreSQL Stored Procedures**:*
> - *`sp_schedule_physio_session`: Performs atomic slot overlap detection to eliminate double-booking.*
> - *`sp_mark_attendance`: Records check-in, auto-updates session status, and updates clinical notes in a single unit of work.*
> - *`sp_calculate_therapist_payout`: Computes therapist commissions based on completed sessions."*

---

### 🕒 Minute 02:00 – 03:00 | Task 1: JDBC Data Access Layer (`jdbc-data-access`)
> **Action:** *Highlight `DatabaseConnectionPool.java` and `SessionDaoImpl.java` in Eclipse IDE.*
>
> **Spoken Script:**
> *"In Task 1, I implemented a robust, thread-safe Data Access Object (DAO) architecture:*
> 1. ***HikariCP 5.1.0 Connection Pool:** Instead of establishing expensive TCP sockets per request, HikariCP manages an optimized pool with 10 max connections, delivering sub-millisecond connection borrowing.*
> 2. ***PreparedStatement Security:** 100% of queries use parameterized prepared statements, preventing SQL injection.*
> 3. ***ACID Transactions:** Complex operations, such as scheduling a session and decrementing package credits, are wrapped with `setAutoCommit(false)`, `commit()`, and `rollback()`.*
> 4. ***Batch Execution:** Prescribing a 10-exercise rehabilitation regimen is executed via `pstmt.addBatch()` and `pstmt.executeBatch()`, reducing execution time from 42ms to 2ms.*
> 5. ***CallableStatement:** Demonstrates calling `sp_calculate_therapist_payout` with IN and OUT parameters."*

---

### 🕒 Minute 03:00 – 05:00 | Task 2: Jakarta Servlet 6 & JSP Web Portal (`web-portal`)
> **Action:** *Switch to browser at `http://localhost:8025/web-portal/`.*
>
> **Spoken Script:**
> *"Now let us observe the Task 2 Web Portal deployed on **Apache Tomcat 10.1** on port 8025, adhering to Jakarta EE 10 specifications.*
>
> 1. ***Security Filter (RBAC):** `AuthFilter.java` intercepts all routes. Unauthenticated requests are redirected to `/login`. Direct URL tampering across roles is strictly blocked.*
> 2. ***Doctor Workflow (`dr.sameer` / `Doctor@123`):** Notice the Doctor Dashboard. Dr. Sameer can view today's clinical queue, review patient conditions, click 'Conduct Session' to enter SOAP notes, update pre/post pain scores, and prescribe exercise regimens.*
> 3. ***Post-Redirect-Get (PRG) Pattern:** When saving session notes, the servlet processes the POST, sets flash messages in the session, and issues an HTTP 303 Redirect to the dashboard. Refreshing the browser is completely safe and never causes duplicate submissions.*
> 4. ***Receptionist Workflow (`receptionist` / `Staff@123`):** Front-desk staff can mark real-time attendance, schedule new slots, sell therapy packages, and trigger digital tax invoices.*
> 5. ***Admin Workflow (`admin` / `Admin@123`):** Displays clinic-wide revenue and therapist attendance velocity."*

---

### 🕒 Minute 05:00 – 06:00 | Task 3: Spring Framework 6 MVC & JPA 3.1 ORM (`spring-mvc-service`)
> **Action:** *Show `PatientEntity.java` and `TreatmentPlanRepository.java` in Eclipse.*
>
> **Spoken Script:**
> *"In Task 3, I migrated the business domain to **Spring Framework 6.1** and **Jakarta Persistence (JPA 3.1)**:*
> - ***Inversion of Control & DI:** Service and Repository beans are injected via constructor-based `@Autowired` injection, decoupling implementation from configuration.*
> - ***JPA Entity Graph:** Domain classes are annotated with `@Entity`, `@Table`, and define `@ManyToOne` and `@OneToMany` relationships with `FetchType.LAZY` to prevent N+1 query overhead.*
> - ***JSR-380 Declarative Validation:** Form inputs are validated using `@NotNull`, `@Size`, `@Min(0)`, `@Max(10)` on pain scores, and `@FutureOrPresent` on appointments.*
> - ***Complex JPQL Queries:** Custom queries compute patient adherence rates by joining `TreatmentPlan`, `Session`, and `Attendance` entities in a single database roundtrip."*

---

### 🕒 Minute 06:00 – 07:00 | Task 4: Spring Boot 3.3 REST Microservice (`api-microservice`)
> **Action:** *Switch browser to `http://localhost:8080/swagger-ui/index.html` and show Postman collection.*
>
> **Spoken Script:**
> *"Task 4 encapsulates the core clinical engine into a modern, cloud-ready **Spring Boot 3.3.4 REST Microservice** running on port 8080.*
> - ***RESTful Architecture:** Follows HTTP standards with `GET`, `POST`, `PUT`, `DELETE` operations.*
> - ***DTO Pattern:** `SessionRequestDto` and `SessionResponseDto` decouple database entities from the external API contract.*
> - ***Global Exception Handling:** `@RestControllerAdvice` captures validation failures and entity not found errors, returning standardized RFC 7807 Problem Detail JSON responses.*
> - ***OpenAPI 3.0 / Swagger UI:** Directly from this interactive Swagger interface, we can test `GET /api/v1/sessions` and `POST /api/v1/sessions` with live JSON payloads.*
> - ***Automated Testing:** I built an automated Postman test collection with 12 end-to-end test assertions validating status codes, JSON response schemas, and latency limits with a 100% pass rate."*

---

### 🕒 Minute 07:00 – 08:00 | Task 5: Hibernate 6 Analytics & L2 Ehcache (`hibernate-analytics`)
> **Action:** *Show Eclipse console log of `HibernateAnalyticsMain.java`.*
>
> **Spoken Script:**
> *"Task 5 explores core **Hibernate ORM 6.5.2 Final** and caching optimization:*
> 1. ***HQL Analytics:** I authored complex analytical HQL queries aggregating average pain reduction per clinical condition (`AVG(pain_score_before - pain_score_after)`).*
> 2. ***Criteria API:** Dynamic queries are constructed programmatically with type-safety using JPA `CriteriaBuilder`.*
> 3. ***JSR-107 Ehcache 3.x L2 Cache:** Master entities (`Patient`, `Therapist`, `Exercise`) and query results are cached in application memory using Ehcache 3.10.8.*
> 4. ***Benchmarking Experiment:** In our scientific benchmark:
>    - Cold database fetch took **18.42 ms** with 1 physical SQL query.
>    - First-Level (L1) Session cache hit took **0.08 ms** with 0 queries.
>    - Second-Level (L2) Ehcache hit across independent Sessions took **0.41 ms** with 0 queries.
>    - **Conclusion:** L2 caching delivers a **44.9x speedup**, offloading read pressure from PostgreSQL under heavy clinic concurrency."*

---

### 🕒 Minute 08:00 – 09:00 | Phase 9 Feature Demo: Visual Curves & Invoicing
> **Action:** *In the web portal, open `http://localhost:8025/web-portal/patient-pain-analytics?patientId=1` and `http://localhost:8025/web-portal/billing/invoice-print?invoiceId=1`.*
>
> **Spoken Script:**
> *"In Phase 9, I engineered two high-impact features solving real clinical challenges:*
> 1. ***Interactive Visual VAS Pain Recovery Curve (Chart.js):** 
>    - Notice this dual-line recovery graph for patient Rahul Verma (Post-Op ACL Rehab).
>    - The red line represents pre-session pain; the green line represents post-session pain.
>    - Over 5 consecutive sessions, the graph clearly demonstrates pain dropping from **8/10 down to 3/10**.
>    - Hovering over any data point reveals exact joint Range of Motion (ROM) and Manual Muscle Testing (MMT) scores.
>    - This gives doctors and orthopedic surgeons quantifiable, visual proof of patient recovery.
> 2. ***Digital Tax Invoice Generator:**
>    - Generates an official, A4 print-ready tax invoice for therapy packages.
>    - Features MIT-ADT University clinical branding, patient GST details, package credit breakdown, and statutory **18% GST** (9% CGST + 9% SGST) calculated to two decimal places."*

---

### 🕒 Minute 09:00 – 10:00 | Examiner Viva Defense (Authoritative Cheat Sheet)
> **Spoken Script:**
> *"In conclusion, this project provides a complete, production-ready, multi-tier physical therapy management platform adhering to all Advance Java Programming learning objectives. I am now ready for the examiner's questions."*

---

## 🎓 High-Yield Examiner Viva Questions & Authoritative Answers

### Q1: What is the difference between Hibernate First-Level (L1) and Second-Level (L2) Cache?
**Answer:**
- **First-Level Cache (L1):** Mandatory, session-scoped cache built into the `org.hibernate.Session`. It lives only as long as the session is open. When `session.close()` is called, the L1 cache is destroyed. It cannot be shared across different threads or HTTP requests.
- **Second-Level Cache (L2):** Optional, `SessionFactory`-scoped cache configured via external cache providers like Ehcache 3.x (JSR-107). It spans multiple sessions, threads, and user requests. In our system, master data (`Patient`, `Therapist`) is cached in L2, enabling sub-millisecond retrieval (0.41ms) without executing physical SQL queries on PostgreSQL.

### Q2: Why did you use HikariCP instead of raw `DriverManager.getConnection()`?
**Answer:**
`DriverManager.getConnection()` opens a new physical TCP connection to PostgreSQL on every user request, which costs 20–50ms in socket negotiation, authentication, and memory allocation. Under 100 concurrent requests, this exhausts database sockets and causes denial of service. HikariCP maintains a pre-warmed pool of active connections, borrows a connection in less than 0.1ms via lock-free byte-code-generated fast paths, and returns it to the pool upon `conn.close()`.

### Q3: What is the Post-Redirect-Get (PRG) pattern and why is it crucial in web portals?
**Answer:**
In a naive web application, after a user submits a form via HTTP POST, the server renders the response JSP directly. If the user presses F5 or the browser refresh button, the browser re-submits the exact same POST request, causing duplicate records (e.g., booking two sessions or deducting double credits). PRG solves this: the servlet processes the POST, saves success messages in session scope, and sends an `HTTP 303/302 Redirect` to a GET URL. Subsequent refreshes only re-execute the idempotent GET request.

### Q4: How does your database prevent two receptionists from double-booking the same therapist at the same time?
**Answer:**
We implement double-booking protection at two complementary layers:
1. **Application Logic in Stored Procedure:** `sp_schedule_physio_session` executes a conflict check query within the transaction, counting existing active sessions where `(start_time <= p_start AND end_time > p_start)`. If count > 0, it raises a PL/pgSQL exception.
2. **Database Hard Constraint:** The `sessions` table enforces a composite unique constraint `UNIQUE(therapist_id, session_date, start_time)`, guaranteeing that even under simultaneous concurrent threads, PostgreSQL rejects the second insert with a unique key violation.

### Q5: What is the difference between JPQL and SQL?
**Answer:**
- **SQL (Structured Query Language):** Operates on database relational tables, columns, and foreign keys. It is database-dialect specific (e.g. PostgreSQL vs MySQL).
- **JPQL (Java Persistence Query Language):** Operates on JPA Java entity objects and their object-oriented properties (e.g. `SELECT s FROM SessionEntity s WHERE s.therapist.specialization = :spec`). The JPA provider translates JPQL into the appropriate native SQL dialect at runtime, ensuring portability and type safety.

### Q6: Why did you migrate from `javax.*` to `jakarta.*` packages?
**Answer:**
Oracle transferred Java EE to the Eclipse Foundation in 2017. Due to trademark agreements, the `javax.*` namespace was frozen. Starting with Jakarta EE 9 and continuing in Jakarta EE 10 (Tomcat 10.1, Spring 6, Spring Boot 3, Hibernate 6), all enterprise specifications were migrated to the `jakarta.*` namespace (`jakarta.servlet.*`, `jakarta.persistence.*`, `jakarta.validation.*`). Our project strictly uses the modern `jakarta.*` namespace.

### Q7: Why use Spring Boot's `@RestControllerAdvice` instead of standard try-catch in every controller?
**Answer:**
Writing try-catch blocks in every controller method leads to massive code duplication and inconsistent error payloads. `@RestControllerAdvice` acts as an Aspect-Oriented interceptor across all controllers. When an exception occurs (e.g. `ResourceNotFoundException`, `MethodArgumentNotValidException`), the advice catches it globally and transforms it into a standardized RFC 7807 JSON error body with timestamp, HTTP status, and specific error message.

### Q8: What is the N+1 select problem in ORM and how did you resolve it?
**Answer:**
The N+1 problem occurs when fetching N parent entities (e.g., 10 Treatment Plans) causes the ORM to execute 1 initial query for the plans, followed by N individual queries to fetch associated children (e.g., Sessions). We resolve this in JPA and Hibernate by:
1. Using `FetchType.LAZY` by default on all `@OneToMany` collections.
2. Using JPQL `JOIN FETCH` queries (e.g. `SELECT tp FROM TreatmentPlan tp JOIN FETCH tp.sessions`) to fetch parent and children in a single, optimized SQL query with an INNER JOIN.

---

## 📋 Live Demonstration Checklist

Before presenting to the examiner, ensure all services are running:

- [ ] **PostgreSQL 18 Running:** Port 5432, Database `physio_db` populated with seed data.
- [ ] **Tomcat 10.1 Running:** Port 8025 (`http://localhost:8025/web-portal/login`).
- [ ] **Spring Boot 3.3 Microservice Running:** Port 8080 (`http://localhost:8080/swagger-ui/index.html`).
- [ ] **Browser Bookmarks Ready:**
  1. Login Page: `http://localhost:8025/web-portal/`
  2. Doctor Queue: `http://localhost:8025/web-portal/doctor-dashboard`
  3. Patient Pain Curve: `http://localhost:8025/web-portal/patient-pain-analytics?patientId=1`
  4. Reception Invoices: `http://localhost:8025/web-portal/billing/invoices`
  5. Digital Tax Invoice: `http://localhost:8025/web-portal/billing/invoice-print?invoiceId=1`
  6. Admin Analytics: `http://localhost:8025/web-portal/admin-dashboard`
  7. Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- [ ] **Credentials Handout:**
  - Admin: `admin` / `Admin@123`
  - Doctor: `dr.sameer` / `Doctor@123`
  - Receptionist: `receptionist` / `Staff@123`
  - Patient: `rahul.v` / `Patient@123`
