package com.physio.api.service;

import com.physio.api.dto.RecoveryAnalyticsResponse;
import com.physio.api.entity.SoapNote;
import com.physio.api.entity.TreatmentPlan;
import com.physio.api.repository.SessionRepository;
import com.physio.api.repository.SoapNoteRepository;
import com.physio.api.repository.TreatmentPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private final TreatmentPlanRepository planRepository;
    private final SessionRepository sessionRepository;
    private final SoapNoteRepository soapNoteRepository;

    @Autowired
    public AnalyticsService(
            TreatmentPlanRepository planRepository,
            SessionRepository sessionRepository,
            SoapNoteRepository soapNoteRepository) {
        this.planRepository = planRepository;
        this.sessionRepository = sessionRepository;
        this.soapNoteRepository = soapNoteRepository;
    }

    public RecoveryAnalyticsResponse getRecoveryAnalytics() {
        List<TreatmentPlan> plans = planRepository.findAllWithDetails();
        long totalPlans = plans.size();
        long activePlans = plans.stream().filter(p -> "ACTIVE".equalsIgnoreCase(p.getStatus())).count();
        long completedPlans = plans.stream().filter(p -> "COMPLETED".equalsIgnoreCase(p.getStatus())).count();

        int targetSessions = plans.stream().mapToInt(TreatmentPlan::getTargetSessions).sum();
        int completedSessions = plans.stream().mapToInt(TreatmentPlan::getCompletedSessions).sum();

        double adherence = targetSessions > 0
                ? Math.round(((double) completedSessions / targetSessions) * 1000.0) / 10.0
                : 0.0;

        List<SoapNote> notes = soapNoteRepository.findAll();
        double avgPainDelta = notes.isEmpty() ? 0.0 :
                Math.round(notes.stream().mapToInt(n -> n.getPainScoreBefore() - n.getPainScoreAfter()).average().orElse(0.0) * 10.0) / 10.0;

        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("SCHEDULED", sessionRepository.countByStatus("SCHEDULED"));
        statusCounts.put("ATTENDED", sessionRepository.countByStatus("ATTENDED"));
        statusCounts.put("CANCELLED", sessionRepository.countByStatus("CANCELLED"));

        RecoveryAnalyticsResponse res = new RecoveryAnalyticsResponse();
        res.setTotalTreatmentPlans(totalPlans);
        res.setActivePlans(activePlans);
        res.setCompletedPlans(completedPlans);
        res.setTotalTargetSessions(targetSessions);
        res.setTotalCompletedSessions(completedSessions);
        res.setOverallAdherencePercentage(adherence);
        res.setAveragePainScoreReduction(avgPainDelta);
        res.setSessionsByStatus(statusCounts);

        return res;
    }
}
