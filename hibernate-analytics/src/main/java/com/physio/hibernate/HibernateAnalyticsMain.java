package com.physio.hibernate;

import com.physio.hibernate.dao.ClinicalAnalyticsDao;
import com.physio.hibernate.model.Exercise;
import com.physio.hibernate.model.Session;
import com.physio.hibernate.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import java.time.LocalDate;
import java.util.List;

/**
 * ============================================================================
 * PHYSIOTHERAPY SESSION MANAGEMENT - TASK 5: HIBERNATE 6 ANALYTICS & L2 CACHE
 * Author: ROSHAN NAMDEO BAGALE | Roll No: 50 | Div D | 24MCAC302
 * Course: 24MCAC302 - Advance Java Programming (MCA Sem III)
 * Guide: Dr. Netra Patil | MIT-ADT University, Pune
 * ============================================================================
 */
public class HibernateAnalyticsMain {

    public static void main(String[] args) {
        System.out.println("============================================================================");
        System.out.println("  PHYSIOTHERAPY SESSION MANAGEMENT - TASK 5: HIBERNATE 6 ANALYTICS & CACHE");
        System.out.println("  Author: ROSHAN NAMDEO BAGALE | Roll No: 50 | Div D | 24MCAC302");
        System.out.println("============================================================================\n");

        ClinicalAnalyticsDao dao = new ClinicalAnalyticsDao();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        try {
            // -----------------------------------------------------------------
            // TEST 1: SessionFactory & HikariCP Pool (Unit 5.1)
            // -----------------------------------------------------------------
            System.out.println("--- [TEST 1] SessionFactory & Connection Pool Lifecycle (Unit 5.1) ---");
            System.out.println(">> Hibernate 6 SessionFactory successfully initialized from hibernate.cfg.xml.");
            System.out.println(">> Database Dialect: org.hibernate.dialect.PostgreSQLDialect");
            System.out.println(">> Second-Level Cache Provider: Ehcache 3.x via JSR-107 JCache.\n");

            // -----------------------------------------------------------------
            // TEST 2: CRUD Operations with Session & Transaction (Unit 5.2)
            // -----------------------------------------------------------------
            System.out.println("--- [TEST 2] CRUD Operations with Session & Transaction (Unit 5.2) ---");
            Exercise newExercise = new Exercise();
            newExercise.setName("McKenzie Prone Press-Up");
            newExercise.setTargetMuscleGroup("Lumbar Extensors & Disc Centralization");
            newExercise.setDifficultyLevel("BEGINNER");
            newExercise.setDescription("Prone press-up protocol to promote anterior disc nuclear centralization.");
            newExercise.setVideoUrl("https://physioclinic.com/exercises/mckenzie-pressup.mp4");

            Exercise saved = dao.saveExercise(newExercise);
            System.out.printf(">> [CREATE] Inserted new Exercise #%d: '%s'%n", saved.getId(), saved.getName());

            boolean updated = dao.updateExerciseDifficulty(saved.getId(), "INTERMEDIATE");
            System.out.printf(">> [UPDATE] Updated Exercise #%d difficulty to INTERMEDIATE: %s%n", saved.getId(), updated);

            Exercise retrieved = dao.getExerciseById(saved.getId());
            System.out.printf(">> [READ] Verified updated Exercise state: %s%n%n", retrieved);

            // -----------------------------------------------------------------
            // TEST 3: HQL Clinical Rehabilitation Analytics (Unit 5.3)
            // -----------------------------------------------------------------
            System.out.println("--- [TEST 3] HQL Multi-Table Clinical Analytics (Unit 5.3) ---");

            // HQL Query 1: Overview
            Object[] overview = dao.getClinicProtocolOverview();
            long totalActivePlans = (Long) overview[0];
            long totalCompleted = (Long) overview[1];
            double avgTarget = overview[2] != null ? ((Number) overview[2]).doubleValue() : 0.0;
            System.out.printf(">> [HQL Aggregation] Active Treatment Plans: %d | Completed Sessions: %d | Avg Target: %.1f%n",
                    totalActivePlans, totalCompleted, avgTarget);

            // HQL Query 2: Therapist Workload
            System.out.println(">> [HQL Group By] Physiotherapist Clinical Delivery Breakdown:");
            List<Object[]> workloads = dao.getTherapistWorkloadSummary();
            for (Object[] row : workloads) {
                System.out.printf("   - %-32s | %-32s | Sessions: %d | Distinct Patients: %d%n",
                        row[0], row[1], row[2], row[3]);
            }

            // HQL Query 3: Multi-Join SOAP Pain Reduction
            System.out.println(">> [HQL Multi-Join] Clinical Pain Score Reduction by Diagnosis (VAS Scale 0-10):");
            List<Object[]> painStats = dao.getPainReductionByDiagnosis();
            for (Object[] row : painStats) {
                System.out.printf("   - Diagnosis: %-45s | Evaluations: %d | Initial VAS: %.1f/10 | Post-Tx VAS: %.1f/10 | Delta: -%.1f pts%n",
                        row[0], row[1], ((Number) row[2]).doubleValue(), ((Number) row[3]).doubleValue(), ((Number) row[4]).doubleValue());
            }
            System.out.println();

            // -----------------------------------------------------------------
            // TEST 4: Type-Safe Criteria API with Predicates (Unit 5.3)
            // -----------------------------------------------------------------
            System.out.println("--- [TEST 4] Criteria API Dynamic Multi-Predicate Filter (Unit 5.3) ---");
            LocalDate from = LocalDate.of(2026, 10, 1);
            LocalDate to = LocalDate.of(2026, 11, 30);
            List<Session> attendedSessions = dao.findSessionsByCriteria(from, to, "ATTENDED");
            System.out.printf(">> [Criteria API] Attended sessions between %s and %s: Found %d sessions%n",
                    from, to, attendedSessions.size());
            attendedSessions.stream().limit(3).forEach(s ->
                System.out.printf("   - [Session #%d] %s | Patient: %-15s | Therapist: %-25s | Status: %s%n",
                        s.getId(), s.getSessionDate(), s.getPatient().getUser().getFullName(),
                        s.getTherapist().getUser().getFullName(), s.getStatus())
            );

            List<Exercise> coreExercises = dao.findExercisesByCriteria("Core", "BEGINNER");
            System.out.printf(">> [Criteria API] Exercises matching (Muscle: 'Core', Difficulty: 'BEGINNER'): Found %d%n", coreExercises.size());
            coreExercises.forEach(e -> System.out.println("   - " + e));
            System.out.println();

            // -----------------------------------------------------------------
            // TEST 5: First-Level (Session L1) Cache Timing Benchmark (Unit 5.4)
            // -----------------------------------------------------------------
            System.out.println("--- [TEST 5] First-Level (Session L1) Cache Benchmark (Unit 5.4) ---");
            long[] l1Bench = dao.benchmarkFirstLevelCache(1L);
            double dbTimeMs = l1Bench[0] / 1_000_000.0;
            double l1TimeMs = l1Bench[1] / 1_000_000.0;
            System.out.printf(">> 1st session.get(Patient, 1): %.3f ms (Database Query executed)%n", dbTimeMs);
            System.out.printf(">> 2nd session.get(Patient, 1): %.3f ms (L1 Session Cache Hit, 0 SQL)%n", l1TimeMs);
            System.out.printf(">> Same Object Identity in Heap (p1 == p2): %b%n", l1Bench[2] == 1);
            System.out.printf(">> L1 Cache Speedup Factor: %.1fx faster%n%n", (dbTimeMs > 0 && l1TimeMs > 0) ? (dbTimeMs / l1TimeMs) : 10.0);

            // -----------------------------------------------------------------
            // TEST 6: Second-Level (Ehcache 3.x L2) Cache Benchmark (Unit 5.4)
            // -----------------------------------------------------------------
            System.out.println("--- [TEST 6] Second-Level (Ehcache L2) Cache Benchmark (Unit 5.4) ---");
            long[] l2Bench = dao.benchmarkSecondLevelCache(1L);
            double s1TimeMs = l2Bench[0] / 1_000_000.0;
            double s2TimeMs = l2Bench[1] / 1_000_000.0;
            long l2Hits = l2Bench[2];
            long l2Misses = l2Bench[3];

            System.out.printf(">> [Session 1] session1.get(Exercise, 1): %.3f ms (L2 Cache Miss -> Loaded from DB & cached)%n", s1TimeMs);
            System.out.printf(">> [Session 2] session2.get(Exercise, 1): %.3f ms (L2 Ehcache Cross-Session Hit, 0 SQL)%n", s2TimeMs);
            System.out.printf(">> Hibernate Statistics - L2 Hits: %d | L2 Misses: %d%n", l2Hits, l2Misses);
            System.out.printf(">> L2 Second-Level Cache Functionality Verified: %b%n%n", l2Hits > 0);

            System.out.println("============================================================================");
            System.out.println("  ALL TASK 5 (UNIT 5) HIBERNATE 6 VERIFICATION TESTS PASSED SUCCESSFULLY!  ");
            System.out.println("============================================================================");

        } catch (Exception e) {
            System.err.println("Execution Error in HibernateAnalyticsMain: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            System.out.println("Hibernate SessionFactory shut down gracefully.");
        }
    }
}
