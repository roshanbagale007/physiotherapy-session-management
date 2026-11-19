# Physiotherapy Session Management System
**Course:** 24MCAC302 - Advance Java Programming  
**Institution:** MIT College of Management & Computer Applications (COM&CA), MIT-ADT University, Pune  
**Student:** ROSHAN NAMDEO BAGALE (Roll No: 50 | Enrollment: ADT25COMM0891 | Div D)  
**Faculty Guide:** Dr. Netra Patil  
**Target Submission / Milestone:** Continuous Assessment Component (Internal Marks) & Freelance Portfolio  

---

## 1. Executive Summary & Vision
The **Physiotherapy Session Management System** is a production-grade healthcare clinical software platform designed to manage end-to-end rehabilitation workflows for physical therapy clinics. It bridges traditional clinical record-keeping with modern enterprise web architectures.

Beyond standard appointment booking, the platform models realistic physical therapy clinical operations:
- Multi-role access (Admin, Physiotherapist/Doctor, Receptionist/Front Desk, Patient).
- Intelligent conflict-free scheduling and slot allocation.
- Digital SOAP clinical progress notes (Subjective, Objective, Assessment, Plan).
- Physical measurement tracking (VAS Pain scale 0-10, Range of Motion degrees, Muscle strength grading).
- Home Exercise Program (HEP) prescription with adherence monitoring.
- Rehabilitation package billing and automatic session deduction.
- Visual recovery analytics and operational clinic reporting.

---

## 2. Academic Alignment: The 5-Task Syllabus Matrix
To fulfill and exceed the criteria defined by Dr. Netra Patil for course 24MCAC302, the domain is implemented across 5 progressive technological paradigms:
1. **Task 1 (JDBC)**: Raw data-access layer with HikariCP connection pooling, `PreparedStatement`, `CallableStatement`, batch processing, and rollback transactions.
2. **Task 2 (Servlets & JSP)**: Multi-role web portal on Apache Tomcat 10.1 using Jakarta EE 10 (Servlet 6, JSP, JSTL, session management, and RBAC filters).
3. **Task 3 (Spring MVC & JPA)**: Enterprise architecture leveraging Spring Framework 6, Inversion of Control (IoC), Dependency Injection (DI), JPA 3.1 entity relations, and `@Valid` form validation.
4. **Task 4 (Spring Boot REST Microservice)**: Cloud-native REST API with Spring Boot 3.3, OpenAPI / Swagger interactive documentation, centralized error handling, and a Postman test collection.
5. **Task 5 (Hibernate Core & Caching)**: Advanced persistence layer using Hibernate 6 `SessionFactory`, analytical queries in HQL and Criteria API, and second-level cache (Ehcache) benchmarking.

---

## 3. Technology Stack
- **Language & Runtime:** Java 21 LTS
- **Build Tool:** Apache Maven
- **Servlet Container:** Apache Tomcat 10.1 (Jakarta EE namespace)
- **Database:** PostgreSQL 18 / MySQL 8
- **Core Frameworks:** Spring Framework 6, Spring Boot 3.3, Hibernate 6 ORM, HikariCP
- **Frontend / UI:** JSP, JSTL, HTML5, CSS3, JavaScript (Chart.js for recovery curves)
- **Testing & API Documentation:** Postman, Swagger / OpenAPI 3.0, JUnit 5
- **Version Control:** Git, GitHub (`roshanbagale007/physiotherapy-session-management`)
