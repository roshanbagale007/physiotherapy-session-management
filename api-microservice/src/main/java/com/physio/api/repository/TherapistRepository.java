package com.physio.api.repository;

import com.physio.api.entity.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TherapistRepository extends JpaRepository<Therapist, Long> {

    @Query("SELECT t FROM Therapist t JOIN FETCH t.user u ORDER BY u.fullName ASC")
    List<Therapist> findAllWithUsers();

    @Query("SELECT t FROM Therapist t JOIN FETCH t.user u WHERE t.id = :id")
    Optional<Therapist> findByIdWithUser(Long id);
}
