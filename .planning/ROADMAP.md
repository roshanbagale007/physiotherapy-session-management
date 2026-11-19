# Roadmap: Physiotherapy Session Management System

## Overview
A comprehensive, 10-phase production-grade development roadmap implementing an end-to-end clinical rehabilitation management system across all 5 syllabus units of 24MCAC302 (Advance Java Programming). Each phase delivers modular, verifiable, production-ready code with complete academic compliance and freelance portfolio depth.

---

## Phases Overview

- [ ] **Phase 1: Domain Modeling & Enterprise Database Architecture** - 9-table relational schema, indexes, audit timestamps, stored procedures, and clinical mock data (`sql/`).
- [ ] **Phase 2: Task 1 — High-Performance JDBC Data Access Layer** - HikariCP pooling, CRUD DAOs, transaction rollback, batch attendance updates, and `DemoMain` test runner (`task1-jdbc/`).
- [ ] **Phase 3: Task 2 Core — Jakarta Servlet 6 & Multi-Role RBAC Foundation** - Tomcat 10.1 setup, `web.xml`, session management, `AuthFilter` security, and unified responsive clinical UI (`task2-web/`).
- [ ] **Phase 4: Task 2 Web Workflows — Receptionist, Doctor & Patient Clinical Portals** - Front-desk scheduling calendar, Doctor SOAP note submission, patient exercise tracking, and PRG pattern (`task2-web/`).
- [ ] **Phase 5: Task 3 — JPA 3.1 & Spring MVC Enterprise Architecture** - JPA entity mappings, Spring IoC/DI container, declarative `@Transactional` boundaries, and `@Valid` form validation (`task3-spring-mvc/`).
- [ ] **Phase 6: Task 4 Core — Cloud-Ready Spring Boot 3.3 REST Microservice** - Standalone Spring Boot 3.3 microservice, DTOs, mappers, REST controllers, and `@RestControllerAdvice` error handling (`task4-boot/`).
- [ ] **Phase 7: Task 4 Verification — Interactive Swagger UI & Postman Automated Test Suite** - Springdoc OpenAPI 3.0 documentation, Swagger UI integration, and Postman automated collection (`task4-boot/`).
- [ ] **Phase 8: Task 5 — Advanced Hibernate 6 Analytics & Second-Level Caching** - Hibernate 6 `SessionFactory`, complex HQL reports, Criteria API multi-filter queries, and Ehcache L2 cache benchmarking (`task5-hibernate/`).
- [ ] **Phase 9: Clinical Intelligence — Visual Pain Analytics, Invoicing & Audit Engine** - Chart.js pain reduction curves, session package deduction, PDF invoice rendering, and audit logging.
- [ ] **Phase 10: Academic Report, Demo Script & Portfolio Packaging** - Comprehensive university report document (`50_PhysiotherapySessionManagement_AJP.docx`), 10-min demo guide, and GitHub showcase polish (`report/`).

---

## Detailed Phase Breakdown

### Phase 1: Domain Modeling & Enterprise Database Architecture (`sql/`)
**Goal**: Establish a robust, normalized relational database modeling physical therapy clinical entities and workflows.  
**Depends on**: Nothing (First phase)  
**Requirements**: REQ-AUTH-01, REQ-SCHED-02, REQ-ATTEND-03, REQ-PLAN-04, REQ-SOAP-05, REQ-HEP-06, REQ-BILL-07  
**Success Criteria**:
  1. Complete 9-table schema (`users`, `therapists`, `patients`, `treatment_plans`, `exercises`, `plan_exercises`, `sessions`, `soap_notes`, `packages_invoices`) created with FK constraints and performance indexes.
  2. Realistic seed data loaded with 4 user roles, specialized therapists, distinct clinical cases (e.g. ACL rehab, Frozen shoulder), and standard exercise directory.
  3. Stored procedures (`sp_complete_session_and_deduct`, `sp_get_therapist_schedule`) execute without errors.
**Plans**: 2 plans (01-01 Schema & Procedures, 01-02 Seed Data & Verification).

---

### Phase 2: Task 1 — High-Performance JDBC Data Access Layer (`task1-jdbc/`)
**Goal**: Build a thread-safe, high-performance data access layer using raw JDBC satisfying all Unit 1 requirements.  
**Depends on**: Phase 1  
**Requirements**: REQ-JDBC-09  
**Success Criteria**:
  1. HikariCP connection pool configured and successfully leasing connections under 10ms.
  2. Multi-table transaction executes with automatic rollback on simulated error (session booking + package deduction).
  3. Batch processing demonstrates bulk session attendance updates with elapsed time benchmarking.
  4. Stored procedure invoked via `CallableStatement` with IN/OUT parameters.
  5. Standalone `DemoMain.java` executes cleanly from terminal and Eclipse, producing structured output.
