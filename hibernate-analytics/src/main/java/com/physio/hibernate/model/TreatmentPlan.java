package com.physio.hibernate.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "treatment_plans")
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "therapist_id", nullable = false)
    private Therapist therapist;

    @Column(nullable = false, length = 200)
    private String diagnosis;

    @Column(name = "treatment_goal", nullable = false, columnDefinition = "TEXT")
    private String treatmentGoal;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "estimated_end_date")
    private LocalDate estimatedEndDate;

    @Column(name = "target_sessions", nullable = false)
    private int targetSessions;

    @Column(name = "completed_sessions")
    private int completedSessions = 0;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "treatmentPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Session> sessions = new ArrayList<>();

    public TreatmentPlan() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Therapist getTherapist() { return therapist; }
    public void setTherapist(Therapist therapist) { this.therapist = therapist; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Session> getSessions() { return sessions; }
    public void setSessions(List<Session> sessions) { this.sessions = sessions; }

    @Override
    public String toString() {
        return String.format("TreatmentPlan #%d: [%s] Patient: %s | %d/%d Sessions (%s)",
                id, diagnosis, patient != null && patient.getUser() != null ? patient.getUser().getFullName() : "N/A",
                completedSessions, targetSessions, status);
    }
}
