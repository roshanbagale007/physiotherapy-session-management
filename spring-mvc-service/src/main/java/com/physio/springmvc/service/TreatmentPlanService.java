package com.physio.springmvc.service;

import com.physio.springmvc.dto.TreatmentPlanForm;
import com.physio.springmvc.model.PatientEntity;
import com.physio.springmvc.model.SessionEntity;
import com.physio.springmvc.model.TherapistEntity;
import com.physio.springmvc.model.TreatmentPlanEntity;
import com.physio.springmvc.repository.PatientRepository;
import com.physio.springmvc.repository.SessionRepository;
import com.physio.springmvc.repository.TherapistRepository;
import com.physio.springmvc.repository.TreatmentPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Unit 3.2.2 & 3.2.5: Spring Service Layer demonstrating Inversion of Control (IoC),
 * Constructor-based Dependency Injection (@Autowired), and Declarative Transactions (@Transactional).
 */
@Service
@Transactional(readOnly = true)
public class TreatmentPlanService {

    private final TreatmentPlanRepository planRepository;
    private final PatientRepository patientRepository;
    private final TherapistRepository therapistRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public TreatmentPlanService(TreatmentPlanRepository planRepository,
                                PatientRepository patientRepository,
                                TherapistRepository therapistRepository,
                                SessionRepository sessionRepository) {
        this.planRepository = planRepository;
        this.patientRepository = patientRepository;
        this.therapistRepository = therapistRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<TreatmentPlanEntity> getAllPlans() {
        return planRepository.findAllWithDetails();
    }

    public TreatmentPlanEntity getPlanById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Treatment plan not found with ID: " + id));
    }

    public List<SessionEntity> getSessionsForPlan(Long planId) {
        return sessionRepository.findByPlanId(planId);
    }

    public List<PatientEntity> getAllPatients() {
        return patientRepository.findAll();
    }

    public List<TherapistEntity> getAllTherapists() {
        return therapistRepository.findAll();
    }

    @Transactional
    public TreatmentPlanEntity createPlan(TreatmentPlanForm form) {
        PatientEntity patient = patientRepository.findById(form.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient selected: ID " + form.getPatientId()));

        TherapistEntity therapist = therapistRepository.findById(form.getTherapistId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid therapist selected: ID " + form.getTherapistId()));

        TreatmentPlanEntity plan = new TreatmentPlanEntity();
        plan.setPatient(patient);
        plan.setTherapist(therapist);
        plan.setDiagnosis(form.getDiagnosis().trim());
        plan.setTreatmentGoal(form.getTreatmentGoal().trim());
        plan.setStartDate(form.getStartDate());
        plan.setEstimatedEndDate(form.getEstimatedEndDate());
        plan.setTargetSessions(form.getTargetSessions());
        plan.setCompletedSessions(0);
        plan.setStatus("ACTIVE");

        return planRepository.save(plan);
    }

    /**
     * Unit 3.4: Aggregate Clinic Recovery and Adherence Statistics calculated using JPQL.
     */
    public Map<String, Object> getClinicRecoveryStats() {
        Map<String, Object> stats = new HashMap<>();

        List<TreatmentPlanEntity> allPlans = planRepository.findAllWithDetails();
        long totalPlans = allPlans.size();
        long activePlans = allPlans.stream().filter(p -> "ACTIVE".equalsIgnoreCase(p.getStatus())).count();
        long completedPlans = allPlans.stream().filter(p -> "COMPLETED".equalsIgnoreCase(p.getStatus())).count();

        int totalTargetSessions = allPlans.stream().mapToInt(TreatmentPlanEntity::getTargetSessions).sum();
        int totalCompletedSessions = allPlans.stream().mapToInt(TreatmentPlanEntity::getCompletedSessions).sum();

        double averageAdherence = totalTargetSessions > 0
                ? Math.round(((double) totalCompletedSessions / totalTargetSessions) * 1000.0) / 10.0
                : 0.0;

        stats.put("totalPlans", totalPlans);
        stats.put("activePlans", activePlans);
        stats.put("completedPlans", completedPlans);
        stats.put("totalTargetSessions", totalTargetSessions);
        stats.put("totalCompletedSessions", totalCompletedSessions);
        stats.put("averageAdherencePercent", averageAdherence);

        return stats;
    }
}
