package com.physio.jdbc.dao;

import com.physio.jdbc.config.DatabaseConnectionPool;
import com.physio.jdbc.model.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Appointment Sessions.
 * Demonstrates:
 * - PreparedStatement conflict detection (Unit 1.7)
 * - Batch Processing for attendance (Unit 1.6)
 * - CallableStatement for Stored Procedures (Unit 1.7)
 */
public class SessionDao {

    /**
     * Checks if a therapist slot is available (prevents double-booking).
     */
    public boolean isSlotAvailable(Long therapistId, Date sessionDate, Time startTime) throws SQLException {
        String sql = """
            SELECT COUNT(*) FROM sessions
            WHERE therapist_id = ? AND session_date = ? AND start_time = ?
            AND status NOT IN ('CANCELLED', 'RESCHEDULED')
        """;

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, therapistId);
            ps.setDate(2, sessionDate);
            ps.setTime(3, startTime);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // True if 0 existing bookings
                }
            }
        }
        return false;
    }

    /**
     * Retrieves all sessions for a specific patient.
     */
    public List<Session> findByPatientId(Long patientId) throws SQLException {
        String sql = """
            SELECT s.id, s.plan_id, s.therapist_id, ut.full_name AS therapist_name,
                   s.patient_id, up.full_name AS patient_name,
                   s.session_date, s.start_time, s.end_time, s.status,
                   s.cancellation_reason, s.package_invoice_id, s.created_at
            FROM sessions s
            JOIN therapists t ON s.therapist_id = t.id
            JOIN users ut ON t.user_id = ut.id
            JOIN patients p ON s.patient_id = p.id
            JOIN users up ON p.user_id = up.id
            WHERE s.patient_id = ?
            ORDER BY s.session_date DESC, s.start_time DESC
        """;

        List<Session> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /**
     * UNIT 1.6: Batch Processing.
     * Marks attendance for multiple sessions in a single database round-trip.
     * Returns an array of affected row counts per batch command.
     */
    public int[] batchUpdateSessionStatus(List<Long> sessionIds, String newStatus) throws SQLException {
        String sql = "UPDATE sessions SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Long sessionId : sessionIds) {
                ps.setString(1, newStatus);
                ps.setLong(2, sessionId);
                ps.addBatch(); // Adds statement to batch queue
            }

            return ps.executeBatch(); // Executes all queued statements together
        }
    }

    /**
     * UNIT 1.7: CallableStatement for Stored Procedures.
     * Invokes sp_complete_session_and_deduct to atomically mark session attended,
     * record SOAP notes, update plan milestones, and deduct package credits.
     */
    public String callCompleteSessionProcedure(
            Long sessionId,
            int painBefore,
            int painAfter,
            String romNotes,
            String muscleGrade,
            String subjective,
            String objective,
            String assessment,
            String planNext,
            String modalities
    ) throws SQLException {

        // In PostgreSQL: CALL procedure_name(?, ?, ..., ?)
        // In MySQL: {CALL sp_complete_session_and_deduct(?, ?, ..., ?)}
        String callSql = "CALL sp_complete_session_and_deduct(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionPool.getConnection();
             CallableStatement cs = conn.prepareCall(callSql)) {

            cs.setLong(1, sessionId);
            cs.setInt(2, painBefore);
            cs.setInt(3, painAfter);
            cs.setString(4, romNotes);
            cs.setString(5, muscleGrade);
            cs.setString(6, subjective);
            cs.setString(7, objective);
            cs.setString(8, assessment);
            cs.setString(9, planNext);
            cs.setString(10, modalities);

            // Register INOUT parameter for status message
            cs.registerOutParameter(11, Types.VARCHAR);

            cs.execute();

            return cs.getString(11);
        }
    }

    private Session mapRow(ResultSet rs) throws SQLException {
        Session s = new Session();
        s.setId(rs.getLong("id"));
        s.setPlanId(rs.getLong("plan_id"));
        s.setTherapistId(rs.getLong("therapist_id"));
        s.setTherapistName(rs.getString("therapist_name"));
        s.setPatientId(rs.getLong("patient_id"));
        s.setPatientName(rs.getString("patient_name"));
        s.setSessionDate(rs.getDate("session_date"));
        s.setStartTime(rs.getTime("start_time"));
        s.setEndTime(rs.getTime("end_time"));
        s.setStatus(rs.getString("status"));
        s.setCancellationReason(rs.getString("cancellation_reason"));
        s.setPackageInvoiceId(rs.getLong("package_invoice_id"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        return s;
    }
}
