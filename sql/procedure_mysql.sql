-- ============================================================================
-- Stored Procedures: MySQL 8+
-- Physiotherapy Session Management System
-- Demonstrates CallableStatement, Transactions & Stored Procedures (Unit 1)
-- ============================================================================

USE physio_db;

DROP PROCEDURE IF EXISTS sp_complete_session_and_deduct;

DELIMITER //

CREATE PROCEDURE sp_complete_session_and_deduct(
    IN p_session_id BIGINT,
    IN p_pain_before INT,
    IN p_pain_after INT,
    IN p_rom_notes VARCHAR(255),
    IN p_muscle_grade VARCHAR(10),
    IN p_subjective TEXT,
    IN p_objective TEXT,
    IN p_assessment TEXT,
    IN p_plan_next TEXT,
    IN p_modalities VARCHAR(255),
    OUT p_status_message VARCHAR(255)
)
proc_body: BEGIN
    DECLARE v_plan_id BIGINT;
    DECLARE v_package_id BIGINT;
    DECLARE v_current_status VARCHAR(20);
    DECLARE v_target_sessions INT;
    DECLARE v_completed_sessions INT;

    -- Handler for SQL exceptions (rollback)
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_status_message = 'ERROR: Database transaction failed and was rolled back.';
    END;

    START TRANSACTION;

    -- 1. Check if session exists
    SELECT plan_id, package_invoice_id, status 
    INTO v_plan_id, v_package_id, v_current_status
    FROM sessions 
    WHERE id = p_session_id;

    IF v_plan_id IS NULL THEN
        SET p_status_message = CONCAT('ERROR: Session not found with ID: ', p_session_id);
        ROLLBACK;
        LEAVE proc_body;
    END IF;

    -- 2. Verify status
    IF v_current_status = 'ATTENDED' THEN
        SET p_status_message = 'WARNING: Session is already marked as ATTENDED.';
        ROLLBACK;
        LEAVE proc_body;
    ELSEIF v_current_status = 'CANCELLED' THEN
        SET p_status_message = 'ERROR: Cannot complete a CANCELLED session.';
        ROLLBACK;
        LEAVE proc_body;
    END IF;

    -- 3. Mark session ATTENDED
    UPDATE sessions 
    SET status = 'ATTENDED' 
    WHERE id = p_session_id;

    -- 4. Insert SOAP notes
    INSERT INTO soap_notes (
        session_id, pain_score_before, pain_score_after,
        range_of_motion_notes, muscle_strength_grade,
        subjective_notes, objective_findings, assessment_summary,
        plan_next_session, modalities_used
    ) VALUES (
        p_session_id, p_pain_before, p_pain_after,
        p_rom_notes, p_muscle_grade,
        p_subjective, p_objective, p_assessment,
        p_plan_next, p_modalities
    );

    -- 5. Update treatment plan
    UPDATE treatment_plans 
    SET completed_sessions = completed_sessions + 1 
    WHERE id = v_plan_id;

    SELECT target_sessions, completed_sessions 
    INTO v_target_sessions, v_completed_sessions
    FROM treatment_plans 
    WHERE id = v_plan_id;

    IF v_completed_sessions >= v_target_sessions THEN
        UPDATE treatment_plans 
        SET status = 'COMPLETED', estimated_end_date = CURRENT_DATE 
        WHERE id = v_plan_id;
    END IF;

    -- 6. Decrement package if linked
    IF v_package_id IS NOT NULL THEN
        UPDATE packages_invoices 
        SET remaining_sessions = GREATEST(0, remaining_sessions - 1) 
        WHERE id = v_package_id;
    END IF;

    COMMIT;
    SET p_status_message = 'SUCCESS: Session marked ATTENDED, SOAP notes recorded, and plan updated.';
END //

DELIMITER ;
