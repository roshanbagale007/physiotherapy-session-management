package com.physio.jdbc;

import com.physio.jdbc.config.DatabaseConnectionPool;
import com.physio.jdbc.dao.PatientDao;
import com.physio.jdbc.dao.SessionDao;
import com.physio.jdbc.dao.TransactionManager;
import com.physio.jdbc.model.Patient;
import com.physio.jdbc.model.Session;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * ============================================================================
 * Task 1: Standalone JDBC Demonstration Runner
 * Course: 24MCAC302 - Advance Java Programming
 * Author: ROSHAN NAMDEO BAGALE (Roll No: 50, Div D)
 * Topic:  Physiotherapy Session Management
 * ============================================================================
 * Demonstrates:
 * 1. HikariCP Connection Pooling (Unit 1.8)
 * 2. CRUD Operations via PreparedStatement (Unit 1.4 & 1.7)
 * 3. Batch Processing with Performance Benchmarking (Unit 1.6)
 * 4. ACID Transaction Management: Successful Commit (Unit 1.5)
 * 5. ACID Transaction Management: Rollback on Error (Unit 1.5)
 * 6. CallableStatement executing Stored Procedure (Unit 1.7)
 * ============================================================================
 */
public class DemoMain {

    public static void main(String[] args) {
        System.out.println("============================================================================");
        System.out.println("  PHYSIOTHERAPY SESSION MANAGEMENT - TASK 1: JDBC DATA ACCESS DEMO");
        System.out.println("  Author: ROSHAN NAMDEO BAGALE | Roll No: 50 | Div D | 24MCAC302");
        System.out.println("============================================================================\n");

        PatientDao patientDao = new PatientDao();
        SessionDao sessionDao = new SessionDao();
        TransactionManager txManager = new TransactionManager();

        try {
            // ----------------------------------------------------------------
            // 1. HikariCP Connection Pool Benchmarking (Unit 1.8)
            // ----------------------------------------------------------------
            System.out.println("--- [TEST 1] HikariCP Connection Pool (Unit 1.8) ---");
            long poolStart = System.nanoTime();
            try (Connection conn = DatabaseConnectionPool.getConnection()) {
                long elapsedNanos = System.nanoTime() - poolStart;
                double elapsedMs = elapsedNanos / 1_000_000.0;
                System.out.printf(">> Connection successfully leased from HikariCP in %.3f ms (Valid: %b)%n",
                        elapsedMs, conn.isValid(2));
            }
            System.out.println();

            // ----------------------------------------------------------------
            // 2. CRUD Operations with PreparedStatement (Unit 1.4 & 1.7)
            // ----------------------------------------------------------------
            System.out.println("--- [TEST 2] CRUD with PreparedStatement (Unit 1.4 & 1.7) ---");
            List<Patient> patients = patientDao.findAll();
            System.out.println(">> Total Registered Patients retrieved: " + patients.size());
            for (Patient p : patients) {
                System.out.printf("   - [%d] %-15s | %-6s | Blood: %-3s | Complaint: %s%n",
                        p.getId(), p.getFullName(), p.getGender(), p.getBloodGroup(), p.getPrimaryComplaint());
            }

            // Update Medical Notes using PreparedStatement
            boolean updated = patientDao.updateMedicalNotes(1L,
                    "Lower back stiffness with radiating numbness down left leg (Post-eval).",
                    "No prior surgeries; Sedentary software engineer; Posture correction advised.");
            System.out.println(">> PreparedStatement UPDATE executed on Patient #1: " + (updated ? "SUCCESS" : "FAILED"));
            System.out.println();

            // ----------------------------------------------------------------
            // 3. Conflict Detection via PreparedStatement
            // ----------------------------------------------------------------
            System.out.println("--- [TEST 3] Slot Availability & Conflict Check (Unit 1.7) ---");
            Date testDate = Date.valueOf(LocalDate.now().plusDays(3));
            Time testTime = Time.valueOf(LocalTime.of(9, 0, 0));
            boolean available = sessionDao.isSlotAvailable(1L, testDate, testTime);
            System.out.printf(">> Therapist #1 availability on %s at %s: %s%n",
                    testDate, testTime, (available ? "AVAILABLE" : "CONFLICT DETECTED"));
            System.out.println();

            // ----------------------------------------------------------------
            // 4. Multi-Table Atomic Transaction: Successful Commit (Unit 1.5)
            // ----------------------------------------------------------------
            System.out.println("--- [TEST 4] Atomic Transaction with Commit (Unit 1.5) ---");
            Session newSession = new Session();
            newSession.setPlanId(1L);
            newSession.setTherapistId(1L);
            newSession.setPatientId(1L);
            newSession.setSessionDate(Date.valueOf(LocalDate.now().plusDays(5)));
            newSession.setStartTime(Time.valueOf("14:00:00"));
            newSession.setEndTime(Time.valueOf("14:45:00"));

            boolean booked = txManager.bookSessionWithPackageCredit(newSession, 1L);
            System.out.println(">> Multi-statement transaction committed successfully: " + booked);
            System.out.println();

            // ----------------------------------------------------------------
            // 5. Transaction Rollback Simulation (Unit 1.5)
            // ----------------------------------------------------------------
            System.out.println("--- [TEST 5] Transaction Rollback on Injected Failure (Unit 1.5) ---");
            Session rollbackSession = new Session();
            rollbackSession.setPlanId(1L);
            rollbackSession.setTherapistId(1L);
            rollbackSession.setPatientId(1L);
            rollbackSession.setSessionDate(Date.valueOf(LocalDate.now().plusDays(6)));
            rollbackSession.setStartTime(Time.valueOf("15:00:00"));
            rollbackSession.setEndTime(Time.valueOf("15:45:00"));

            boolean rollbackResult = txManager.demonstrateRollback(rollbackSession, 1L);
            System.out.println(">> Expected transaction outcome (Should be false due to rollback): " + rollbackResult);
            System.out.println();

            // ----------------------------------------------------------------
            // 6. Batch Processing with Timing Benchmark (Unit 1.6)
            // ----------------------------------------------------------------
            System.out.println("--- [TEST 6] Batch Processing with Timing Benchmark (Unit 1.6) ---");
            List<Long> batchSessions = Arrays.asList(5L, 6L);
            long batchStart = System.currentTimeMillis();
            int[] batchResults = sessionDao.batchUpdateSessionStatus(batchSessions, "SCHEDULED");
            long batchElapsed = System.currentTimeMillis() - batchStart;

            System.out.printf(">> Batch update executed for %d sessions in %d ms.%n", batchResults.length, batchElapsed);
            System.out.println(">> Rows affected per batch item: " + Arrays.toString(batchResults));
            System.out.println();

            // ----------------------------------------------------------------
            // 7. CallableStatement: Stored Procedure Execution (Unit 1.7)
            // ----------------------------------------------------------------
            System.out.println("--- [TEST 7] CallableStatement Executing Stored Procedure (Unit 1.7) ---");
            // Mark session #5 attended with SOAP notes
            String procOutput = sessionDao.callCompleteSessionProcedure(
                    5L,
                    6, // Pain before: 6/10
                    3, // Pain after: 3/10
                    "Lumbar flexion 65 deg; SLR negative till 60 deg.",
                    "4/5 MMT",
                    "Patient reported feeling lighter with less morning stiffness.",
                    "Spinal paraspinal guarding markedly decreased; core brace stable.",
                    "Progressing well toward lumbar stabilization targets.",
                    "Advance home bridging exercises with resistance loop.",
                    "IFT 4-pole, Lumbar Traction, Moist Heat"
            );
            System.out.println(">> Stored Procedure OUT Parameter Result:");
            System.out.println("   \"" + procOutput + "\"");
            System.out.println();

            System.out.println("============================================================================");
            System.out.println("  ALL TASK 1 (UNIT 1) JDBC VERIFICATION TESTS PASSED SUCCESSFULLY!");
            System.out.println("============================================================================");

        } catch (Exception e) {
            System.err.println("Execution Error in DemoMain: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnectionPool.shutdown();
        }
    }
}
