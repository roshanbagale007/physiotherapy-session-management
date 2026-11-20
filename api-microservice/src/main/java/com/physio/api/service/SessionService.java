package com.physio.api.service;

import com.physio.api.dto.*;
import com.physio.api.entity.*;
import com.physio.api.exception.BadRequestException;
import com.physio.api.exception.ConflictException;
import com.physio.api.exception.ResourceNotFoundException;
import com.physio.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;
    private final TreatmentPlanRepository planRepository;
    private final PatientRepository patientRepository;
    private final TherapistRepository therapistRepository;
    private final SoapNoteRepository soapNoteRepository;
    private final PackageInvoiceRepository packageInvoiceRepository;

    @Autowired
    public SessionService(
            SessionRepository sessionRepository,
            TreatmentPlanRepository planRepository,
            PatientRepository patientRepository,
            TherapistRepository therapistRepository,
            SoapNoteRepository soapNoteRepository,
            PackageInvoiceRepository packageInvoiceRepository) {
        this.sessionRepository = sessionRepository;
        this.planRepository = planRepository;
        this.patientRepository = patientRepository;
        this.therapistRepository = therapistRepository;
        this.soapNoteRepository = soapNoteRepository;
        this.packageInvoiceRepository = packageInvoiceRepository;
    }

    public List<SessionResponse> getAllSessions(LocalDate date, String status, Long patientId, Long therapistId) {
        List<Session> list = sessionRepository.findAllWithDetails();

        return list.stream()
                .filter(s -> date == null || s.getSessionDate().equals(date))
                .filter(s -> status == null || s.getStatus().equalsIgnoreCase(status))
                .filter(s -> patientId == null || s.getPatient().getId().equals(patientId))
                .filter(s -> therapistId == null || s.getTherapist().getId().equals(therapistId))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SessionResponse getSessionById(Long id) {
        Session s = sessionRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Therapy session not found with ID: " + id));
        return mapToResponse(s);
    }

    @Transactional
    public SessionResponse bookSession(SessionBookRequest req) {
        TreatmentPlan plan = planRepository.findById(req.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found with ID: " + req.getPlanId()));

        Patient patient = patientRepository.findByIdWithUser(req.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + req.getPatientId()));

        Therapist therapist = therapistRepository.findByIdWithUser(req.getTherapistId())
                .orElseThrow(() -> new ResourceNotFoundException("Therapist not found with ID: " + req.getTherapistId()));

        LocalTime start = LocalTime.parse(req.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime end = start.plusMinutes(therapist.getSlotDurationMinutes());

        // Check for conflicting sessions with this therapist
        long conflicts = sessionRepository.countConflicts(therapist.getId(), req.getSessionDate(), start, end);
        if (conflicts > 0) {
            throw new ConflictException(String.format(
                    "Therapist %s is already booked on %s during %s - %s.",
                    therapist.getUser().getFullName(), req.getSessionDate(), start, end
            ));
        }

        // Deduct from package credit if available
        packageInvoiceRepository.findFirstActivePackage(patient.getId()).ifPresent(pkg -> {
            pkg.setRemainingSessions(pkg.getRemainingSessions() - 1);
            packageInvoiceRepository.save(pkg);
        });

        Session session = new Session();
        session.setTreatmentPlan(plan);
        session.setPatient(patient);
        session.setTherapist(therapist);
        session.setSessionDate(req.getSessionDate());
        session.setStartTime(start);
        session.setEndTime(end);
        session.setStatus("SCHEDULED");

        Session saved = sessionRepository.save(session);
        return mapToResponse(saved);
    }

    @Transactional
    public SessionResponse updateSessionStatus(Long id, SessionStatusUpdateRequest req) {
        Session session = sessionRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Therapy session not found with ID: " + id));

        String oldStatus = session.getStatus();
        String newStatus = req.getStatus().toUpperCase();

        session.setStatus(newStatus);
        if ("CANCELLED".equals(newStatus)) {
            session.setCancellationReason(req.getCancellationReason() != null ? req.getCancellationReason() : "Cancelled by desk/patient.");
        }

        // If status changed to ATTENDED, increment plan completed sessions
        if ("ATTENDED".equals(newStatus) && !"ATTENDED".equals(oldStatus)) {
            TreatmentPlan plan = session.getTreatmentPlan();
            plan.setCompletedSessions(plan.getCompletedSessions() + 1);
            if (plan.getCompletedSessions() >= plan.getTargetSessions()) {
                plan.setStatus("COMPLETED");
            }
            planRepository.save(plan);
        }

        return mapToResponse(sessionRepository.save(session));
    }

    @Transactional
    public SoapNoteResponse recordSoapNote(Long sessionId, SoapNoteRequest req) {
        Session session = sessionRepository.findByIdWithDetails(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Therapy session not found with ID: " + sessionId));

        SoapNote note = soapNoteRepository.findBySessionId(sessionId).orElseGet(SoapNote::new);
        note.setSession(session);
        note.setPainScoreBefore(req.getPainScoreBefore());
        note.setPainScoreAfter(req.getPainScoreAfter());
        note.setRangeOfMotionNotes(req.getRangeOfMotionNotes());
        note.setMuscleStrengthGrade(req.getMuscleStrengthGrade());
        note.setSubjectiveNotes(req.getSubjectiveNotes().trim());
        note.setObjectiveFindings(req.getObjectiveFindings().trim());
        note.setAssessmentSummary(req.getAssessmentSummary().trim());
        note.setPlanNextSession(req.getPlanNextSession().trim());
        note.setModalitiesUsed(req.getModalitiesUsed());

        SoapNote savedNote = soapNoteRepository.save(note);

        // Mark session as ATTENDED if not already
        if (!"ATTENDED".equalsIgnoreCase(session.getStatus())) {
            session.setStatus("ATTENDED");
            TreatmentPlan plan = session.getTreatmentPlan();
            plan.setCompletedSessions(plan.getCompletedSessions() + 1);
            if (plan.getCompletedSessions() >= plan.getTargetSessions()) {
                plan.setStatus("COMPLETED");
            }
            planRepository.save(plan);
            sessionRepository.save(session);
        }

        return mapSoapToResponse(savedNote);
    }

    private SessionResponse mapToResponse(Session s) {
        SessionResponse res = new SessionResponse();
        res.setId(s.getId());
        res.setPlanId(s.getTreatmentPlan().getId());
        res.setDiagnosis(s.getTreatmentPlan().getDiagnosis());
        res.setPatientId(s.getPatient().getId());
        res.setPatientName(s.getPatient().getUser().getFullName());
        res.setTherapistId(s.getTherapist().getId());
        res.setTherapistName(s.getTherapist().getUser().getFullName());
        res.setSessionDate(s.getSessionDate());
        res.setStartTime(s.getStartTime());
        res.setEndTime(s.getEndTime());
        res.setStatus(s.getStatus());
        res.setCancellationReason(s.getCancellationReason());

        if (s.getSoapNote() != null) {
            res.setSoapNote(mapSoapToResponse(s.getSoapNote()));
        }
        return res;
    }

    private SoapNoteResponse mapSoapToResponse(SoapNote n) {
        SoapNoteResponse res = new SoapNoteResponse();
        res.setId(n.getId());
        res.setSessionId(n.getSession().getId());
        res.setPainScoreBefore(n.getPainScoreBefore());
        res.setPainScoreAfter(n.getPainScoreAfter());
        res.setPainDelta(n.getPainScoreBefore() - n.getPainScoreAfter());
        res.setRangeOfMotionNotes(n.getRangeOfMotionNotes());
        res.setMuscleStrengthGrade(n.getMuscleStrengthGrade());
        res.setSubjectiveNotes(n.getSubjectiveNotes());
        res.setObjectiveFindings(n.getObjectiveFindings());
        res.setAssessmentSummary(n.getAssessmentSummary());
        res.setPlanNextSession(n.getPlanNextSession());
        res.setModalitiesUsed(n.getModalitiesUsed());
        res.setRecordedAt(n.getRecordedAt());
        return res;
    }
}
