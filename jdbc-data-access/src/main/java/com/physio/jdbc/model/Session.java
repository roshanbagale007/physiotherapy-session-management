package com.physio.jdbc.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Model representing a Scheduled / Attended Physiotherapy Appointment Session.
 */
public class Session {
    private Long id;
    private Long planId;
    private Long therapistId;
    private String therapistName;
    private Long patientId;
    private String patientName;
    private Date sessionDate;
    private Time startTime;
    private Time endTime;
    private String status; // SCHEDULED, ATTENDED, CANCELLED, NO_SHOW, RESCHEDULED
    private String cancellationReason;
    private Long packageInvoiceId;
    private Timestamp createdAt;

    public Session() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public Long getTherapistId() { return therapistId; }
    public void setTherapistId(Long therapistId) { this.therapistId = therapistId; }

    public String getTherapistName() { return therapistName; }
    public void setTherapistName(String therapistName) { this.therapistName = therapistName; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Date getSessionDate() { return sessionDate; }
    public void setSessionDate(Date sessionDate) { this.sessionDate = sessionDate; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public Long getPackageInvoiceId() { return packageInvoiceId; }
    public void setPackageInvoiceId(Long packageInvoiceId) { this.packageInvoiceId = packageInvoiceId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("Session[ID=%d, Date=%s %s-%s, Patient='%s', Therapist='%s', Status=%s]",
                id, sessionDate, startTime, endTime, patientName, therapistName, status);
    }
}
