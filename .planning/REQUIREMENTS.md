# System Requirements Specification: Physiotherapy Session Management

## 1. Functional Requirements (FR)

### Domain & Clinical Requirements
- **REQ-AUTH-01 (RBAC & Authentication)**: Support secure authentication for four roles: `ADMIN`, `THERAPIST`, `RECEPTIONIST`, and `PATIENT` with SHA-256 password hashing.
- **REQ-SCHED-02 (Slot Scheduling)**: Schedule appointments with conflict detection preventing double-booking of therapists and time slots.
- **REQ-ATTEND-03 (Attendance Tracking)**: Record session attendance states (`SCHEDULED`, `ATTENDED`, `CANCELLED`, `NO_SHOW`, `RESCHEDULED`) with reason tracking.
- **REQ-PLAN-04 (Treatment Plans)**: Create and track personalized rehabilitation plans containing diagnosis, target sessions, completed sessions, and status.
- **REQ-SOAP-05 (Digital SOAP Notes)**: Enable physiotherapists to record Subjective notes (pain scale 0-10), Objective metrics (Range of Motion degrees, Muscle strength), Assessment, and Plan after each session.
- **REQ-HEP-06 (Home Exercise Prescription)**: Prescribe home exercises with target muscle groups, sets, repetitions, and frequency.
- **REQ-BILL-07 (Packages & Billing)**: Support multi-session packages with automatic credit deduction upon session completion and invoice generation.
- **REQ-ANALYTICS-08 (Recovery Analytics)**: Compute patient recovery velocity, pain reduction trajectories, and clinic operational metrics.

### Technical & Academic Requirements
- **REQ-JDBC-09 (Task 1 JDBC Layer)**: Implement connection pooling via HikariCP, CRUD operations with `PreparedStatement`, batch processing for attendance updates, transactional atomic rollback, and `CallableStatement` invoking a stored procedure.
- **REQ-WEB-10 (Task 2 Servlet/JSP Portal)**: Deploy on Apache Tomcat 10.1 using Jakarta Servlet 6, session management with timeouts, role-based `AuthFilter`, Post-Redirect-Get (PRG) pattern, and responsive JSP/JSTL views.
- **REQ-SPRING-11 (Task 3 Spring MVC & JPA)**: Map entities with JPA 3.1, configure Spring IoC/DI, manage transactions declaratively, and apply JSR-380 `@Valid` form validation.
- **REQ-BOOT-12 (Task 4 REST Microservice)**: Build standalone Spring Boot 3.3 microservice with JSON REST controllers, `@RestControllerAdvice` error responses, OpenAPI Swagger UI, and Postman testing.
- **REQ-HIB-13 (Task 5 Hibernate Caching)**: Implement native Hibernate 6 `SessionFactory`, write complex analytical queries with HQL and Criteria API, and benchmark second-level cache (L2 Ehcache) performance.
- **REQ-DOC-14 (Report & Demo)**: Produce university project report (`50_PhysiotherapySessionManagement_AJP.docx`) matching the 10-chapter format with demo presentation guide.

---

## 2. Non-Functional Requirements (NFR)
- **NFR-SEC-01 (Security)**: All passwords hashed; unauthorized URLs blocked by RBAC filters with HTTP 403 / redirect to login.
- **NFR-DATA-02 (Data Integrity)**: Foreign key constraints on plans, sessions, notes, and exercises to prevent orphaned clinical records.
- **NFR-PERF-03 (Performance)**: Connection pooling keeps database latency under 15ms; L2 cache optimizes repeated therapist/exercise lookups.
- **NFR-UX-04 (Usability)**: Responsive, medical-themed CSS interface accessible across desktop and tablet screens.
