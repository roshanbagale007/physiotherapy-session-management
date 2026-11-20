package com.physio.api.controller;

import com.physio.api.dto.ApiResponse;
import com.physio.api.dto.RecoveryAnalyticsResponse;
import com.physio.api.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "5. Clinical Recovery Analytics", description = "Endpoints for aggregate clinic-wide recovery KPIs, patient adherence rates, and pain reduction metrics.")
@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsRestController {

    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsRestController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Operation(summary = "Get clinic recovery & adherence analytics", description = "Computes aggregate rehabilitation KPIs: total active vs completed protocols, delivered sessions, adherence percentage, and average VAS pain score reduction across all SOAP evaluations.")
    @GetMapping("/recovery")
    public ResponseEntity<ApiResponse<RecoveryAnalyticsResponse>> getRecoveryStats() {
        RecoveryAnalyticsResponse res = analyticsService.getRecoveryAnalytics();
        return ResponseEntity.ok(ApiResponse.ok("Clinical recovery analytics computed", res));
    }
}
