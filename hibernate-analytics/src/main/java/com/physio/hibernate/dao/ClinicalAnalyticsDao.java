package com.physio.hibernate.dao;

import com.physio.hibernate.model.*;
import com.physio.hibernate.util.HibernateUtil;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit 5: Hibernate 6 Data Access & Analytics Engine.
 * Demonstrates:
 * - Session CRUD operations with ACID Transactions
 * - HQL Multi-table Join & Aggregate Queries
 * - Type-Safe Criteria API with dynamic Predicates
 * - First-Level (Session) Cache timing benchmark
 * - Second-Level (Ehcache 3) cross-session cache benchmark & hit statistics
 */
public class ClinicalAnalyticsDao {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    // =========================================================================
    // 1. Session CRUD Operations (Unit 5.2)
    // =========================================================================

    public Exercise saveExercise(Exercise exercise) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(exercise);
            tx.commit();
            return exercise;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public Exercise getExerciseById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Exercise.class, id);
        }
    }

    public boolean updateExerciseCategory(Long id, String newCategory) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Exercise ex = session.get(Exercise.class, id);
            if (ex != null) {
                ex.setCategory(newCategory);
                session.merge(ex);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // =========================================================================
    // 2. HQL Clinical Analytics Queries (Unit 5.3)
    // =========================================================================

    /**
     * HQL Aggregate Query: Total plans, sum of completed sessions, avg target sessions.
     */
    public Object[] getClinicProtocolOverview() {
        String hql = """
            SELECT COUNT(tp.id), SUM(tp.completedSessions), AVG(tp.targetSessions)
            FROM TreatmentPlan tp
            WHERE tp.status = 'ACTIVE'
        """;
        try (Session session = sessionFactory.openSession()) {
            return (Object[]) session.createQuery(hql, Object[].class).getSingleResult();
        }
    }

    /**
     * HQL Group By Query: Therapist workload, patient count, and attended session counts.
     */
    public List<Object[]> getTherapistWorkloadSummary() {
        String hql = """
            SELECT t.user.fullName, t.specialization, COUNT(s.id), COUNT(DISTINCT s.patient.id)
            FROM Session s
            JOIN s.therapist t
            WHERE s.status = 'ATTENDED'
            GROUP BY t.user.fullName, t.specialization
            ORDER BY COUNT(s.id) DESC
        """;
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(hql, Object[].class).getResultList();
        }
    }

    /**
     * HQL Multi-table Join Query: Average pain score reduction grouped by clinical diagnosis.
     */
    public List<Object[]> getPainReductionByDiagnosis() {
        String hql = """
            SELECT tp.diagnosis,
                   COUNT(sn.id),
                   AVG(sn.painScoreBefore),
                   AVG(sn.painScoreAfter),
                   AVG(sn.painScoreBefore - sn.painScoreAfter)
            FROM SoapNote sn
            JOIN sn.session s
            JOIN s.treatmentPlan tp
            GROUP BY tp.diagnosis
            ORDER BY AVG(sn.painScoreBefore - sn.painScoreAfter) DESC
        """;
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(hql, Object[].class).getResultList();
        }
    }

    // =========================================================================
    // 3. Type-Safe Criteria API Queries (Unit 5.3)
    // =========================================================================

    /**
     * Criteria API: Dynamic multi-predicate query filtering sessions by date range and status.
     */
    public List<com.physio.hibernate.model.Session> findSessionsByCriteria(LocalDate fromDate, LocalDate toDate, String status) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<com.physio.hibernate.model.Session> cq = cb.createQuery(com.physio.hibernate.model.Session.class);
            Root<com.physio.hibernate.model.Session> root = cq.from(com.physio.hibernate.model.Session.class);

            // Fetch join patient and therapist for clean projection
            root.fetch("patient", JoinType.INNER).fetch("user", JoinType.INNER);
            root.fetch("therapist", JoinType.INNER).fetch("user", JoinType.INNER);
            root.fetch("treatmentPlan", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();

            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("sessionDate"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("sessionDate"), toDate));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status.toUpperCase()));
            }

            cq.where(cb.and(predicates.toArray(new Predicate[0])));
            cq.orderBy(cb.desc(root.get("sessionDate")), cb.desc(root.get("startTime")));

            return session.createQuery(cq).getResultList();
        }
    }

    /**
     * Criteria API: Filter exercises by muscle group and category.
     */
    public List<Exercise> findExercisesByCriteria(String muscleGroup, String category) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Exercise> cq = cb.createQuery(Exercise.class);
            Root<Exercise> root = cq.from(Exercise.class);

            List<Predicate> predicates = new ArrayList<>();
            if (muscleGroup != null && !muscleGroup.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("targetMuscleGroup")), "%" + muscleGroup.toLowerCase() + "%"));
            }
            if (category != null && !category.isBlank()) {
                predicates.add(cb.equal(cb.upper(root.get("category")), category.toUpperCase()));
            }

            cq.where(cb.and(predicates.toArray(new Predicate[0])));
            return session.createQuery(cq).getResultList();
        }
    }

    // =========================================================================
    // 4. Caching Benchmarks: First-Level vs Second-Level (Unit 5.4)
    // =========================================================================

    /**
     * First-Level (L1) Session Cache Benchmark:
     * Within the SAME Session, multiple get() calls hit the L1 cache without issuing SQL.
     */
    public long[] benchmarkFirstLevelCache(Long patientId) {
        try (Session session = sessionFactory.openSession()) {
            // First Call: Hits Database
            long start1 = System.nanoTime();
            Patient p1 = session.get(Patient.class, patientId);
            long time1 = System.nanoTime() - start1;

            // Second Call: Hits First-Level Session Cache
            long start2 = System.nanoTime();
            Patient p2 = session.get(Patient.class, patientId);
            long time2 = System.nanoTime() - start2;

            boolean sameInstance = (p1 == p2);
            return new long[]{time1, time2, sameInstance ? 1 : 0};
        }
    }

    /**
     * Second-Level (L2) Ehcache Benchmark:
     * Across TWO INDEPENDENT Sessions, Session 2 fetches from L2 Cache with ZERO database queries.
     */
    public long[] benchmarkSecondLevelCache(Long exerciseId) {
        Statistics stats = sessionFactory.getStatistics();
        stats.clear();

        long hitCountBefore = stats.getSecondLevelCacheHitCount();
        long missCountBefore = stats.getSecondLevelCacheMissCount();

        // --- Session 1: Cache Miss -> Fetches from DB and puts into L2 Ehcache ---
        long start1 = System.nanoTime();
        try (Session session1 = sessionFactory.openSession()) {
            Exercise e1 = session1.get(Exercise.class, exerciseId);
        }
        long time1 = System.nanoTime() - start1;

        // --- Session 2: Cache Hit -> Fetches directly from L2 Ehcache across sessions ---
        long start2 = System.nanoTime();
        try (Session session2 = sessionFactory.openSession()) {
            Exercise e2 = session2.get(Exercise.class, exerciseId);
        }
        long time2 = System.nanoTime() - start2;

        long hits = stats.getSecondLevelCacheHitCount() - hitCountBefore;
        long misses = stats.getSecondLevelCacheMissCount() - missCountBefore;

        return new long[]{time1, time2, hits, misses};
    }
}
