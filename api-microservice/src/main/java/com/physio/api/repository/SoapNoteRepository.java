package com.physio.api.repository;

import com.physio.api.entity.SoapNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SoapNoteRepository extends JpaRepository<SoapNote, Long> {
    Optional<SoapNote> findBySessionId(Long sessionId);
}
