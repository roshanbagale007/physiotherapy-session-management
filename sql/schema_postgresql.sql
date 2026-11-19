-- ============================================================================
-- Physiotherapy Session Management System
-- Database Schema: PostgreSQL 18+
-- Author: ROSHAN NAMDEO BAGALE (Roll No: 50, Div D, MCA Sem III)
-- Course: 24MCAC302 - Advance Java Programming
-- ============================================================================

-- Drop existing tables in reverse dependency order (clean reset)
DROP TABLE IF EXISTS soap_notes CASCADE;
DROP TABLE IF EXISTS sessions CASCADE;
DROP TABLE IF EXISTS packages_invoices CASCADE;
DROP TABLE IF EXISTS plan_exercises CASCADE;
DROP TABLE IF EXISTS exercises CASCADE;
DROP TABLE IF EXISTS treatment_plans CASCADE;
DROP TABLE IF EXISTS patients CASCADE;
DROP TABLE IF EXISTS therapists CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ----------------------------------------------------------------------------
-- 1. USERS: Central authentication and Role-Based Access Control (RBAC)
-- ----------------------------------------------------------------------------
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL, -- SHA-256 encrypted password
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'THERAPIST', 'RECEPTIONIST', 'PATIENT')),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);

-- ----------------------------------------------------------------------------
-- 2. THERAPISTS: Clinical provider profiles and scheduling preferences
-- ----------------------------------------------------------------------------
CREATE TABLE therapists (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    specialization VARCHAR(100) NOT NULL, -- e.g., 'Orthopedic Rehab', 'Sports Medicine', 'Neuro Rehab'
    license_number VARCHAR(50) UNIQUE NOT NULL,
    experience_years INT NOT NULL CHECK (experience_years >= 0),
    consultation_fee DECIMAL(10, 2) NOT NULL CHECK (consultation_fee >= 0),
    available_days VARCHAR(50) DEFAULT 'MON,TUE,WED,THU,FRI',
    slot_duration_minutes INT DEFAULT 45 CHECK (slot_duration_minutes IN (30, 45, 60))
);

CREATE INDEX idx_therapists_specialization ON therapists(specialization);

