# Physiotherapy Session Management System
**Course:** 24MCAC302 - Advance Java Programming  
**Programme:** Master of Computer Applications (MCA), Semester III (Batch 2025–27, Div D)  
**Institution:** MIT College of Management & Computer Applications (COM&CA), MIT-ADT University, Pune  
**Faculty Guide:** Dr. Netra Patil  
**Student:** ROSHAN NAMDEO BAGALE (Roll No: 50 | Enrollment: ADT25COMM0891)  

---

## 📌 Project Overview
The **Physiotherapy Session Management System** is an enterprise-grade clinical management application designed to streamline rehabilitation scheduling, session attendance tracking, and individualized patient treatment plans.

This project implements the allocated domain across **all five modules** of the Advance Java Programming syllabus, structured as a modular enterprise architecture:

| Module Directory | Architecture & Technologies | Academic Unit Mapping |
| :--- | :--- | :--- |
| **`sql/`** | Relational schemas, ACID constraints, seed data, stored procedures | Relational Database Engine |
| **`jdbc-data-access/`** | Thread-safe DAO Layer with HikariCP pooling, transactions & batching | Task 1: JDBC |
| **`web-portal/`** | Jakarta Servlets 6, JSP, JSTL & RBAC security filters on Tomcat 10.1 | Task 2: Servlets & JSP |
| **`spring-mvc-service/`** | Spring 6 MVC, JPA 3.1 ORM, IoC/DI container & Bean validation | Task 3: JPA & Spring MVC |
| **`api-microservice/`** | Cloud-Native Spring Boot 3.3 REST API with Swagger & Postman tests | Task 4: Spring Boot REST |
| **`hibernate-analytics/`** | Core Hibernate 6, HQL, Criteria API & Ehcache L2 cache benchmarking | Task 5: Hibernate |
| **`report/`** | Comprehensive academic report (`.docx`) & presentation materials | Course Assessment |

---

## 🎯 Core Clinical Functionalities
- **Schedule Sessions:** Allocate appointments between patients and physiotherapists with conflict prevention and slot verification.
- **Record Attendance:** Track real-time session attendance (Attended, Cancelled, Rescheduled) with therapist notes and pre/post pain scale indices.
- **Track Treatment Plans:** Prescribe targeted exercises, monitor rehabilitation milestones, and evaluate patient recovery progress over time.
- **Clinical SOAP Notes:** Standardized physical therapy documentation (Subjective, Objective, Assessment, Plan) with Range of Motion (ROM) metrics.
- **Package Billing:** Manage multi-session therapy packages with automatic credit deductions and digital receipt generation.

---

## 🛠️ Technology Stack
- **Language:** Java 21 LTS
- **Build Tool:** Apache Maven
- **Web Server:** Apache Tomcat 10.1 (Jakarta EE namespace)
- **Frameworks:** Spring Framework 6, Spring Boot 3.3, Hibernate 6 ORM, HikariCP
- **Database:** PostgreSQL / MySQL
- **Testing & Tools:** Postman, Swagger OpenAPI 3.0, JUnit 5, Git, Eclipse IDE / Antigravity IDE

---

## 📂 Repository Structure
```
physiotherapy-session-management/
├── sql/                   # DDL schemas, sample clinical data, and stored procedures
├── jdbc-data-access/      # Task 1: High-Performance JDBC Data Access Layer
├── web-portal/            # Task 2: Jakarta Servlets 6 + JSP Multi-Role Web Portal
├── spring-mvc-service/    # Task 3: JPA 3.1 + Spring MVC Enterprise Application
├── api-microservice/      # Task 4: Spring Boot 3.3 RESTful Microservice
├── hibernate-analytics/   # Task 5: Hibernate 6 Core & Caching Module
├── report/                # Mini-Project Report and Presentation
├── .gitignore             # Git ignore patterns (protects internal planning & caches)
└── README.md              # Project documentation
```

---

## 👤 Author
- **Roshan Namdeo Bagale** (Roll No. 50, Div D)
- GitHub: [@roshanbagale007](https://github.com/roshanbagale007)
