-- ============================================================================
-- Stored Procedures: PostgreSQL 18+
-- Physiotherapy Session Management System
-- Demonstrates CallableStatement, Transactions & Stored Procedures (Unit 1)
-- ============================================================================

CREATE OR REPLACE PROCEDURE sp_complete_session_and_deduct(
    IN p_session_id BIGINT,
    IN p_pain_before INT,
    IN p_pain_after INT,
    IN p_rom_notes VARCHAR,
    IN p_muscle_grade VARCHAR,
    IN p_subjective TEXT,
    IN p_objective TEXT,
    IN p_assessment TEXT,
    IN p_plan_next TEXT,
    IN p_modalities VARCHAR,
    INOUT p_status_message VARCHAR DEFAULT ''
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_plan_id BIGINT;
    v_package_id BIGINT;
    v_current_status VARCHAR(20);
    v_target_sessions INT;
    v_completed_sessions INT;
BEGIN
    -- 1. Check if session exists
    SELECT plan_id, package_invoice_id, status 
    INTO v_plan_id, v_package_id, v_current_status
    FROM sessions 
    WHERE id = p_session_id;

    IF NOT FOUND THEN
        p_status_message := 'ERROR: Session not found with ID: ' || p_session_id;
        RETURN;
    END IF;

    -- 2. Verify session is not already completed or cancelled
    IF v_current_status = 'ATTENDED' THEN
        p_status_message := 'WARNING: Session is already marked as ATTENDED.';
        RETURN;
    ELSIF v_current_status = 'CANCELLED' THEN
        p_status_message := 'ERROR: Cannot complete a CANCELLED session.';
        RETURN;
    END IF;

    -- 3. Mark session as ATTENDED
    UPDATE sessions 
    SET status = 'ATTENDED' 
    WHERE id = p_session_id;

    -- 4. Insert clinical SOAP notes
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

    -- 5. Increment completed sessions in treatment plan
    UPDATE treatment_plans 
    SET completed_sessions = completed_sessions + 1 
    WHERE id = v_plan_id
    RETURNING target_sessions, completed_sessions 
    INTO v_target_sessions, v_completed_sessions;

    -- Check if treatment plan milestone is reached
    IF v_completed_sessions >= v_target_sessions THEN
        UPDATE treatment_plans 
        SET status = 'COMPLETED', estimated_end_date = CURRENT_DATE 
        WHERE id = v_plan_id;
    END IF;

    -- 6. Decrement remaining sessions in package if linked
    IF v_package_id IS NOT NULL THEN
        UPDATE packages_invoices 
        SET remaining_sessions = GREATEST(0, remaining_sessions - 1) 
        WHERE id = v_package_id;
    END IF;

    p_status_message := 'SUCCESS: Session marked ATTENDED, SOAP notes recorded, and plan updated.';
END;
$$;