-- ----------------------------------------------------------------------------
-- 3. PATIENTS: Patient clinical intake records and emergency details
-- ----------------------------------------------------------------------------
CREATE TABLE patients (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    blood_group VARCHAR(5),
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    primary_complaint TEXT,
    medical_history TEXT,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_patients_user_id ON patients(user_id);

-- ----------------------------------------------------------------------------
-- 4. TREATMENT_PLANS: Individualized rehabilitation programs
-- ----------------------------------------------------------------------------
CREATE TABLE treatment_plans (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    therapist_id BIGINT NOT NULL REFERENCES therapists(id) ON DELETE RESTRICT,
    diagnosis VARCHAR(200) NOT NULL, -- e.g. 'Cervical Radiculopathy', 'Post-ACL Reconstruction Phase 2'
    treatment_goal TEXT NOT NULL,
    start_date DATE NOT NULL,
    estimated_end_date DATE,
    target_sessions INT NOT NULL CHECK (target_sessions > 0),
    completed_sessions INT DEFAULT 0 CHECK (completed_sessions >= 0),
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'COMPLETED', 'PAUSED', 'DISCONTINUED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_plans_patient ON treatment_plans(patient_id);
CREATE INDEX idx_plans_therapist ON treatment_plans(therapist_id);
CREATE INDEX idx_plans_status ON treatment_plans(status);

-- ----------------------------------------------------------------------------
-- 5. EXERCISES: Comprehensive clinical exercise directory
-- ----------------------------------------------------------------------------
CREATE TABLE exercises (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) UNIQUE NOT NULL,
    target_muscle_group VARCHAR(100) NOT NULL, -- 'Rotator Cuff', 'Quadriceps', 'Core Stabilizers'
    category VARCHAR(50) NOT NULL CHECK (category IN ('STRENGTH', 'FLEXIBILITY', 'BALANCE', 'MOBILITY')),
    instructions TEXT NOT NULL,
    default_sets INT DEFAULT 3 CHECK (default_sets > 0),
    default_reps INT DEFAULT 10 CHECK (default_reps > 0),
    default_hold_seconds INT DEFAULT 0 CHECK (default_hold_seconds >= 0)
);

CREATE INDEX idx_exercises_category ON exercises(category);

-- ----------------------------------------------------------------------------
-- 6. PLAN_EXERCISES: Prescribed Home Exercise Program (HEP)
-- ----------------------------------------------------------------------------
CREATE TABLE plan_exercises (
    id BIGSERIAL PRIMARY KEY,
    plan_id BIGINT NOT NULL REFERENCES treatment_plans(id) ON DELETE CASCADE,
    exercise_id BIGINT NOT NULL REFERENCES exercises(id) ON DELETE RESTRICT,
    prescribed_sets INT NOT NULL CHECK (prescribed_sets > 0),
    prescribed_reps INT NOT NULL CHECK (prescribed_reps > 0),
    prescribed_hold_seconds INT DEFAULT 0 CHECK (prescribed_hold_seconds >= 0),
    frequency_per_day INT DEFAULT 2 CHECK (frequency_per_day > 0),
    special_notes VARCHAR(255),
    CONSTRAINT uq_plan_exercise UNIQUE (plan_id, exercise_id)
);

-- ----------------------------------------------------------------------------
-- 7. PACKAGES_INVOICES: Multi-session rehabilitation packages and billing
-- ----------------------------------------------------------------------------
CREATE TABLE packages_invoices (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    package_name VARCHAR(100) NOT NULL, -- e.g. 'Comprehensive Spine Care (10 Sessions)'
    total_sessions INT NOT NULL CHECK (total_sessions > 0),
    remaining_sessions INT NOT NULL CHECK (remaining_sessions >= 0),
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    discount_amount DECIMAL(10, 2) DEFAULT 0.00 CHECK (discount_amount >= 0),
    net_amount DECIMAL(10, 2) NOT NULL CHECK (net_amount >= 0),
    payment_status VARCHAR(20) DEFAULT 'PAID' CHECK (payment_status IN ('PAID', 'PENDING', 'PARTIAL')),
    payment_method VARCHAR(20) DEFAULT 'UPI' CHECK (payment_method IN ('CASH', 'UPI', 'CARD', 'NET_BANKING')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_invoices_patient ON packages_invoices(patient_id);

-- ----------------------------------------------------------------------------
-- 8. SESSIONS: Appointment scheduling and real-time attendance
-- ----------------------------------------------------------------------------
CREATE TABLE sessions (
    id BIGSERIAL PRIMARY KEY,
    plan_id BIGINT NOT NULL REFERENCES treatment_plans(id) ON DELETE CASCADE,
    therapist_id BIGINT NOT NULL REFERENCES therapists(id) ON DELETE RESTRICT,
    patient_id BIGINT NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'ATTENDED', 'CANCELLED', 'NO_SHOW', 'RESCHEDULED')),
    cancellation_reason VARCHAR(255),
    package_invoice_id BIGINT REFERENCES packages_invoices(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_therapist_slot UNIQUE (therapist_id, session_date, start_time)
);

CREATE INDEX idx_sessions_date ON sessions(session_date);
CREATE INDEX idx_sessions_therapist_date ON sessions(therapist_id, session_date);
CREATE INDEX idx_sessions_patient ON sessions(patient_id);
CREATE INDEX idx_sessions_status ON sessions(status);

-- ----------------------------------------------------------------------------
-- 9. SOAP_NOTES: Digital clinical documentation (Subjective, Objective, Assessment, Plan)
-- ----------------------------------------------------------------------------
CREATE TABLE soap_notes (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT UNIQUE NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    pain_score_before INT NOT NULL CHECK (pain_score_before BETWEEN 0 AND 10), -- VAS scale
    pain_score_after INT NOT NULL CHECK (pain_score_after BETWEEN 0 AND 10),
    range_of_motion_notes VARCHAR(255), -- e.g. 'Right Shoulder Flexion: 140 deg'
    muscle_strength_grade VARCHAR(10),  -- e.g. '4/5 MMT'
    subjective_notes TEXT NOT NULL,
    objective_findings TEXT NOT NULL,
    assessment_summary TEXT NOT NULL,
    plan_next_session TEXT NOT NULL,
    modalities_used VARCHAR(255), -- 'Ultrasound 1MHz, IFT 4-pole, Cryotherapy'
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_soap_session ON soap_notes(session_id);
