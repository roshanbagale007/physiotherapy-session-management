package com.physio.api.dto;

import java.util.Map;

public class RecoveryAnalyticsResponse {

    private long totalTreatmentPlans;
    private long activePlans;
    private long completedPlans;
    private int totalTargetSessions;
    private int totalCompletedSessions;
    private double overallAdherencePercentage;
    private double averagePainScoreReduction;
    private Map<String, Long> sessionsByStatus;

    public RecoveryAnalyticsResponse() {}

    public long getTotalTreatmentPlans() { return totalTreatmentPlans; }
    public void setTotalTreatmentPlans(long totalTreatmentPlans) { this.totalTreatmentPlans = totalTreatmentPlans; }

    public long getActivePlans() { return activePlans; }
    public void setActivePlans(long activePlans) { this.activePlans = activePlans; }

    public long getCompletedPlans() { return completedPlans; }
    public void setCompletedPlans(long completedPlans) { this.completedPlans = completedPlans; }

    public int getTotalTargetSessions() { return totalTargetSessions; }
    public void setTotalTargetSessions(int totalTargetSessions) { this.totalTargetSessions = totalTargetSessions; }

    public int getTotalCompletedSessions() { return totalCompletedSessions; }
    public void setTotalCompletedSessions(int totalCompletedSessions) { this.totalCompletedSessions = totalCompletedSessions; }

    public double getOverallAdherencePercentage() { return overallAdherencePercentage; }
    public void setOverallAdherencePercentage(double overallAdherencePercentage) { this.overallAdherencePercentage = overallAdherencePercentage; }

    public double getAveragePainScoreReduction() { return averagePainScoreReduction; }
    public void setAveragePainScoreReduction(double averagePainScoreReduction) { this.averagePainScoreReduction = averagePainScoreReduction; }

    public Map<String, Long> getSessionsByStatus() { return sessionsByStatus; }
    public void setSessionsByStatus(Map<String, Long> sessionsByStatus) { this.sessionsByStatus = sessionsByStatus; }
}
