package com.physio.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TreatmentPlanResponse {

    private Long id;
    private Long patientId;
    private String patientName;
    private Long therapistId;
    private String therapistName;
    private String therapistSpecialization;
    private String diagnosis;
    private String treatmentGoal;
    private LocalDate startDate;
    private LocalDate estimatedEndDate;
    private int targetSessions;
    private int completedSessions;
    private double adherencePercentage;
    private String status;
    private LocalDateTime createdAt;

    public TreatmentPlanResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Long getTherapistId() { return therapistId; }
    public void setTherapistId(Long therapistId) { this.therapistId = therapistId; }

    public String getTherapistName() { return therapistName; }
    public void setTherapistName(String therapistName) { this.therapistName = therapistName; }

    public String getTherapistSpecialization() { return therapistSpecialization; }
    public void setTherapistSpecialization(String therapistSpecialization) { this.therapistSpecialization = therapistSpecialization; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatmentGoal() { return treatmentGoal; }
    public void setTreatmentGoal(String treatmentGoal) { this.treatmentGoal = treatmentGoal; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEstimatedEndDate() { return estimatedEndDate; }
    public void setEstimatedEndDate(LocalDate estimatedEndDate) { this.estimatedEndDate = estimatedEndDate; }

    public int getTargetSessions() { return targetSessions; }
    public void setTargetSessions(int targetSessions) { this.targetSessions = targetSessions; }

    public int getCompletedSessions() { return completedSessions; }
    public void setCompletedSessions(int completedSessions) { this.completedSessions = completedSessions; }

    public double getAdherencePercentage() { return adherencePercentage; }
    public void setAdherencePercentage(double adherencePercentage) { this.adherencePercentage = adherencePercentage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
