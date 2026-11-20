package com.physio.api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class TreatmentPlanRequest {

    @NotNull(message = "Patient ID is required.")
    private Long patientId;

    @NotNull(message = "Therapist ID is required.")
    private Long therapistId;

    @NotBlank(message = "Diagnosis cannot be empty.")
    @Size(min = 5, max = 200, message = "Diagnosis must be between 5 and 200 characters.")
    private String diagnosis;

    @NotBlank(message = "Treatment goal cannot be empty.")
    @Size(min = 10, max = 500, message = "Treatment goal must be between 10 and 500 characters.")
    private String treatmentGoal;

    @NotNull(message = "Start date is mandatory.")
    private LocalDate startDate;

    private LocalDate estimatedEndDate;

    @NotNull(message = "Target sessions is mandatory.")
    @Min(value = 1, message = "Target sessions must be at least 1.")
    @Max(value = 60, message = "Target sessions cannot exceed 60.")
    private Integer targetSessions = 10;

    public TreatmentPlanRequest() {}

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getTherapistId() { return therapistId; }
    public void setTherapistId(Long therapistId) { this.therapistId = therapistId; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatmentGoal() { return treatmentGoal; }
    public void setTreatmentGoal(String treatmentGoal) { this.treatmentGoal = treatmentGoal; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEstimatedEndDate() { return estimatedEndDate; }
    public void setEstimatedEndDate(LocalDate estimatedEndDate) { this.estimatedEndDate = estimatedEndDate; }

    public Integer getTargetSessions() { return targetSessions; }
    public void setTargetSessions(Integer targetSessions) { this.targetSessions = targetSessions; }
}
