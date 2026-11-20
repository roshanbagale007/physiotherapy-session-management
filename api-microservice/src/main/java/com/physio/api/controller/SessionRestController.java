package com.physio.api.controller;

import com.physio.api.dto.*;
import com.physio.api.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 */
@Tag(name = "1. Therapy Sessions", description = "Endpoints for booking appointments, conflict detection, marking attendance, and recording clinical SOAP notes.")
@RestController
@RequestMapping("/api/v1/sessions")
public class SessionRestController {

    private final SessionService sessionService;

    @Autowired
    public SessionRestController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(summary = "List all therapy sessions", description = "Retrieve all appointments with optional filters for date, status, patient, or therapist.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sessions retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<com.physio.api.dto.ApiResponse<List<SessionResponse>>> getSessions(
            @Parameter(description = "Filter by date (YYYY-MM-DD)")
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Filter by status: SCHEDULED, ATTENDED, CANCELLED")
            @RequestParam(value = "status", required = false) String status,
            @Parameter(description = "Filter by Patient ID")
            @RequestParam(value = "patientId", required = false) Long patientId,
            @Parameter(description = "Filter by Therapist ID")
            @RequestParam(value = "therapistId", required = false) Long therapistId) {

        List<SessionResponse> list = sessionService.getAllSessions(date, status, patientId, therapistId);
        return ResponseEntity.ok(com.physio.api.dto.ApiResponse.ok("Sessions retrieved successfully", list));
    }

    @Operation(summary = "Get session details by ID", description = "Fetch a single appointment along with its full clinical SOAP evaluation note if recorded.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Session found"),
        @ApiResponse(responseCode = "404", description = "Session not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<com.physio.api.dto.ApiResponse<SessionResponse>> getSessionById(
            @Parameter(description = "Session Primary Key ID", example = "1")
            @PathVariable("id") Long id) {
        SessionResponse res = sessionService.getSessionById(id);
        return ResponseEntity.ok(com.physio.api.dto.ApiResponse.ok("Session details retrieved", res));
    }

    @Operation(summary = "Book a new clinical session", description = "Validates slot availability against therapist calendar, checks for time overlap collisions, and deducts 1 credit from the patient's prepaid package.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Session booked successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid booking payload", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Patient, Therapist, or Plan not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Therapist slot conflict detected", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<com.physio.api.dto.ApiResponse<SessionResponse>> bookSession(
            @Valid @RequestBody SessionBookRequest request) {
        SessionResponse res = sessionService.bookSession(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(com.physio.api.dto.ApiResponse.created("Session booked successfully with conflict check verification", res));
    }

    @Operation(summary = "Update session attendance status", description = "Transition appointment between SCHEDULED, ATTENDED, CANCELLED, or NO_SHOW. Marking ATTENDED automatically advances plan progress.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status enum value"),
        @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<com.physio.api.dto.ApiResponse<SessionResponse>> updateStatus(
            @Parameter(description = "Session ID", example = "1")
            @PathVariable("id") Long id,
            @Valid @RequestBody SessionStatusUpdateRequest request) {

        SessionResponse res = sessionService.updateSessionStatus(id, request);
        return ResponseEntity.ok(com.physio.api.dto.ApiResponse.ok("Session status updated to " + res.getStatus(), res));
    }

    @Operation(summary = "Record digital SOAP progress note", description = "Clinical SOAP documentation including VAS pain score delta (0-10), ROM, MMT strength grade, modalities used, and functional assessment. Auto-marks session as ATTENDED.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "SOAP note recorded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid clinical input", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Session not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/soap-note")
    public ResponseEntity<com.physio.api.dto.ApiResponse<SoapNoteResponse>> recordSoapNote(
            @Parameter(description = "Session ID", example = "1")
            @PathVariable("id") Long id,
            @Valid @RequestBody SoapNoteRequest request) {

        SoapNoteResponse res = sessionService.recordSoapNote(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(com.physio.api.dto.ApiResponse.created("Clinical SOAP note recorded. Pain score reduced by " + res.getPainDelta() + " points.", res));
    }
}
