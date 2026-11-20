package com.physio.hibernate.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "soap_notes")
public class SoapNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private Session session;

    @Column(name = "pain_score_before", nullable = false)
    private int painScoreBefore;

    @Column(name = "pain_score_after", nullable = false)
    private int painScoreAfter;

    @Column(name = "range_of_motion_notes", length = 255)
    private String rangeOfMotionNotes;

    @Column(name = "muscle_strength_grade", length = 10)
    private String muscleStrengthGrade;

    @Column(name = "subjective_notes", nullable = false, columnDefinition = "TEXT")
    private String subjectiveNotes;

    @Column(name = "objective_findings", nullable = false, columnDefinition = "TEXT")
    private String objectiveFindings;

    @Column(name = "assessment_summary", nullable = false, columnDefinition = "TEXT")
    private String assessmentSummary;

    @Column(name = "plan_next_session", nullable = false, columnDefinition = "TEXT")
    private String planNextSession;

    @Column(name = "modalities_used", length = 255)
    private String modalitiesUsed;

    @Column(name = "recorded_at", insertable = false, updatable = false)
    private LocalDateTime recordedAt;

    public SoapNote() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }

    public int getPainScoreBefore() { return painScoreBefore; }
    public void setPainScoreBefore(int painScoreBefore) { this.painScoreBefore = painScoreBefore; }

    public int getPainScoreAfter() { return painScoreAfter; }
    public void setPainScoreAfter(int painScoreAfter) { this.painScoreAfter = painScoreAfter; }

    public String getRangeOfMotionNotes() { return rangeOfMotionNotes; }
    public void setRangeOfMotionNotes(String rangeOfMotionNotes) { this.rangeOfMotionNotes = rangeOfMotionNotes; }

    public String getMuscleStrengthGrade() { return muscleStrengthGrade; }
    public void setMuscleStrengthGrade(String muscleStrengthGrade) { this.muscleStrengthGrade = muscleStrengthGrade; }

    public String getSubjectiveNotes() { return subjectiveNotes; }
    public void setSubjectiveNotes(String subjectiveNotes) { this.subjectiveNotes = subjectiveNotes; }

    public String getObjectiveFindings() { return objectiveFindings; }
    public void setObjectiveFindings(String objectiveFindings) { this.objectiveFindings = objectiveFindings; }

    public String getAssessmentSummary() { return assessmentSummary; }
    public void setAssessmentSummary(String assessmentSummary) { this.assessmentSummary = assessmentSummary; }

    public String getPlanNextSession() { return planNextSession; }
    public void setPlanNextSession(String planNextSession) { this.planNextSession = planNextSession; }

    public String getModalitiesUsed() { return modalitiesUsed; }
    public void setModalitiesUsed(String modalitiesUsed) { this.modalitiesUsed = modalitiesUsed; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }

    @Override
    public String toString() {
        return String.format("SoapNote #%d: Pain %d/10 -> %d/10 (Delta: %d) | MMT: %s",
                id, painScoreBefore, painScoreAfter, (painScoreBefore - painScoreAfter), muscleStrengthGrade);
    }
}
