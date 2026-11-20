# Spring Boot 3.3 REST Microservice

**Course:** 24MCAC302 - Advance Java Programming  
**Author:** ROSHAN NAMDEO BAGALE (Roll No: 50 | Div D | 24MCAC302)  
**Guide:** Dr. Netra Patil  
**Institution:** MIT College of Management & Computer Applications (COM&CA), MIT-ADT University, Pune  
**Project:** Physiotherapy Session Management & Clinical Rehabilitation System  

---

## 1. Overview & Syllabus Mapping (Unit 4)

This module implements **Task 4** of the MCA syllabus, building an enterprise-grade RESTful Microservice with **Spring Boot 3.3**, **Spring Data JPA**, and **Jackson JSON** running on an **embedded Apache Tomcat server** (Port `8080`):

| Syllabus Unit | Concept Demonstrated | Implementation in this Microservice |
| :--- | :--- | :--- |
| **Unit 4.1** | **Spring Boot Architecture & Starters** | `@SpringBootApplication`, `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, `spring-boot-starter-actuator`, auto-configuration. |
| **Unit 4.2.1** | **RESTful Controllers & Request Mappings** | `@RestController`, `@RequestMapping("/api/v1/*")`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@PatchMapping`. |
| **Unit 4.2.2** | **Request & Response Body Serialization** | `@RequestBody`, `@PathVariable`, `@RequestParam`, `ResponseEntity<T>`, standardized `ApiResponse<T>` JSON envelope. |
| **Unit 4.2.3** | **JSR-380 Bean Validation in REST** | Declarative `@Valid` on incoming request DTOs (`SessionBookRequest`, `SoapNoteRequest`, `TreatmentPlanRequest`). |
| **Unit 4.2.4** | **Centralized Exception Handling** | `@RestControllerAdvice` and `@ExceptionHandler` producing structured RFC 7807 error envelopes (`ErrorResponse`) for HTTP 400, 404, 409, 500. |
| **Unit 4.2.5** | **Spring Data JPA Repositories** | `JpaRepository<T, ID>` with custom JPQL queries, conflict checking, and derived queries (`SessionRepository`, `TreatmentPlanRepository`, etc.). |
| **Unit 4.3** | **Health & Metrics Monitoring** | Spring Boot Actuator endpoints at `/actuator/health` and `/actuator/info`. |

---

## 2. Microservice Architecture & Directory Structure

```text
api-microservice/
├── pom.xml                                     # Spring Boot 3.3.5, Spring Data JPA, Actuator, PostgreSQL
├── src/main/
│   ├── java/com/physio/api/
│   │   ├── ApiMicroserviceApplication.java     # @SpringBootApplication Main Class
│   │   ├── controller/
│   │   │   ├── SessionRestController.java      # REST endpoints for /api/v1/sessions
│   │   │   ├── TreatmentPlanRestController.java# REST endpoints for /api/v1/plans
│   │   │   ├── PatientRestController.java      # REST endpoints for /api/v1/patients
│   │   │   ├── TherapistRestController.java    # REST endpoints for /api/v1/therapists
│   │   │   └── AnalyticsRestController.java    # REST endpoints for /api/v1/analytics
│   │   ├── dto/
│   │   │   ├── ApiResponse.java                # Standard success response envelope
│   │   │   ├── ErrorResponse.java              # Structured error response envelope
│   │   │   ├── SessionBookRequest.java         # Booking DTO with slot validation
│   │   │   ├── SessionStatusUpdateRequest.java # Status update DTO
│   │   │   ├── SessionResponse.java            # Formatted session JSON representation
│   │   │   ├── SoapNoteRequest.java            # Clinical SOAP note DTO (pain scores, ROM)
│   │   │   ├── SoapNoteResponse.java           # Formatted clinical note with pain reduction delta
│   │   │   ├── TreatmentPlanRequest.java       # Plan creation DTO
│   │   │   ├── TreatmentPlanResponse.java      # Plan details with adherence percentage
│   │   │   ├── PatientSummaryResponse.java     # Patient profile DTO
│   │   │   ├── TherapistSummaryResponse.java   # Therapist DTO with availability slots
│   │   │   └── RecoveryAnalyticsResponse.java  # Clinic KPI metrics DTO
│   │   ├── entity/
│   │   │   ├── User.java                       # JPA Entity for clinic users
│   │   │   ├── Patient.java                    # JPA Entity for patients
│   │   │   ├── Therapist.java                  # JPA Entity for physiotherapists
│   │   │   ├── TreatmentPlan.java              # JPA Entity for treatment plans
│   │   │   ├── Session.java                    # JPA Entity for appointments
│   │   │   ├── SoapNote.java                   # JPA Entity for clinical notes
│   │   │   └── PackageInvoice.java             # JPA Entity for prepaid sessions
│   │   ├── exception/
│   │   │   ├── BadRequestException.java        # HTTP 400 Bad Request
│   │   │   ├── ResourceNotFoundException.java  # HTTP 404 Not Found
│   │   │   ├── ConflictException.java          # HTTP 409 Slot Conflict
│   │   │   └── GlobalRestExceptionHandler.java # @RestControllerAdvice centralized handler
│   │   ├── repository/
│   │   │   ├── UserRepository.java             # Spring Data JPA
│   │   │   ├── PatientRepository.java          # Spring Data JPA
│   │   │   ├── TherapistRepository.java        # Spring Data JPA
│   │   │   ├── TreatmentPlanRepository.java    # Spring Data JPA
│   │   │   ├── SessionRepository.java          # Spring Data JPA (conflict queries)
│   │   │   ├── SoapNoteRepository.java         # Spring Data JPA
│   │   │   └── PackageInvoiceRepository.java   # Spring Data JPA
│   │   └── service/
│   │       ├── SessionService.java             # Slot conflict checking & package credit deduction
│   │       ├── TreatmentPlanService.java       # Plan management & adherence tracking
│   │       ├── PatientService.java             # Patient directory
│   │       ├── TherapistService.java           # Therapist slots & availability calculator
│   │       └── AnalyticsService.java           # Recovery KPI aggregation
│   └── resources/
│       └── application.properties              # Port 8080, PostgreSQL & Actuator config
```

