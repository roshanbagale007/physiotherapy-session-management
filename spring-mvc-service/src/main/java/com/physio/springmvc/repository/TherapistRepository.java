package com.physio.springmvc.repository;

import com.physio.springmvc.model.TherapistEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Unit 3.3 & 3.4: Therapist Repository using EntityManager and JPQL.
 */
@Repository
public class TherapistRepository {

    @PersistenceContext
    private EntityManager em;

    public List<TherapistEntity> findAll() {
        String jpql = "SELECT t FROM TherapistEntity t JOIN FETCH t.user u ORDER BY u.fullName ASC";
        return em.createQuery(jpql, TherapistEntity.class).getResultList();
    }

    public Optional<TherapistEntity> findById(Long id) {
        return Optional.ofNullable(em.find(TherapistEntity.class, id));
    }
}
