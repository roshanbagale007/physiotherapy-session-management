package com.physio.springmvc.dto;

import jakarta.validation.constraints.*;
import java.sql.Date;

/**
 * Unit 3.2.4: Form Backing Bean with JSR-380 Bean Validation Annotations.
 */
public class TreatmentPlanForm {

    @NotNull(message = "Patient selection is mandatory.")
    private Long patientId;

    @NotNull(message = "Assigning a physiotherapist is mandatory.")
    private Long therapistId;

    @NotBlank(message = "Clinical diagnosis cannot be empty.")
    @Size(min = 5, max = 200, message = "Diagnosis must be between 5 and 200 characters.")
    private String diagnosis;

    @NotBlank(message = "Rehabilitation goal is mandatory.")
    @Size(min = 10, max = 500, message = "Goal description must be between 10 and 500 characters.")
    private String treatmentGoal;

    @NotNull(message = "Rehabilitation start date is required.")
    private Date startDate;

    private Date estimatedEndDate;

    @NotNull(message = "Target session count is required.")
    @Min(value = 1, message = "A treatment plan must have at least 1 scheduled session.")
    @Max(value = 60, message = "Target sessions cannot exceed 60 sessions per protocol.")
    private Integer targetSessions = 10;

    public TreatmentPlanForm() {}

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getTherapistId() { return therapistId; }
    public void setTherapistId(Long therapistId) { this.therapistId = therapistId; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatmentGoal() { return treatmentGoal; }
    public void setTreatmentGoal(String treatmentGoal) { this.treatmentGoal = treatmentGoal; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEstimatedEndDate() { return estimatedEndDate; }
    public void setEstimatedEndDate(Date estimatedEndDate) { this.estimatedEndDate = estimatedEndDate; }

    public Integer getTargetSessions() { return targetSessions; }
    public void setTargetSessions(Integer targetSessions) { this.targetSessions = targetSessions; }
}
