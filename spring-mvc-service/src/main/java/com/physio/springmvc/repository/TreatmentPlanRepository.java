package com.physio.springmvc.repository;

import com.physio.springmvc.model.TreatmentPlanEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Unit 3.3 & 3.4: JPA Repository using EntityManager and JPQL Queries.
 */
@Repository
public class TreatmentPlanRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Unit 3.3: CRUD - Save (persist) a new treatment plan.
     */
    public TreatmentPlanEntity save(TreatmentPlanEntity plan) {
        if (plan.getId() == null) {
            em.persist(plan);
            return plan;
        } else {
            return em.merge(plan);
        }
    }

    /**
     * Unit 3.3: CRUD - Find by Primary Key.
     */
    public Optional<TreatmentPlanEntity> findById(Long id) {
        return Optional.ofNullable(em.find(TreatmentPlanEntity.class, id));
    }

    /**
     * Unit 3.4: JPQL Query - Find all active plans joined with Patient and Therapist.
     */
    public List<TreatmentPlanEntity> findAllWithDetails() {
        String jpql = """
            SELECT tp FROM TreatmentPlanEntity tp
            JOIN FETCH tp.patient p
            JOIN FETCH p.user pu
            JOIN FETCH tp.therapist t
            JOIN FETCH t.user tu
            ORDER BY tp.createdAt DESC
        """;
        return em.createQuery(jpql, TreatmentPlanEntity.class).getResultList();
    }

    /**
     * Unit 3.4: JPQL Query - Filter by Status.
     */
    public List<TreatmentPlanEntity> findByStatus(String status) {
        String jpql = "SELECT tp FROM TreatmentPlanEntity tp WHERE tp.status = :status ORDER BY tp.startDate DESC";
        return em.createQuery(jpql, TreatmentPlanEntity.class)
                 .setParameter("status", status)
                 .getResultList();
    }

    /**
     * Unit 3.4: JPQL Aggregate Query - Count active vs completed plans.
     */
    public Long countByStatus(String status) {
        String jpql = "SELECT COUNT(tp) FROM TreatmentPlanEntity tp WHERE tp.status = :status";
        return em.createQuery(jpql, Long.class)
                 .setParameter("status", status)
                 .getSingleResult();
    }
}