**Plans**: 3 plans (02-01 Maven Setup & HikariCP Pool, 02-02 DAOs & Transaction Rollback, 02-03 Batch Processing & DemoMain).

---

### Phase 3: Task 2 Core — Jakarta Servlet 6 & Multi-Role RBAC Foundation (`task2-web/`)
**Goal**: Establish the web application architecture on Apache Tomcat 10.1 with role-based authentication and secure session management.  
**Depends on**: Phase 1, Phase 2  
**Requirements**: REQ-AUTH-01, REQ-WEB-10  
**Success Criteria**:
  1. Dynamic Web application builds to WAR and deploys to Tomcat 10.1 on port 8025.
  2. `LoginServlet` authenticates users against database credentials and sets up HTTP session with role attributes and timeout.
  3. `AuthFilter` intercepts requests to `/admin/*`, `/doctor/*`, `/desk/*`, and `/patient/*`, preventing unauthorized role access and session hijacking.
  4. Professional healthcare-themed responsive CSS layout with role-specific navigation bars.
**Plans**: 2 plans (03-01 Tomcat 10 WAR & Auth Architecture, 03-02 RBAC AuthFilter & Shared UI Layout).

---

### Phase 4: Task 2 Web Workflows — Receptionist, Doctor & Patient Clinical Portals (`task2-web/`)
**Goal**: Implement complete interactive web interfaces for scheduling, attendance marking, SOAP note recording, and patient tracking.  
**Depends on**: Phase 3  
**Requirements**: REQ-SCHED-02, REQ-ATTEND-03, REQ-SOAP-05, REQ-HEP-06, REQ-WEB-10  
**Success Criteria**:
  1. Front-Desk Receptionist can book appointments with instant conflict detection and mark real-time attendance.
  2. Doctor can record digital SOAP notes with pain scale sliders (0-10) and Range of Motion degrees.
  3. Patient can log in to view assigned rehabilitation plans and prescribed home exercises.
  4. All form submissions utilize the Post-Redirect-Get (PRG) pattern to eliminate duplicate submissions.
**Plans**: 3 plans (04-01 Receptionist Scheduling & Attendance, 04-02 Doctor SOAP & Treatment Engine, 04-03 Patient Portal & PRG).

---

### Phase 5: Task 3 — JPA 3.1 & Spring MVC Enterprise Architecture (`task3-spring-mvc/`)
**Goal**: Refactor the persistence and web layers into modern Spring 6 MVC and JPA 3.1 ORM with Inversion of Control.  
**Depends on**: Phase 1  
**Requirements**: REQ-SPRING-11  
**Success Criteria**:
  1. Domain entities mapped using JPA annotations (`@Entity`, `@OneToMany`, `@ManyToOne`, `@Enumerated`).
  2. Spring ApplicationContext configured with Constructor-based Dependency Injection (DI) across Controller, Service, and Repository layers.
  3. Complex clinical reports executed using clean JPQL queries.
  4. Server-side validation using JSR-380 Bean Validation (`@Valid`, `@Min`, `@Max`, `@NotBlank`) with field-level JSP error rendering.
**Plans**: 3 plans (05-01 JPA Entities & Persistence Config, 05-02 Spring IoC Service Layer, 05-03 Spring MVC Form Validation).

---

### Phase 6: Task 4 Core — Cloud-Ready Spring Boot 3.3 REST Microservice (`task4-boot/`)
**Goal**: Develop a standalone, cloud-native RESTful microservice with Spring Boot 3.3 for seamless mobile/web integration.  
**Depends on**: Phase 1  
**Requirements**: REQ-BOOT-12  
**Success Criteria**:
  1. Standalone Spring Boot 3.3 app boots with embedded Tomcat, Actuator health endpoints, and zero external XML configuration.
  2. RESTful endpoints for `/api/v1/sessions`, `/api/v1/patients`, `/api/v1/plans`, and `/api/v1/soap-notes` returning standardized JSON DTOs.
  3. Proper HTTP status code conventions: 200, 201, 204, 400, 404, 409 Conflict.
  4. Global exception handling via `@RestControllerAdvice` generating RFC 7807 compliant error payloads.
**Plans**: 2 plans (06-01 Spring Boot Setup & DTO Architecture, 06-02 REST Controllers & Global Exception Handler).

---

