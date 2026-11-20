package com.physio.api.controller;

import com.physio.api.dto.ApiResponse;
import com.physio.api.dto.PatientSummaryResponse;
import com.physio.api.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientRestController {

    private final PatientService patientService;

    @Autowired
    public PatientRestController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientSummaryResponse>>> getPatients() {
        List<PatientSummaryResponse> list = patientService.getAllPatients();
        return ResponseEntity.ok(ApiResponse.ok("Patients retrieved successfully", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientSummaryResponse>> getPatientById(@PathVariable("id") Long id) {
        PatientSummaryResponse res = patientService.getPatientById(id);
        return ResponseEntity.ok(ApiResponse.ok("Patient profile retrieved", res));
    }
}
