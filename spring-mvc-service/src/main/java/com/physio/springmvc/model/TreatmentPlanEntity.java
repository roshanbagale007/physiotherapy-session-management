package com.physio.springmvc.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit 3.2: JPA Entity Mapping with Relationships and Lifecycle Callbacks.
 */
@Entity
@Table(name = "treatment_plans")
public class TreatmentPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "therapist_id", nullable = false)
    private TherapistEntity therapist;

    @Column(nullable = false, length = 200)
    private String diagnosis;

    @Column(name = "treatment_goal", nullable = false, columnDefinition = "TEXT")
    private String treatmentGoal;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "estimated_end_date")
    private Date estimatedEndDate;

    @Column(name = "target_sessions", nullable = false)
    private int targetSessions;

    @Column(name = "completed_sessions")
    private int completedSessions = 0;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE, COMPLETED, PAUSED, DISCONTINUED

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @OneToMany(mappedBy = "treatmentPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SessionEntity> sessions = new ArrayList<>();

    public TreatmentPlanEntity() {}

    @PrePersist
    protected void onCreate() {
        if (status == null) status = "ACTIVE";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PatientEntity getPatient() { return patient; }
    public void setPatient(PatientEntity patient) { this.patient = patient; }

    public TherapistEntity getTherapist() { return therapist; }
    public void setTherapist(TherapistEntity therapist) { this.therapist = therapist; }

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

    public List<SessionEntity> getSessions() { return sessions; }
    public void setSessions(List<SessionEntity> sessions) { this.sessions = sessions; }
}
