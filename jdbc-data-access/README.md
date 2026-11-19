# Task 1: High-Performance JDBC Data Access Layer

**Module Directory:** `jdbc-data-access/`  
**Course:** 24MCAC302 - Advance Java Programming (Unit 1)  
**Student:** ROSHAN NAMDEO BAGALE (Roll No: 50, Div D)  
**Main Class:** `com.physio.jdbc.DemoMain`  

---

## 1. Syllabus Unit 1 Mapping

| Syllabus Unit 1 Topic | Implementation in `jdbc-data-access` |
| :--- | :--- |
| **1.1 - 1.3: JDBC Architecture & Connecting** | `DatabaseConnectionPool.java` managing database drivers (PostgreSQL / MySQL) and credentials via `db.properties`. |
| **1.4 & 1.7: CRUD & PreparedStatement** | `PatientDao.java` and `SessionDao.java` utilizing parameterized `PreparedStatement` to prevent SQL injection. |
| **1.5: Handling Transactions & Rollback** | `TransactionManager.java` managing atomic multi-step execution (`setAutoCommit(false)`, `commit()`, `rollback()`) with simulated failure test. |
| **1.6: Batch Processing** | `SessionDao.batchUpdateSessionStatus()` executing bulk session attendance updates via `addBatch()` and `executeBatch()`. |
| **1.7: CallableStatement** | `SessionDao.callCompleteSessionProcedure()` invoking stored procedure `sp_complete_session_and_deduct` with IN & OUT parameters. |
| **1.8: Connection Pooling** | `HikariCP` configured with max pool size 10, idle timeouts, and connection leasing under 10ms. |

---

## 2. How to Run

### Method 1: In Eclipse IDE (Recommended for College Lab)
1. Open Eclipse IDE.
2. Go to **File $\rightarrow$ Import... $\rightarrow$ Maven $\rightarrow$ Existing Maven Projects**.
3. Browse to `C:\Users\ROSHAN\eclipse-workspace\physiotherapy-session-management\jdbc-data-access`.
4. Click **Finish**.
5. In Project Explorer, navigate to:
   `src/main/java` $\rightarrow$ `com.physio.jdbc` $\rightarrow$ **`DemoMain.java`**.
6. Right-click **`DemoMain.java`** $\rightarrow$ **Run As** $\rightarrow$ **Java Application**.

### Method 2: Via Terminal / Maven
```bash
cd C:\Users\ROSHAN\eclipse-workspace\physiotherapy-session-management\jdbc-data-access
mvn compile exec:java
```

---

## 3. Expected Console Output

```text
============================================================================
  PHYSIOTHERAPY SESSION MANAGEMENT - TASK 1: JDBC DATA ACCESS DEMO
  Author: ROSHAN NAMDEO BAGALE | Roll No: 50 | Div D | 24MCAC302
============================================================================

--- [TEST 1] HikariCP Connection Pool (Unit 1.8) ---
>> Connection successfully leased from HikariCP in 8.412 ms (Valid: true)

--- [TEST 2] CRUD with PreparedStatement (Unit 1.4 & 1.7) ---
>> Total Registered Patients retrieved: 3
   - [1] Rahul Verma     | MALE   | Blood: B+  | Complaint: Lower back stiffness...
   - [2] Priya Patil     | FEMALE | Blood: O+  | Complaint: Post-Op Week 6: Left Knee ACL...
   - [3] Amit Kulkarni   | MALE   | Blood: A+  | Complaint: Severe right shoulder pain...
>> PreparedStatement UPDATE executed on Patient #1: SUCCESS

--- [TEST 3] Slot Availability & Conflict Check (Unit 1.7) ---
>> Therapist #1 availability on 2026-11-24 at 09:00:00: AVAILABLE

--- [TEST 4] Atomic Transaction with Commit (Unit 1.5) ---
>> [TRANSACTION SUCCESS]: Session #17 booked and package credit deducted.
>> Multi-statement transaction committed successfully: true

--- [TEST 5] Transaction Rollback on Injected Failure (Unit 1.5) ---
   [Step 1] Session statement executed in transaction buffer.
   [Step 2] SIMULATING EXCEPTION: Payment gateway timeout!
   [Catch Block] Caught simulated error: Simulated Payment Failure: Injected exception to test rollback.
   [Rollback] conn.rollback() successfully executed! Database remains clean.
>> Expected transaction outcome (Should be false due to rollback): false

--- [TEST 6] Batch Processing with Timing Benchmark (Unit 1.6) ---
>> Batch update executed for 2 sessions in 4 ms.
>> Rows affected per batch item: [1, 1]

--- [TEST 7] CallableStatement Executing Stored Procedure (Unit 1.7) ---
>> Stored Procedure OUT Parameter Result:
   "SUCCESS: Session marked ATTENDED, SOAP notes recorded, and plan updated."

============================================================================
  ALL TASK 1 (UNIT 1) JDBC VERIFICATION TESTS PASSED SUCCESSFULLY!
============================================================================
```
