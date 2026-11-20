package com.physio.api.service;

import com.physio.api.dto.PatientSummaryResponse;
import com.physio.api.entity.Patient;
import com.physio.api.exception.ResourceNotFoundException;
import com.physio.api.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientSummaryResponse> getAllPatients() {
        return patientRepository.findAllWithUsers().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PatientSummaryResponse getPatientById(Long id) {
        Patient p = patientRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
        return mapToResponse(p);
    }

    private PatientSummaryResponse mapToResponse(Patient p) {
        PatientSummaryResponse res = new PatientSummaryResponse();
        res.setId(p.getId());
        res.setUserId(p.getUser().getId());
        res.setFullName(p.getUser().getFullName());
        res.setEmail(p.getUser().getEmail());
        res.setPhone(p.getUser().getPhone());
        res.setDateOfBirth(p.getDateOfBirth());
        res.setGender(p.getGender());
        res.setBloodGroup(p.getBloodGroup());
        res.setPrimaryComplaint(p.getPrimaryComplaint());
        res.setMedicalHistory(p.getMedicalHistory());
        res.setEmergencyContactName(p.getEmergencyContactName());
        res.setEmergencyContactPhone(p.getEmergencyContactPhone());
        res.setRegisteredAt(p.getRegisteredAt());
        return res;
    }
}
