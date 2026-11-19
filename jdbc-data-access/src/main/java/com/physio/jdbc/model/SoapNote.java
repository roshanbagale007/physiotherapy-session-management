package com.physio.jdbc.model;

import java.sql.Timestamp;

/**
 * Model representing clinical SOAP Notes (Subjective, Objective, Assessment, Plan).
 */
public class SoapNote {
    private Long id;
    private Long sessionId;
    private int painScoreBefore; // 0 to 10 VAS scale
    private int painScoreAfter;  // 0 to 10 VAS scale
    private String rangeOfMotionNotes;
    private String muscleStrengthGrade;
    private String subjectiveNotes;
    private String objectiveFindings;
    private String assessmentSummary;
    private String planNextSession;
    private String modalitiesUsed;
    private Timestamp recordedAt;

    public SoapNote() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

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

    public Timestamp getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Timestamp recordedAt) { this.recordedAt = recordedAt; }

    @Override
    public String toString() {
        return String.format("SoapNote[ID=%d, SessionID=%d, Pain: %d -> %d/10, ROM='%s', Modalities='%s']",
                id, sessionId, painScoreBefore, painScoreAfter, rangeOfMotionNotes, modalitiesUsed);
    }
}
