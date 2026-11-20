# Hibernate 6 Architecture, HQL Analytics & Ehcache L2 Cache

**Course:** 24MCAC302 - Advance Java Programming  
**Author:** ROSHAN NAMDEO BAGALE (Roll No: 50 | Div D | 24MCAC302)  
**Guide:** Dr. Netra Patil  
**Institution:** MIT College of Management & Computer Applications (COM&CA), MIT-ADT University, Pune  
**Project:** Physiotherapy Session Management & Clinical Rehabilitation System  

---

## 1. Overview & Syllabus Mapping (Unit 5)

This module implements **Task 5** of the MCA syllabus, focusing on core **Hibernate 6 ORM architecture**, XML/Annotation configuration (`hibernate.cfg.xml`), **HQL (Hibernate Query Language)** multi-table clinical analytics, type-safe **Criteria API**, and **performance benchmarking of First-Level (L1) vs. Second-Level (Ehcache 3.x L2) Caching**:

| Syllabus Unit | Concept Demonstrated | Implementation in this Module |
| :--- | :--- | :--- |
| **Unit 5.1** | **Hibernate Architecture & Lifecycle** | `StandardServiceRegistry`, `MetadataSources`, singleton `SessionFactory` management, and short-lived `Session` lifecycle in `HibernateUtil.java`. |
| **Unit 5.2** | **Configuration & CRUD Operations** | `hibernate.cfg.xml` combined with JPA annotations (`@Entity`, `@Table`, `@Cacheable`), `session.persist()`, `session.get()`, `session.merge()` inside ACID `Transaction`. |
| **Unit 5.3** | **HQL (Hibernate Query Language)** | Aggregate queries (`COUNT`, `SUM`, `AVG`), multi-table joins (`JOIN`), `GROUP BY`, and `ORDER BY` analyzing clinic recovery, therapist workload, and pain reduction. |
| **Unit 5.3** | **Criteria API (Type-Safe Querying)** | `CriteriaBuilder`, `CriteriaQuery`, and dynamic multi-predicate filtering (`cb.and(...)`, `cb.between`, `cb.like`, `cb.equal`) for therapy appointments and exercises. |
| **Unit 5.4** | **First-Level (L1) Cache** | Within a single `Session`, repeated calls to `session.get()` retrieve entities directly from L1 memory (0 SQL statements, verifying reference equality `p1 == p2`). |
| **Unit 5.4** | **Second-Level (L2) Cache with Ehcache 3** | Cross-session caching using JSR-107 JCache and Ehcache 3.x. Session 2 fetches from L2 cache without hitting PostgreSQL, verified by `stats.getSecondLevelCacheHitCount()`. |

---

## 2. Directory Structure

```text
hibernate-analytics/
├── pom.xml                                         # Hibernate 6.5.2, Ehcache 3.10.8, HikariCP, PostgreSQL
├── src/main/
│   ├── java/com/physio/hibernate/
│   │   ├── HibernateAnalyticsMain.java             # Standalone verification runner (Tests 1 - 6)
│   │   ├── dao/
│   │   │   └── ClinicalAnalyticsDao.java           # CRUD, HQL analytics, Criteria API, Cache benchmarks
│   │   ├── model/
│   │   │   ├── User.java                           # Entity for users
│   │   │   ├── Therapist.java                      # @Cacheable therapist entity
│   │   │   ├── Patient.java                        # @Cacheable patient entity
│   │   │   ├── TreatmentPlan.java                  # Entity for rehabilitation plans
│   │   │   ├── Session.java                        # Entity for therapy appointments
│   │   │   ├── SoapNote.java                       # Entity for SOAP clinical notes
│   │   │   └── Exercise.java                       # @Cacheable exercise entity
│   │   └── util/
│   │       └── HibernateUtil.java                  # SessionFactory bootstrap & sequence synchronization
│   └── resources/
│       ├── hibernate.cfg.xml                       # Core Hibernate 6 & JCache / HikariCP settings
│       └── ehcache.xml                             # Ehcache 3.x heap & TTL cache policy
```

---

## 3. How to Run in Eclipse IDE

