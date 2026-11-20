package com.physio.api.repository;

import com.physio.api.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("""
        SELECT s FROM Session s
        JOIN FETCH s.patient p
        JOIN FETCH p.user pu
        JOIN FETCH s.therapist t
        JOIN FETCH t.user tu
        JOIN FETCH s.treatmentPlan tp
        LEFT JOIN FETCH s.soapNote sn
        ORDER BY s.sessionDate DESC, s.startTime DESC
    """)
    List<Session> findAllWithDetails();

    @Query("""
        SELECT s FROM Session s
        JOIN FETCH s.patient p
        JOIN FETCH p.user pu
        JOIN FETCH s.therapist t
        JOIN FETCH t.user tu
        JOIN FETCH s.treatmentPlan tp
        LEFT JOIN FETCH s.soapNote sn
        WHERE s.id = :id
    """)
    java.util.Optional<Session> findByIdWithDetails(@Param("id") Long id);

    List<Session> findBySessionDate(LocalDate sessionDate);

    List<Session> findByTherapistIdAndSessionDate(Long therapistId, LocalDate sessionDate);

    List<Session> findByPatientId(Long patientId);

    List<Session> findByTreatmentPlanId(Long treatmentPlanId);

    @Query("""
        SELECT COUNT(s) FROM Session s
        WHERE s.therapist.id = :therapistId
        AND s.sessionDate = :sessionDate
        AND s.status != 'CANCELLED'
        AND s.startTime < :endTime
        AND s.endTime > :startTime
    """)
    long countConflicts(
            @Param("therapistId") Long therapistId,
            @Param("sessionDate") LocalDate sessionDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    long countByStatus(String status);
}