---

## 3. Complete REST API Reference Table

| Method | Endpoint | Description | Request Body | Response Status |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/sessions` | List sessions (filter by date, status, patientId, therapistId) | None | `200 OK` |
| `GET` | `/api/v1/sessions/{id}` | Get session details & SOAP note | None | `200 OK` / `404 Not Found` |
| `POST` | `/api/v1/sessions` | Book session with conflict checking & package deduction | `SessionBookRequest` JSON | `201 Created` / `409 Conflict` |
| `PUT` | `/api/v1/sessions/{id}/status` | Update session attendance (ATTENDED, CANCELLED) | `SessionStatusUpdateRequest` | `200 OK` / `404 Not Found` |
| `POST` | `/api/v1/sessions/{id}/soap-note` | Record clinical SOAP note (VAS pain, ROM) | `SoapNoteRequest` JSON | `201 Created` / `400 Bad Request` |
| `GET` | `/api/v1/plans` | List treatment plans (filter by status, patientId) | None | `200 OK` |
| `GET` | `/api/v1/plans/{id}` | Get single plan with adherence percentage | None | `200 OK` / `404 Not Found` |
| `POST` | `/api/v1/plans` | Create treatment plan with validation | `TreatmentPlanRequest` JSON | `201 Created` / `400 Bad Request` |
| `PATCH`| `/api/v1/plans/{id}/status` | Update plan status (ACTIVE, COMPLETED, PAUSED) | `{"status": "COMPLETED"}` | `200 OK` / `400 Bad Request` |
| `GET` | `/api/v1/patients` | List all registered patients | None | `200 OK` |
| `GET` | `/api/v1/patients/{id}` | Get patient medical profile | None | `200 OK` / `404 Not Found` |
| `GET` | `/api/v1/therapists` | List physiotherapists & fees | None | `200 OK` |
| `GET` | `/api/v1/therapists/{id}/availability?date=YYYY-MM-DD` | Dynamic available appointment slots | None | `200 OK` |
| `GET` | `/api/v1/analytics/recovery` | Clinic recovery KPI metrics & pain reduction | None | `200 OK` |
| `GET` | `/actuator/health` | Spring Boot Actuator health status | None | `200 OK` |

---

## 4. How to Run in Eclipse IDE

1. In Eclipse **Project Explorer**, right-click **`api-microservice`**.
2. Select **Maven ➔ Update Project...** (or press **`Alt + F5`**), make sure `api-microservice` is checked, and click **OK**.
3. Navigate to:  
   `api-microservice/src/main/java/com/physio/api/ApiMicroserviceApplication.java`
4. **Right-click** on `ApiMicroserviceApplication.java` ➔ **Run As ➔ Java Application** (or *Spring Boot App*).
5. The console will display the Spring Boot banner and embedded Tomcat startup:
   ```text
     .   ____          _            __ _ _
    /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
   ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
    \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
     '  |____| .__|_| |_|_| |_\__, | / / / /
    =========|_|==============|___/=/_/_/_/
    :: Spring Boot ::                (v3.3.5)

   [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
   [main] c.p.a.ApiMicroserviceApplication         : Started ApiMicroserviceApplication in ... seconds
   ```
