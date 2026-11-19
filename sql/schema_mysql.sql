-- ============================================================================
-- Physiotherapy Session Management System
-- Database Schema: MySQL 8+
-- Author: ROSHAN NAMDEO BAGALE (Roll No: 50, Div D, MCA Sem III)
-- Course: 24MCAC302 - Advance Java Programming
-- ============================================================================

CREATE DATABASE IF NOT EXISTS physio_db;
USE physio_db;

-- Drop tables in reverse dependency order
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS soap_notes;
DROP TABLE IF EXISTS sessions;
DROP TABLE IF EXISTS packages_invoices;
DROP TABLE IF EXISTS plan_exercises;
DROP TABLE IF EXISTS exercises;
DROP TABLE IF EXISTS treatment_plans;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS therapists;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------------------------------------------------------
-- 1. USERS: Authentication and Role-Based Access Control (RBAC)
-- ----------------------------------------------------------------------------
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    role ENUM('ADMIN', 'THERAPIST', 'RECEPTIONIST', 'PATIENT') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_users_username (username),
    INDEX idx_users_role (role)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 2. THERAPISTS: Clinical provider profiles and scheduling preferences
-- ----------------------------------------------------------------------------
CREATE TABLE therapists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    experience_years INT NOT NULL,
    consultation_fee DECIMAL(10, 2) NOT NULL,
    available_days VARCHAR(50) DEFAULT 'MON,TUE,WED,THU,FRI',
    slot_duration_minutes INT DEFAULT 45,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_therapists_specialization (specialization)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 3. PATIENTS: Patient clinical intake records and emergency details
-- ----------------------------------------------------------------------------
CREATE TABLE patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    date_of_birth DATE NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    blood_group VARCHAR(5),
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    primary_complaint TEXT,
    medical_history TEXT,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_patients_user_id (user_id)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 4. TREATMENT_PLANS: Individualized rehabilitation programs
-- ----------------------------------------------------------------------------
CREATE TABLE treatment_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    therapist_id BIGINT NOT NULL,
    diagnosis VARCHAR(200) NOT NULL,
    treatment_goal TEXT NOT NULL,
    start_date DATE NOT NULL,
    estimated_end_date DATE,
    target_sessions INT NOT NULL,
    completed_sessions INT DEFAULT 0,
    status ENUM('ACTIVE', 'COMPLETED', 'PAUSED', 'DISCONTINUED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (therapist_id) REFERENCES therapists(id) ON DELETE RESTRICT,
    INDEX idx_plans_patient (patient_id),
    INDEX idx_plans_therapist (therapist_id),
    INDEX idx_plans_status (status)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 5. EXERCISES: Comprehensive clinical exercise directory
-- ----------------------------------------------------------------------------
CREATE TABLE exercises (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) UNIQUE NOT NULL,
    target_muscle_group VARCHAR(100) NOT NULL,
    category ENUM('STRENGTH', 'FLEXIBILITY', 'BALANCE', 'MOBILITY') NOT NULL,
    instructions TEXT NOT NULL,
    default_sets INT DEFAULT 3,
    default_reps INT DEFAULT 10,
    default_hold_seconds INT DEFAULT 0,
    INDEX idx_exercises_category (category)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 6. PLAN_EXERCISES: Prescribed Home Exercise Program (HEP)
-- ----------------------------------------------------------------------------
CREATE TABLE plan_exercises (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_id BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,
    prescribed_sets INT NOT NULL,
    prescribed_reps INT NOT NULL,
    prescribed_hold_seconds INT DEFAULT 0,
    frequency_per_day INT DEFAULT 2,
    special_notes VARCHAR(255),
    FOREIGN KEY (plan_id) REFERENCES treatment_plans(id) ON DELETE CASCADE,
    FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE RESTRICT,
    UNIQUE KEY uq_plan_exercise (plan_id, exercise_id)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 7. PACKAGES_INVOICES: Multi-session rehabilitation packages and billing
-- ----------------------------------------------------------------------------
CREATE TABLE packages_invoices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    package_name VARCHAR(100) NOT NULL,
    total_sessions INT NOT NULL,
    remaining_sessions INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    discount_amount DECIMAL(10, 2) DEFAULT 0.00,
    net_amount DECIMAL(10, 2) NOT NULL,
    payment_status ENUM('PAID', 'PENDING', 'PARTIAL') DEFAULT 'PAID',
    payment_method ENUM('CASH', 'UPI', 'CARD', 'NET_BANKING') DEFAULT 'UPI',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_invoices_patient (patient_id)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 8. SESSIONS: Appointment scheduling and real-time attendance
-- ----------------------------------------------------------------------------
CREATE TABLE sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_id BIGINT NOT NULL,
    therapist_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status ENUM('SCHEDULED', 'ATTENDED', 'CANCELLED', 'NO_SHOW', 'RESCHEDULED') DEFAULT 'SCHEDULED',
    cancellation_reason VARCHAR(255),
    package_invoice_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (plan_id) REFERENCES treatment_plans(id) ON DELETE CASCADE,
    FOREIGN KEY (therapist_id) REFERENCES therapists(id) ON DELETE RESTRICT,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (package_invoice_id) REFERENCES packages_invoices(id) ON DELETE SET NULL,
    UNIQUE KEY uq_therapist_slot (therapist_id, session_date, start_time),
    INDEX idx_sessions_date (session_date),
    INDEX idx_sessions_therapist_date (therapist_id, session_date),
    INDEX idx_sessions_patient (patient_id),
    INDEX idx_sessions_status (status)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 9. SOAP_NOTES: Digital clinical progress notes
-- ----------------------------------------------------------------------------
CREATE TABLE soap_notes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT UNIQUE NOT NULL,
    pain_score_before INT NOT NULL,
    pain_score_after INT NOT NULL,
    range_of_motion_notes VARCHAR(255),
    muscle_strength_grade VARCHAR(10),
    subjective_notes TEXT NOT NULL,
    objective_findings TEXT NOT NULL,
    assessment_summary TEXT NOT NULL,
    plan_next_session TEXT NOT NULL,
    modalities_used VARCHAR(255),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE CASCADE,
    INDEX idx_soap_session (session_id)
) ENGINE=InnoDB;
