package com.physio.jdbc.dao;

import com.physio.jdbc.config.DatabaseConnectionPool;
import com.physio.jdbc.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Patient records.
 * Demonstrates CRUD operations using PreparedStatement (Unit 1.4 & 1.7).
 */
public class PatientDao {

    /**
     * Retrieves all registered patients joined with their user credentials.
     */
    public List<Patient> findAll() throws SQLException {
        String sql = """
            SELECT p.id, p.user_id, u.full_name, u.email, u.phone,
                   p.date_of_birth, p.gender, p.blood_group,
                   p.emergency_contact_name, p.emergency_contact_phone,
                   p.primary_complaint, p.medical_history, p.registered_at
            FROM patients p
            JOIN users u ON p.user_id = u.id
            ORDER BY p.id ASC
        """;

        List<Patient> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /**
     * Retrieves a single patient by ID.
     */
    public Optional<Patient> findById(Long id) throws SQLException {
        String sql = """
            SELECT p.id, p.user_id, u.full_name, u.email, u.phone,
                   p.date_of_birth, p.gender, p.blood_group,
                   p.emergency_contact_name, p.emergency_contact_phone,
                   p.primary_complaint, p.medical_history, p.registered_at
            FROM patients p
            JOIN users u ON p.user_id = u.id
            WHERE p.id = ?
        """;

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Updates patient medical notes using PreparedStatement.
     */
    public boolean updateMedicalNotes(Long patientId, String primaryComplaint, String medicalHistory) throws SQLException {
        String sql = "UPDATE patients SET primary_complaint = ?, medical_history = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, primaryComplaint);
            ps.setString(2, medicalHistory);
            ps.setLong(3, patientId);

            return ps.executeUpdate() > 0;
        }
    }

    private Patient mapRow(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setId(rs.getLong("id"));
        p.setUserId(rs.getLong("user_id"));
        p.setFullName(rs.getString("full_name"));
        p.setEmail(rs.getString("email"));
        p.setPhone(rs.getString("phone"));
        p.setDateOfBirth(rs.getDate("date_of_birth"));
        p.setGender(rs.getString("gender"));
        p.setBloodGroup(rs.getString("blood_group"));
        p.setEmergencyContactName(rs.getString("emergency_contact_name"));
        p.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        p.setPrimaryComplaint(rs.getString("primary_complaint"));
        p.setMedicalHistory(rs.getString("medical_history"));
        p.setRegisteredAt(rs.getTimestamp("registered_at"));
        return p;
    }
}
