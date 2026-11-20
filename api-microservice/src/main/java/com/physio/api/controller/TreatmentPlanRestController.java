package com.physio.api.controller;

import com.physio.api.dto.ApiResponse;
import com.physio.api.dto.TreatmentPlanRequest;
import com.physio.api.dto.TreatmentPlanResponse;
import com.physio.api.service.TreatmentPlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Unit 4.2: RESTful Controller for Clinical Rehabilitation Protocols.
 */
@RestController
@RequestMapping("/api/v1/plans")
public class TreatmentPlanRestController {

    private final TreatmentPlanService planService;

    @Autowired
    public TreatmentPlanRestController(TreatmentPlanService planService) {
        this.planService = planService;
    }

    /**
     * GET /api/v1/plans
     * List all treatment plans with optional filtering.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TreatmentPlanResponse>>> getPlans(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "patientId", required = false) Long patientId) {

        List<TreatmentPlanResponse> list = planService.getAllPlans(status, patientId);
        return ResponseEntity.ok(ApiResponse.ok("Treatment plans retrieved", list));
    }

    /**
     * GET /api/v1/plans/{id}
     * Get single plan with patient and therapist info.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TreatmentPlanResponse>> getPlanById(@PathVariable("id") Long id) {
        TreatmentPlanResponse res = planService.getPlanById(id);
        return ResponseEntity.ok(ApiResponse.ok("Treatment plan details retrieved", res));
    }

    /**
     * POST /api/v1/plans
     * Create new treatment plan with JSR-380 validation.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TreatmentPlanResponse>> createPlan(@Valid @RequestBody TreatmentPlanRequest request) {
        TreatmentPlanResponse res = planService.createPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Treatment plan created successfully", res));
    }

    /**
     * PATCH /api/v1/plans/{id}/status
     * Update plan status (ACTIVE, COMPLETED, PAUSED, DISCONTINUED).
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TreatmentPlanResponse>> updateStatus(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "status field is required in request body", null));
        }

        TreatmentPlanResponse res = planService.updatePlanStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Treatment plan status updated to " + res.getStatus(), res));
    }
}