1. In Eclipse **Project Explorer**, right-click **`hibernate-analytics`**.
2. Select **Maven ➔ Update Project...** (or press **`Alt + F5`**), ensure `hibernate-analytics` is checked, and click **OK**.
3. Navigate to:  
   `hibernate-analytics/src/main/java/com/physio/hibernate/HibernateAnalyticsMain.java`
4. **Right-click** on `HibernateAnalyticsMain.java` ➔ **Run As ➔ Java Application**.
5. Watch the Console output verifying all 6 automated tests!

---

## 4. Expected Verification Console Output

```text
============================================================================
  PHYSIOTHERAPY SESSION MANAGEMENT - TASK 5: HIBERNATE 6 ANALYTICS & CACHE
  Author: ROSHAN NAMDEO BAGALE | Roll No: 50 | Div D | 24MCAC302
============================================================================

--- [TEST 1] SessionFactory & Connection Pool Lifecycle (Unit 5.1) ---
>> Hibernate 6 SessionFactory successfully initialized from hibernate.cfg.xml.
>> Database Dialect: org.hibernate.dialect.PostgreSQLDialect
>> Second-Level Cache Provider: Ehcache 3.x via JSR-107 JCache.

--- [TEST 2] CRUD Operations with Session & Transaction (Unit 5.2) ---
>> [CREATE] Inserted new Exercise #...: 'McKenzie Prone Press-Up'
>> [UPDATE] Updated Exercise #... difficulty to INTERMEDIATE: true
>> [READ] Verified updated Exercise state: Exercise #...: McKenzie Prone Press-Up [Lumbar Extensors | INTERMEDIATE]

--- [TEST 3] HQL Multi-Table Clinical Analytics (Unit 5.3) ---
>> [HQL Aggregation] Active Treatment Plans: 3 | Completed Sessions: 24 | Avg Target: 11.0
>> [HQL Group By] Physiotherapist Clinical Delivery Breakdown:
   - Dr. Sameer Deshmukh, MPT        | Orthopedic & Spine Rehabilitation | Sessions: 16 | Distinct Patients: 2
   - Dr. Ananya Sharma, MPT (Sports) | Sports Injury & Return-to-Play    | Sessions: 8  | Distinct Patients: 1
>> [HQL Multi-Join] Clinical Pain Score Reduction by Diagnosis (VAS Scale 0-10):
   - Diagnosis: Lumbar Disc Bulge (L4-L5) with Radiculopathy  | Evaluations: 5 | Initial VAS: 6.2/10 | Post-Tx VAS: 3.4/10 | Delta: -2.8 pts

--- [TEST 4] Criteria API Dynamic Multi-Predicate Filter (Unit 5.3) ---
>> [Criteria API] Attended sessions between 2026-10-01 and 2026-11-30: Found 15 sessions
>> [Criteria API] Exercises matching (Muscle: 'Core', Difficulty: 'BEGINNER'): Found 2

--- [TEST 5] First-Level (Session L1) Cache Benchmark (Unit 5.4) ---
>> 1st session.get(Patient, 1): 18.250 ms (Database Query executed)
>> 2nd session.get(Patient, 1): 0.045 ms (L1 Session Cache Hit, 0 SQL)
>> Same Object Identity in Heap (p1 == p2): true
>> L1 Cache Speedup Factor: 405.6x faster

--- [TEST 6] Second-Level (Ehcache L2) Cache Benchmark (Unit 5.4) ---
>> [Session 1] session1.get(Exercise, 1): 12.410 ms (L2 Cache Miss -> Loaded from DB & cached)
>> [Session 2] session2.get(Exercise, 1): 0.120 ms (L2 Ehcache Cross-Session Hit, 0 SQL)
>> Hibernate Statistics - L2 Hits: 1 | L2 Misses: 1
>> L2 Second-Level Cache Functionality Verified: true

============================================================================
  ALL TASK 5 (UNIT 5) HIBERNATE 6 VERIFICATION TESTS PASSED SUCCESSFULLY!  
============================================================================
Hibernate SessionFactory shut down gracefully.
```
