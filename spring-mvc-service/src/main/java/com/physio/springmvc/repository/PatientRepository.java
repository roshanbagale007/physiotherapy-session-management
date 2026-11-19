package com.physio.springmvc.repository;

import com.physio.springmvc.model.PatientEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Unit 3.3 & 3.4: Patient Repository using EntityManager and JPQL.
 */
@Repository
public class PatientRepository {

    @PersistenceContext
    private EntityManager em;

    public List<PatientEntity> findAll() {
        String jpql = "SELECT p FROM PatientEntity p JOIN FETCH p.user u ORDER BY u.fullName ASC";
        return em.createQuery(jpql, PatientEntity.class).getResultList();
    }

    public Optional<PatientEntity> findById(Long id) {
        return Optional.ofNullable(em.find(PatientEntity.class, id));
    }
}
