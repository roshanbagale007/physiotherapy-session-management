package com.physio.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SoapNoteRequest {

    @NotNull(message = "Pre-session pain score is mandatory.")
    @Min(value = 0, message = "VAS pain score minimum is 0.")
    @Max(value = 10, message = "VAS pain score maximum is 10.")
    private Integer painScoreBefore;

    @NotNull(message = "Post-session pain score is mandatory.")
    @Min(value = 0, message = "VAS pain score minimum is 0.")
    @Max(value = 10, message = "VAS pain score maximum is 10.")
    private Integer painScoreAfter;

    private String rangeOfMotionNotes;
    private String muscleStrengthGrade;

    @NotBlank(message = "Subjective notes (patient report) cannot be empty.")
    private String subjectiveNotes;

    @NotBlank(message = "Objective clinical findings cannot be empty.")
    private String objectiveFindings;

    @NotBlank(message = "Assessment summary cannot be empty.")
    private String assessmentSummary;

    @NotBlank(message = "Plan for next session is required.")
    private String planNextSession;

    private String modalitiesUsed;

    public SoapNoteRequest() {}

    public Integer getPainScoreBefore() { return painScoreBefore; }
    public void setPainScoreBefore(Integer painScoreBefore) { this.painScoreBefore = painScoreBefore; }

    public Integer getPainScoreAfter() { return painScoreAfter; }
    public void setPainScoreAfter(Integer painScoreAfter) { this.painScoreAfter = painScoreAfter; }

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
}
