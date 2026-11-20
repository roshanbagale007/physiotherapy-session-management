package com.physio.api.controller;

import com.physio.api.dto.ApiResponse;
import com.physio.api.dto.ErrorResponse;
import com.physio.api.dto.TreatmentPlanRequest;
import com.physio.api.dto.TreatmentPlanResponse;
import com.physio.api.service.TreatmentPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "2. Treatment Plans", description = "Endpoints for creating and tracking long-term physical therapy rehabilitation protocols.")
@RestController
@RequestMapping("/api/v1/plans")
public class TreatmentPlanRestController {

    private final TreatmentPlanService planService;

    @Autowired
    public TreatmentPlanRestController(TreatmentPlanService planService) {
        this.planService = planService;
    }

    @Operation(summary = "List treatment plans", description = "Filter plans by clinical status (ACTIVE, COMPLETED) or patient ID.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TreatmentPlanResponse>>> getPlans(
            @Parameter(description = "Plan status: ACTIVE, COMPLETED, PAUSED, DISCONTINUED")
            @RequestParam(value = "status", required = false) String status,
            @Parameter(description = "Filter by Patient ID")
            @RequestParam(value = "patientId", required = false) Long patientId) {

        List<TreatmentPlanResponse> list = planService.getAllPlans(status, patientId);
        return ResponseEntity.ok(ApiResponse.ok("Treatment plans retrieved", list));
    }

    @Operation(summary = "Get treatment plan by ID", description = "Fetch rehabilitation goals, session target vs completed ratio, and adherence percentage.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Plan found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Plan not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TreatmentPlanResponse>> getPlanById(
            @Parameter(description = "Treatment Plan ID", example = "1")
            @PathVariable("id") Long id) {
        TreatmentPlanResponse res = planService.getPlanById(id);
        return ResponseEntity.ok(ApiResponse.ok("Treatment plan details retrieved", res));
    }

    @Operation(summary = "Create new treatment plan", description = "Instantiates a new clinical protocol for a patient assigned to a lead physiotherapist.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Treatment plan created"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Patient or Therapist not found")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<TreatmentPlanResponse>> createPlan(
            @Valid @RequestBody TreatmentPlanRequest request) {
        TreatmentPlanResponse res = planService.createPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Treatment plan created successfully", res));
    }

    @Operation(summary = "Update treatment plan status", description = "Update status flag (e.g. mark ACTIVE or COMPLETED).")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TreatmentPlanResponse>> updateStatus(
            @Parameter(description = "Plan ID", example = "1")
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
