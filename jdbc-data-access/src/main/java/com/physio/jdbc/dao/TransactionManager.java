package com.physio.jdbc.dao;

import com.physio.jdbc.config.DatabaseConnectionPool;
import com.physio.jdbc.model.Session;

import java.sql.*;

/**
 * Demonstrates Unit 1.5: Handling Transactions with Commit & Rollback.
 * Ensures ACID transaction guarantees for critical clinical & financial workflows.
 */
public class TransactionManager {

    /**
     * Atomically schedules a session AND deducts one credit from the patient's package.
     * Uses setAutoCommit(false), commit(), and rollback().
     */
    public boolean bookSessionWithPackageCredit(Session session, Long packageId) throws SQLException {
        String checkPackageSql = "SELECT remaining_sessions FROM packages_invoices WHERE id = ? FOR UPDATE";
        String insertSessionSql = """
            INSERT INTO sessions (plan_id, therapist_id, patient_id, session_date, start_time, end_time, status, package_invoice_id)
            VALUES (?, ?, ?, ?, ?, ?, 'SCHEDULED', ?)
        """;
        String decrementPackageSql = "UPDATE packages_invoices SET remaining_sessions = remaining_sessions - 1 WHERE id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnectionPool.getConnection();
            // 1. Disable Auto-Commit mode (start atomic transaction block)
            conn.setAutoCommit(false);

            // 2. Verify package has remaining sessions
            int remaining = 0;
            try (PreparedStatement psCheck = conn.prepareStatement(checkPackageSql)) {
                psCheck.setLong(1, packageId);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        remaining = rs.getInt(1);
                    } else {
                        throw new SQLException("Package not found with ID: " + packageId);
                    }
                }
            }

            if (remaining <= 0) {
                throw new SQLException("Cannot book session: Package has 0 remaining sessions!");
            }

            // 3. Insert Session
            try (PreparedStatement psSession = conn.prepareStatement(insertSessionSql, Statement.RETURN_GENERATED_KEYS)) {
                psSession.setLong(1, session.getPlanId());
                psSession.setLong(2, session.getTherapistId());
                psSession.setLong(3, session.getPatientId());
                psSession.setDate(4, session.getSessionDate());
                psSession.setTime(5, session.getStartTime());
                psSession.setTime(6, session.getEndTime());
                psSession.setLong(7, packageId);
                psSession.executeUpdate();

                try (ResultSet generatedKeys = psSession.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        session.setId(generatedKeys.getLong(1));
                    }
                }
            }

            // 4. Decrement Package balance
            try (PreparedStatement psPackage = conn.prepareStatement(decrementPackageSql)) {
                psPackage.setLong(1, packageId);
                psPackage.executeUpdate();
            }

            // 5. Commit transaction atomically
            conn.commit();
            System.out.println(">> [TRANSACTION SUCCESS]: Session #" + session.getId() + " booked and package credit deducted.");
            return true;

        } catch (SQLException e) {
            // 6. Rollback on ANY failure
            if (conn != null) {
                try {
                    System.err.println(">> [TRANSACTION ROLLBACK]: Rolling back due to: " + e.getMessage());
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset to default state
                    conn.close();             // Return connection to pool
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Demonstrates simulated failure and transaction rollback.
     * Inserts a session but intentionally throws an error before decrementing package.
     * Verifies that the inserted session is NOT persisted (Atomic Rollback).
     */
    public boolean demonstrateRollback(Session session, Long packageId) {
        String insertSessionSql = """
            INSERT INTO sessions (plan_id, therapist_id, patient_id, session_date, start_time, end_time, status, package_invoice_id)
            VALUES (?, ?, ?, ?, ?, ?, 'SCHEDULED', ?)
        """;

        Connection conn = null;
        try {
            conn = DatabaseConnectionPool.getConnection();
            conn.setAutoCommit(false);

            // Step 1: Insert session
            try (PreparedStatement ps = conn.prepareStatement(insertSessionSql)) {
                ps.setLong(1, session.getPlanId());
                ps.setLong(2, session.getTherapistId());
                ps.setLong(3, session.getPatientId());
                ps.setDate(4, session.getSessionDate());
                ps.setTime(5, session.getStartTime());
                ps.setTime(6, session.getEndTime());
                ps.setLong(7, packageId);
                ps.executeUpdate();
                System.out.println("   [Step 1] Session statement executed in transaction buffer.");
            }

            // Step 2: Simulate sudden failure (e.g., payment gateway timeout, hardware fault)
            System.out.println("   [Step 2] SIMULATING EXCEPTION: Payment gateway timeout!");
            if (true) {
                throw new SQLException("Simulated Payment Failure: Injected exception to test rollback.");
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("   [Catch Block] Caught simulated error: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("   [Rollback] conn.rollback() successfully executed! Database remains clean.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
