package com.physio.api.service;

import com.physio.api.dto.TreatmentPlanRequest;
import com.physio.api.dto.TreatmentPlanResponse;
import com.physio.api.entity.Patient;
import com.physio.api.entity.Therapist;
import com.physio.api.entity.TreatmentPlan;
import com.physio.api.exception.BadRequestException;
import com.physio.api.exception.ResourceNotFoundException;
import com.physio.api.repository.PatientRepository;
import com.physio.api.repository.TherapistRepository;
import com.physio.api.repository.TreatmentPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TreatmentPlanService {

    private final TreatmentPlanRepository planRepository;
    private final PatientRepository patientRepository;
    private final TherapistRepository therapistRepository;

    @Autowired
    public TreatmentPlanService(
            TreatmentPlanRepository planRepository,
            PatientRepository patientRepository,
            TherapistRepository therapistRepository) {
        this.planRepository = planRepository;
        this.patientRepository = patientRepository;
        this.therapistRepository = therapistRepository;
    }

    public List<TreatmentPlanResponse> getAllPlans(String status, Long patientId) {
        List<TreatmentPlan> plans;
        if (patientId != null && status != null) {
            plans = planRepository.findByPatientIdAndStatus(patientId, status.toUpperCase());
        } else if (patientId != null) {
            plans = planRepository.findByPatientId(patientId);
        } else if (status != null) {
            plans = planRepository.findByStatus(status.toUpperCase());
        } else {
            plans = planRepository.findAllWithDetails();
        }

        return plans.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public TreatmentPlanResponse getPlanById(Long id) {
        TreatmentPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found with ID: " + id));
        return mapToResponse(plan);
    }

    @Transactional
    public TreatmentPlanResponse createPlan(TreatmentPlanRequest req) {
        if (req.getStartDate() != null && req.getEstimatedEndDate() != null) {
            if (req.getEstimatedEndDate().isBefore(req.getStartDate())) {
                throw new BadRequestException("Estimated end date cannot be before start date.");
            }
        }

        Patient patient = patientRepository.findByIdWithUser(req.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + req.getPatientId()));

        Therapist therapist = therapistRepository.findByIdWithUser(req.getTherapistId())
                .orElseThrow(() -> new ResourceNotFoundException("Therapist not found with ID: " + req.getTherapistId()));

        TreatmentPlan plan = new TreatmentPlan();
        plan.setPatient(patient);
        plan.setTherapist(therapist);
        plan.setDiagnosis(req.getDiagnosis().trim());
        plan.setTreatmentGoal(req.getTreatmentGoal().trim());
        plan.setStartDate(req.getStartDate());
        plan.setEstimatedEndDate(req.getEstimatedEndDate());
        plan.setTargetSessions(req.getTargetSessions());
        plan.setCompletedSessions(0);
        plan.setStatus("ACTIVE");

        return mapToResponse(planRepository.save(plan));
    }

    @Transactional
    public TreatmentPlanResponse updatePlanStatus(Long id, String status) {
        TreatmentPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found with ID: " + id));

        String upper = status.toUpperCase();
        if (!List.of("ACTIVE", "COMPLETED", "PAUSED", "DISCONTINUED").contains(upper)) {
            throw new BadRequestException("Invalid status: " + status + ". Allowed: ACTIVE, COMPLETED, PAUSED, DISCONTINUED.");
        }

        plan.setStatus(upper);
        return mapToResponse(planRepository.save(plan));
    }

    private TreatmentPlanResponse mapToResponse(TreatmentPlan p) {
        TreatmentPlanResponse res = new TreatmentPlanResponse();
        res.setId(p.getId());
        res.setPatientId(p.getPatient().getId());
        res.setPatientName(p.getPatient().getUser().getFullName());
        res.setTherapistId(p.getTherapist().getId());
        res.setTherapistName(p.getTherapist().getUser().getFullName());
        res.setTherapistSpecialization(p.getTherapist().getSpecialization());
        res.setDiagnosis(p.getDiagnosis());
        res.setTreatmentGoal(p.getTreatmentGoal());
        res.setStartDate(p.getStartDate());
        res.setEstimatedEndDate(p.getEstimatedEndDate());
        res.setTargetSessions(p.getTargetSessions());
        res.setCompletedSessions(p.getCompletedSessions());

        double adherence = p.getTargetSessions() > 0
                ? Math.round(((double) p.getCompletedSessions() / p.getTargetSessions()) * 1000.0) / 10.0
                : 0.0;
        res.setAdherencePercentage(adherence);

        res.setStatus(p.getStatus());
        res.setCreatedAt(p.getCreatedAt());
        return res;
    }
}
