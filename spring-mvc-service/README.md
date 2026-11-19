# Spring 6 MVC & JPA 3.1 ORM Service

**Course:** 24MCAC302 - Advance Java Programming  
**Author:** ROSHAN NAMDEO BAGALE (Roll No: 50 | Div D | 24MCAC302)  
**Guide:** Dr. Netra Patil  
**Institution:** MIT College of Management & Computer Applications (COM&CA), MIT-ADT University, Pune  
**Project:** Physiotherapy Session Management & Clinical Rehabilitation System  

---

## 1. Overview & Syllabus Mapping (Unit 3)

This module implements **Task 3** of the course curriculum, building a Spring 6 Web MVC and JPA 3.1 Object-Relational Mapping (ORM) clinical management service running on **Apache Tomcat 10.1** with **Jakarta EE 10**:

| Syllabus Unit | Concept Demonstrated | Implementation in this Module |
| :--- | :--- | :--- |
| **Unit 3.1** | **JPA 3.1 Architecture & Entity Mapping** | `@Entity`, `@Table`, `@Id`, `@GeneratedValue(IDENTITY)`, `@Column`, `@ManyToOne(fetch = FetchType.EAGER)`, `@OneToMany`, `@OneToOne` in `com.physio.springmvc.model.*`. |
| **Unit 3.2.1** | **Spring MVC DispatcherServlet & Configuration** | Pure Java/Annotation-based Spring MVC setup via `WebMvcConfig.java` and `web.xml` mapped to `/`. |
| **Unit 3.2.2** | **Inversion of Control (IoC) & Dependency Injection (DI)** | `@Service`, `@Repository`, `@Controller` beans managed by Spring WebApplicationContext with clean **constructor-based dependency injection**. |
| **Unit 3.2.3** | **Spring MVC Controllers & Request Handlers** | `TreatmentPlanController` handling `@GetMapping`, `@PostMapping`, path variables (`@PathVariable`), and Model/View binding. |
| **Unit 3.2.4** | **JSR-380 Bean Validation** | Declarative `@Valid` with `BindingResult` on `TreatmentPlanForm` using Hibernate Validator 8 (`@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max`), rendered with Spring `<form:errors>`. |
| **Unit 3.2.5** | **Spring ORM & Declarative Transactions** | `LocalContainerEntityManagerFactoryBean`, `JpaTransactionManager`, `@EnableTransactionManagement`, and `@Transactional` service methods. |
| **Unit 3.3 & 3.4** | **JPQL (Java Persistence Query Language)** | Custom JPQL queries in `TreatmentPlanRepository` for joining patient/therapist entities (`JOIN FETCH`) and calculating aggregate clinic recovery statistics (`COUNT`, `SUM`). |

---

## 2. Architecture & Layer Directory Structure

```text
spring-mvc-service/
├── pom.xml                                   # Spring 6.1.14, Hibernate 6.5.2, Jakarta EE 10, HikariCP
├── src/main/
│   ├── java/com/physio/springmvc/
│   │   ├── config/
│   │   │   ├── JpaConfig.java                # HikariCP, EntityManagerFactory, JpaTransactionManager
│   │   │   └── WebMvcConfig.java             # ViewResolver, ResourceHandlers, ComponentScan
│   │   ├── controller/
│   │   │   └── TreatmentPlanController.java  # Spring MVC Controller handling /plans routes
│   │   ├── dto/
│   │   │   └── TreatmentPlanForm.java        # Form backing bean with JSR-380 validation annotations
│   │   ├── model/
│   │   │   ├── UserEntity.java               # JPA Entity for clinic users
│   │   │   ├── PatientEntity.java            # JPA Entity for patients
│   │   │   ├── TherapistEntity.java          # JPA Entity for physiotherapists
│   │   │   ├── TreatmentPlanEntity.java      # JPA Entity for rehabilitation plans
│   │   │   ├── SessionEntity.java            # JPA Entity for clinical appointments
│   │   │   └── SoapNoteEntity.java           # JPA Entity for SOAP progress notes
│   │   ├── repository/
│   │   │   ├── TreatmentPlanRepository.java  # JPA EntityManager & JPQL queries for plans
│   │   │   ├── PatientRepository.java        # JPA queries for patients
│   │   │   ├── TherapistRepository.java      # JPA queries for physiotherapists
│   │   │   └── SessionRepository.java        # JPA queries for sessions & notes
│   │   └── service/
│   │       └── TreatmentPlanService.java     # @Transactional business service & recovery KPI analytics
│   ├── resources/
│   │   └── application.properties            # PostgreSQL HikariCP & Hibernate dialect settings
│   └── webapp/
│       ├── css/
│       │   └── style.css                     # Responsive healthcare clinical UI styling
│       └── WEB-INF/
│           ├── web.xml                       # Deployment descriptor for Tomcat 10.1
│           └── views/
│               ├── plan-list.jsp             # JPQL-backed list of all treatment plans with progress bars
│               ├── plan-create.jsp           # Spring <form:form> with JSR-380 validation error display
│               ├── plan-details.jsp          # Deep view: patient, therapist, diagnosis & session timeline
│               └── recovery-report.jsp       # JPQL aggregated clinical KPI dashboard
```

