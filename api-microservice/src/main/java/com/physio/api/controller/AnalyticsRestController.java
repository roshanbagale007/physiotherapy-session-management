package com.physio.api.controller;

import com.physio.api.dto.ApiResponse;
import com.physio.api.dto.RecoveryAnalyticsResponse;
import com.physio.api.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsRestController {

    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsRestController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * GET /api/v1/analytics/recovery
     * Aggregate clinical rehabilitation metrics (adherence, pain score reduction delta, session counts).
     */
    @GetMapping("/recovery")
    public ResponseEntity<ApiResponse<RecoveryAnalyticsResponse>> getRecoveryStats() {
        RecoveryAnalyticsResponse res = analyticsService.getRecoveryAnalytics();
        return ResponseEntity.ok(ApiResponse.ok("Clinical recovery analytics computed", res));
    }
}
