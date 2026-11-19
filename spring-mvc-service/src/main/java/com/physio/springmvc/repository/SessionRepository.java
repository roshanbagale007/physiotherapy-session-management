package com.physio.springmvc.repository;

import com.physio.springmvc.model.SessionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Unit 3.3 & 3.4: Session Repository for plan session tracking.
 */
@Repository
public class SessionRepository {

    @PersistenceContext
    private EntityManager em;

    public List<SessionEntity> findByPlanId(Long planId) {
        String jpql = """
            SELECT s FROM SessionEntity s
            JOIN FETCH s.patient p
            JOIN FETCH p.user pu
            JOIN FETCH s.therapist t
            JOIN FETCH t.user tu
            LEFT JOIN FETCH s.soapNote sn
            WHERE s.treatmentPlan.id = :planId
            ORDER BY s.sessionDate ASC, s.startTime ASC
        """;
        return em.createQuery(jpql, SessionEntity.class)
                 .setParameter("planId", planId)
                 .getResultList();
    }
}
