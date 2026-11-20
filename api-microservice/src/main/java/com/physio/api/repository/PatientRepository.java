package com.physio.api.repository;

import com.physio.api.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p JOIN FETCH p.user u ORDER BY u.fullName ASC")
    List<Patient> findAllWithUsers();

    @Query("SELECT p FROM Patient p JOIN FETCH p.user u WHERE p.id = :id")
    Optional<Patient> findByIdWithUser(Long id);
}
