package com.physio.api.controller;

import com.physio.api.dto.ApiResponse;
import com.physio.api.dto.PatientSummaryResponse;
import com.physio.api.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "3. Patients Directory", description = "Endpoints for retrieving patient medical history and demographic details.")
@RestController
@RequestMapping("/api/v1/patients")
public class PatientRestController {

    private final PatientService patientService;

    @Autowired
    public PatientRestController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "List all patients", description = "Retrieve registered patient directory with emergency contact details and primary complaints.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientSummaryResponse>>> getPatients() {
        List<PatientSummaryResponse> list = patientService.getAllPatients();
        return ResponseEntity.ok(ApiResponse.ok("Patients retrieved successfully", list));
    }

    @Operation(summary = "Get patient by ID", description = "Retrieve full clinical medical profile for a patient.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientSummaryResponse>> getPatientById(
            @Parameter(description = "Patient ID", example = "1")
            @PathVariable("id") Long id) {
        PatientSummaryResponse res = patientService.getPatientById(id);
        return ResponseEntity.ok(ApiResponse.ok("Patient profile retrieved", res));
    }
}
