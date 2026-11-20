package com.physio.api.controller;

import com.physio.api.dto.ApiResponse;
import com.physio.api.dto.TherapistSummaryResponse;
import com.physio.api.service.TherapistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "4. Physiotherapists & Availability", description = "Endpoints for viewing physiotherapist profiles and computing real-time unbooked appointment slots.")
@RestController
@RequestMapping("/api/v1/therapists")
public class TherapistRestController {

    private final TherapistService therapistService;

    @Autowired
    public TherapistRestController(TherapistService therapistService) {
        this.therapistService = therapistService;
    }

    @Operation(summary = "List all physiotherapists", description = "Retrieve physiotherapist profiles, specializations, experience, and consultation fees.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TherapistSummaryResponse>>> getTherapists() {
        List<TherapistSummaryResponse> list = therapistService.getAllTherapists();
        return ResponseEntity.ok(ApiResponse.ok("Physiotherapists retrieved", list));
    }

    @Operation(summary = "Check therapist slot availability", description = "Calculates available unbooked clinical appointment slots for a given date by subtracting booked sessions from working clinic hours.")
    @GetMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<TherapistSummaryResponse>> getAvailability(
            @Parameter(description = "Therapist ID", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Date to check (YYYY-MM-DD), defaults to today")
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        TherapistSummaryResponse res = therapistService.getTherapistAvailability(id, date);
        return ResponseEntity.ok(ApiResponse.ok("Therapist availability slots retrieved for " + (date != null ? date : LocalDate.now()), res));
    }
}
