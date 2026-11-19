# Physiotherapy Session Management System
**Course:** 24MCAC302 - Advance Java Programming  
**Programme:** Master of Computer Applications (MCA), Semester III (Batch 2025–27, Div D)  
**Institution:** MIT College of Management & Computer Applications (COM&CA), MIT-ADT University, Pune  
**Faculty Guide:** Dr. Netra Patil  
**Student:** ROSHAN NAMDEO BAGALE (Roll No: 50 | Enrollment: ADT25COMM0891)  

---

## 📌 Project Overview
The **Physiotherapy Session Management System** is a complete enterprise-grade healthcare management application designed to streamline rehabilitation scheduling, session attendance tracking, and individualized patient treatment plans.

This project implements the allocated domain across **all five modules** of the Advance Java Programming syllabus, demonstrating progressive software architectures from traditional JDBC to modern Spring Boot microservices:

1. **`sql/`**: Relational database schemas, mock data, and stored procedures.
2. **`task1-jdbc/`**: Data Access Layer implemented using raw **JDBC** with HikariCP connection pooling, `PreparedStatement`, `CallableStatement`, batch processing, and rollback transactions.
3. **`task2-web/`**: Web portal built with **Jakarta Servlets 6 & JSP** running on **Apache Tomcat 10.1**, featuring session management and security filters.
4. **`task3-spring-mvc/`**: Enterprise MVC architecture with **Spring Framework 6**, **JPA/Hibernate**, Inversion of Control (IoC), Dependency Injection (DI), and Bean Validation.
5. **`task4-boot/`**: RESTful Microservice built with **Spring Boot 3.3**, Spring Data JPA, centralized exception handling, and tested with Postman.
6. **`task5-hibernate/`**: Advanced persistence using core **Hibernate 6**, SessionFactory, HQL analytics, Criteria API, and second-level caching (Ehcache).
7. **`report/`**: Comprehensive project documentation following university guidelines.

---

## 🎯 Core Functionalities
- **Schedule Sessions:** Allocate appointments between patients and physiotherapists with conflict prevention and slot verification.
- **Record Attendance:** Track real-time session attendance (Attended, Cancelled, Rescheduled) with therapist notes and pre/post pain scale indices.
- **Track Treatment Plans:** Prescribe targeted exercises, monitor rehabilitation milestones, and evaluate patient recovery progress over time.

---

## 🛠️ Technology Stack
- **Language:** Java 21 LTS
- **Build Tool:** Apache Maven
- **Web Server:** Apache Tomcat 10.1 (Jakarta EE namespace)
- **Frameworks:** Spring Framework 6, Spring Boot 3.3, Hibernate 6 ORM
- **Database:** PostgreSQL / MySQL
- **Testing & Tools:** Postman, JUnit, Git, Eclipse IDE / Antigravity IDE

---

## 📂 Repository Structure
```
physiotherapy-session-management/
├── sql/                   # DDL schemas, sample data, and stored procedures
├── task1-jdbc/            # Task 1: JDBC Data Access Layer
├── task2-web/             # Task 2: Jakarta Servlets + JSP Web Portal
├── task3-spring-mvc/      # Task 3: JPA + Spring MVC Application
├── task4-boot/            # Task 4: Spring Boot RESTful Microservice
├── task5-hibernate/       # Task 5: Hibernate 6 Core & Caching Module
├── report/                # Mini-Project Report and Presentation
├── .gitignore             # Git ignore patterns
└── README.md              # Project documentation
```

---

## 👤 Author
- **Roshan Namdeo Bagale** (Roll No. 50, Div D)
- GitHub: [@roshanbagale007](https://github.com/roshanbagale007)
