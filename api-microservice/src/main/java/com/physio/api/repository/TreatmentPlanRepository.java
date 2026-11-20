package com.physio.api.repository;

import com.physio.api.entity.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {

    @Query("SELECT tp FROM TreatmentPlan tp JOIN FETCH tp.patient p JOIN FETCH p.user pu JOIN FETCH tp.therapist t JOIN FETCH t.user tu ORDER BY tp.createdAt DESC")
    List<TreatmentPlan> findAllWithDetails();

    List<TreatmentPlan> findByPatientId(Long patientId);

    List<TreatmentPlan> findByStatus(String status);

    List<TreatmentPlan> findByPatientIdAndStatus(Long patientId, String status);

    long countByStatus(String status);
}
