package com.physio.api.controller;

import com.physio.api.dto.*;
import com.physio.api.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Unit 4.2: RESTful Controller for Therapy Appointments & Attendance.
 * Demonstrates:
 * - @RestController, @RequestMapping, @GetMapping, @PostMapping, @PutMapping
 * - @PathVariable, @RequestParam, @RequestBody, @Valid
 * - HttpStatus (200 OK, 201 Created)
 */
@RestController
@RequestMapping("/api/v1/sessions")
public class SessionRestController {

    private final SessionService sessionService;

    @Autowired
    public SessionRestController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * GET /api/v1/sessions
     * Filter sessions by date, status, patientId, or therapistId.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getSessions(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "patientId", required = false) Long patientId,
            @RequestParam(value = "therapistId", required = false) Long therapistId) {

        List<SessionResponse> list = sessionService.getAllSessions(date, status, patientId, therapistId);
        return ResponseEntity.ok(ApiResponse.ok("Sessions retrieved successfully", list));
    }

    /**
     * GET /api/v1/sessions/{id}
     * Retrieve single session with full clinical notes.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SessionResponse>> getSessionById(@PathVariable("id") Long id) {
        SessionResponse res = sessionService.getSessionById(id);
        return ResponseEntity.ok(ApiResponse.ok("Session details retrieved", res));
    }

    /**
     * POST /api/v1/sessions
     * Book new session with slot conflict validation & package deduction.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SessionResponse>> bookSession(@Valid @RequestBody SessionBookRequest request) {
        SessionResponse res = sessionService.bookSession(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Session booked successfully with conflict check verification", res));
    }

    /**
     * PUT /api/v1/sessions/{id}/status
     * Mark attendance (ATTENDED, CANCELLED, NO_SHOW).
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SessionResponse>> updateStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody SessionStatusUpdateRequest request) {

        SessionResponse res = sessionService.updateSessionStatus(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Session status updated to " + res.getStatus(), res));
    }

    /**
     * POST /api/v1/sessions/{id}/soap-note
     * Record clinical SOAP progress note with VAS pain scores.
     */
    @PostMapping("/{id}/soap-note")
    public ResponseEntity<ApiResponse<SoapNoteResponse>> recordSoapNote(
            @PathVariable("id") Long id,
            @Valid @RequestBody SoapNoteRequest request) {

        SoapNoteResponse res = sessionService.recordSoapNote(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Clinical SOAP note recorded. Pain score reduced by " + res.getPainDelta() + " points.", res));
    }
}
