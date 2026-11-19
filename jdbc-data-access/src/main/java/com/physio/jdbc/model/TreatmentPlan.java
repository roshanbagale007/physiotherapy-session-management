package com.physio.jdbc.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model representing a Patient's prescribed Rehabilitation Treatment Plan.
 */
public class TreatmentPlan {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long therapistId;
    private String therapistName;
    private String diagnosis;
    private String treatmentGoal;
    private Date startDate;
    private Date estimatedEndDate;
    private int targetSessions;
    private int completedSessions;
    private String status; // ACTIVE, COMPLETED, PAUSED, DISCONTINUED
    private Timestamp createdAt;

    public TreatmentPlan() {}

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

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatmentGoal() { return treatmentGoal; }
    public void setTreatmentGoal(String treatmentGoal) { this.treatmentGoal = treatmentGoal; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEstimatedEndDate() { return estimatedEndDate; }
    public void setEstimatedEndDate(Date estimatedEndDate) { this.estimatedEndDate = estimatedEndDate; }

    public int getTargetSessions() { return targetSessions; }
    public void setTargetSessions(int targetSessions) { this.targetSessions = targetSessions; }

    public int getCompletedSessions() { return completedSessions; }
    public void setCompletedSessions(int completedSessions) { this.completedSessions = completedSessions; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("TreatmentPlan[ID=%d, Patient='%s', Diagnosis='%s', Progress=%d/%d sessions, Status=%s]",
                id, patientName, diagnosis, completedSessions, targetSessions, status);
    }
}
