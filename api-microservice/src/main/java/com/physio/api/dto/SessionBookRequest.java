package com.physio.api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class SessionBookRequest {

    @NotNull(message = "Plan ID is required.")
    private Long planId;

    @NotNull(message = "Therapist ID is required.")
    private Long therapistId;

    @NotNull(message = "Patient ID is required.")
    private Long patientId;

    @NotNull(message = "Session date is required.")
    @FutureOrPresent(message = "Session date cannot be in the past.")
    private LocalDate sessionDate;

    @NotNull(message = "Start time is required (HH:mm).")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Start time must be in HH:mm format.")
    private String startTime;

    public SessionBookRequest() {}

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public Long getTherapistId() { return therapistId; }
    public void setTherapistId(Long therapistId) { this.therapistId = therapistId; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
}
