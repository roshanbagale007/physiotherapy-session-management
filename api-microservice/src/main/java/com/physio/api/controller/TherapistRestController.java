package com.physio.api.controller;

import com.physio.api.dto.ApiResponse;
import com.physio.api.dto.TherapistSummaryResponse;
import com.physio.api.service.TherapistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/therapists")
public class TherapistRestController {

    private final TherapistService therapistService;

    @Autowired
    public TherapistRestController(TherapistService therapistService) {
        this.therapistService = therapistService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TherapistSummaryResponse>>> getTherapists() {
        List<TherapistSummaryResponse> list = therapistService.getAllTherapists();
        return ResponseEntity.ok(ApiResponse.ok("Physiotherapists retrieved", list));
    }

    /**
     * GET /api/v1/therapists/{id}/availability?date=YYYY-MM-DD
     * Checks available appointment slots for the therapist on a specific date.
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<TherapistSummaryResponse>> getAvailability(
            @PathVariable("id") Long id,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        TherapistSummaryResponse res = therapistService.getTherapistAvailability(id, date);
        return ResponseEntity.ok(ApiResponse.ok("Therapist availability slots retrieved for " + (date != null ? date : LocalDate.now()), res));
    }
}