### Phase 7: Task 4 Verification — Interactive Swagger UI & Postman Automated Test Suite (`task4-boot/`)
**Goal**: Deliver interactive API documentation and automated verification tools expected of production APIs.  
**Depends on**: Phase 6  
**Requirements**: REQ-BOOT-12  
**Success Criteria**:
  1. Springdoc OpenAPI 3.0 configured with live Swagger UI available at `http://localhost:8080/swagger-ui.html`.
  2. Fully documented request/response schemas with sample values for clinical payloads.
  3. Postman collection (`physiotherapy-api.postman_collection.json`) with automated test assertions for all CRUD endpoints.
  4. Microservices architecture documentation explaining service decoupling and event-driven notifications.
**Plans**: 2 plans (07-01 Swagger OpenAPI 3.0 Integration, 07-02 Postman Automated Test Suite).

---

### Phase 8: Task 5 — Advanced Hibernate 6 Analytics & Second-Level Caching (`task5-hibernate/`)
**Goal**: Implement deep persistence management using core Hibernate 6, HQL, Criteria API, and Cache benchmarking.  
**Depends on**: Phase 1  
**Requirements**: REQ-HIB-13  
**Success Criteria**:
  1. Native `SessionFactory` and `HibernateUtil` configuration using `hibernate.cfg.xml`.
  2. Analytical reporting queries written in HQL (e.g. recovery completion rates, therapist workload).
  3. Type-safe multi-condition clinical searches built with the JPA/Hibernate Criteria API.
  4. Second-level cache (L2 Ehcache) benchmark demonstrating query cache hits, zero redundant SQL queries, and execution speedups.
**Plans**: 3 plans (08-01 SessionFactory & HQL Reports, 08-02 Dynamic Criteria API Engine, 08-03 L2 Cache Benchmarking).

---

### Phase 9: Clinical Intelligence — Visual Pain Analytics, Invoicing & Audit Engine
**Goal**: Integrate advanced portfolio-differentiating features that transform the project into a commercial healthcare SaaS.  
**Depends on**: Phase 4, Phase 6  
**Requirements**: REQ-BILL-07, REQ-ANALYTICS-08  
**Success Criteria**:
  1. Interactive Chart.js pain recovery curve showing visual trajectory of patient pain scores across visits.
  2. Automated package session deduction and printable digital PDF/HTML invoice generator.
  3. Clinical audit log tracking timestamps and therapist edits for patient notes.
  4. Executive Admin dashboard showing clinic KPI cards (revenue, utilization, attendance velocity).
**Plans**: 2 plans (09-01 Pain Analytics & Executive Dashboard, 09-02 Invoicing & Audit Engine).

---

### Phase 10: Academic Report, Demo Script & Portfolio Packaging (`report/`)
**Goal**: Finalize formal university documentation, demo video presentation package, and GitHub showcase.  
**Depends on**: Phase 1 through Phase 9  
**Requirements**: REQ-DOC-14  
**Success Criteria**:
  1. Complete Word document (`50_PhysiotherapySessionManagement_AJP.docx`) matching Dr. Netra Patil's sample report across all 10 chapters and Annexure A.
  2. 10-minute presentation slide outline and video demonstration checklist.
  3. GitHub repository finalized with architecture diagrams, badges, and quick-start instructions.
**Plans**: 2 plans (10-01 University Report Generation, 10-02 Demo Guide & GitHub Showcase).

---

## Progress Tracking

| Phase | Description | Plans Complete | Status |
| :---: | :--- | :---: | :---: |
| **1** | Domain Modeling & Database Architecture (`sql/`) | 0/2 | Pending |
| **2** | Task 1: High-Performance JDBC Data Access Layer | 0/3 | Pending |
| **3** | Task 2 Core: Servlet 6 & Multi-Role RBAC Foundation | 0/2 | Pending |
| **4** | Task 2 Workflows: Clinical Web Portals & Scheduling | 0/3 | Pending |
| **5** | Task 3: JPA 3.1 & Spring MVC 6 Architecture | 0/3 | Pending |
| **6** | Task 4 Core: Spring Boot 3.3 REST Microservice | 0/2 | Pending |
| **7** | Task 4 Verification: Swagger UI & Postman Suite | 0/2 | Pending |
| **8** | Task 5: Advanced Hibernate 6 Analytics & L2 Cache | 0/3 | Pending |
| **9** | Clinical Intelligence: Pain Analytics & Invoicing | 0/2 | Pending |
| **10** | Academic Report, Demo Script & Showcase Packaging | 0/2 | Pending |