---

## 3. How to Run & Verify in Eclipse IDE

### Step 1: Open Eclipse & Refresh Project Explorer
1. In Eclipse IDE, right-click `spring-mvc-service` in the **Project Explorer**.
2. Select **Maven ➔ Update Project...** (or press `Alt + F5`), check `spring-mvc-service`, and click **OK**.
3. Eclipse m2e will configure the classpath and dynamic web facet (`jst.web 6.0`).

### Step 2: Deploy to Apache Tomcat 10.1
1. Right-click `spring-mvc-service`.
2. Select **Run As ➔ Run on Server**.
3. Choose your configured **Apache Tomcat v10.1 at localhost** (Port 8025) and click **Finish**.
4. The Tomcat console will show HikariCP initialization:
   ```text
   HikariConfig - PhysioSpringHikariPool - Starting...
   HikariPool - PhysioSpringHikariPool - Added connection org.postgresql.jdbc.PgConnection@...
   DispatcherServlet 'dispatcher': initialization completed in ... ms
   ```

### Step 3: Test Web Endpoints in Chrome / Edge
Open your browser and navigate to:

1. **Treatment Plans List (JPQL Fetch):**  
   `http://localhost:8025/spring-mvc-service/plans`  
   *Displays all active and historical rehabilitation plans retrieved via JPQL `JOIN FETCH` queries, with visual session completion progress bars.*

2. **JSR-380 Form Validation Demo:**  
   `http://localhost:8025/spring-mvc-service/plans/new`  
   * **Test Error Handling:** Click **"Save & Persist Treatment Plan"** with empty fields. Spring MVC triggers JSR-380 validation, preventing database insertion and rendering inline error messages:
     * *"Patient selection is mandatory."*
     * *"Assigning a physiotherapist is mandatory."*
     * *"Clinical diagnosis cannot be empty."*
     * *"Rehabilitation goal is mandatory."*
     * *"Rehabilitation start date is required."*
   * **Test Valid Submission:** Fill out valid clinical details (e.g., Patient: *Amit Kulkarni*, Therapist: *Dr. Sameer Joshi*, Diagnosis: *Rotator Cuff Impingement Syndrome*, Goal: *Eliminate shoulder impingement and restore overhead abduction to 170 degrees*, Start Date: *Today*, Target Sessions: *12*).  
     Click Submit ➔ Redirects to `/plans` with green success banner: *"Treatment Plan #4 for patient Amit Kulkarni created successfully!"*

3. **Plan Timeline & Clinical Progress:**  
   `http://localhost:8025/spring-mvc-service/plans/1`  
   *Shows Patient Rahul Verma's lumbar disc rehabilitation plan, target vs completed sessions, and chronological session cards with before/after pain scores (7/10 ➔ 4/10) and ROM notes.*

4. **JPQL Recovery & Adherence Report:**  
   `http://localhost:8025/spring-mvc-service/plans/report`  
   *Displays aggregate KPI cards computed via JPQL (`COUNT`, `SUM`), total sessions delivered across the clinic, and overall rehabilitation adherence rate.*
