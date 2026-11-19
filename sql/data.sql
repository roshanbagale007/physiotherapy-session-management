-- ============================================================================
-- Physiotherapy Session Management System
-- Seed Data: Sample Clinical Dataset
-- Author: ROSHAN NAMDEO BAGALE (Roll No: 50, Div D, MCA Sem III)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 1. USERS (Passwords hashed with SHA-256)
-- admin        / Admin@123   -> e86f78a8a3caf0b60d8e74e5942aa6d86dc150cd3c03338aef25b7d2d7e3acc7
-- dr.sameer    / Doctor@123  -> 63bbe564aa5c0882355f6dcd6f2241b6f8d02d6f39725966ae564cd310f07850
-- dr.ananya    / Doctor@123  -> 63bbe564aa5c0882355f6dcd6f2241b6f8d02d6f39725966ae564cd310f07850
-- receptionist / Staff@123   -> dfd48f36338aa36228ebb9e204bba6b4e18db0b623e25c458901edc831fb18e9
-- rahul.v      / Patient@123 -> 6dff47814e92e3a14c9f021d2d3ddd353a03e5bf3b0f49d53e80a1a3606c86d6
-- priya.p      / Patient@123 -> 6dff47814e92e3a14c9f021d2d3ddd353a03e5bf3b0f49d53e80a1a3606c86d6
-- amit.k       / Patient@123 -> 6dff47814e92e3a14c9f021d2d3ddd353a03e5bf3b0f49d53e80a1a3606c86d6
-- ----------------------------------------------------------------------------
INSERT INTO users (id, username, password_hash, full_name, email, phone, role, is_active) VALUES
(1, 'admin', 'e86f78a8a3caf0b60d8e74e5942aa6d86dc150cd3c03338aef25b7d2d7e3acc7', 'System Administrator', 'admin@physioclinic.com', '9876543210', 'ADMIN', TRUE),
(2, 'dr.sameer', '63bbe564aa5c0882355f6dcd6f2241b6f8d02d6f39725966ae564cd310f07850', 'Dr. Sameer Deshmukh, MPT', 'sameer.d@physioclinic.com', '9822012345', 'THERAPIST', TRUE),
(3, 'dr.ananya', '63bbe564aa5c0882355f6dcd6f2241b6f8d02d6f39725966ae564cd310f07850', 'Dr. Ananya Sharma, MPT (Sports)', 'ananya.s@physioclinic.com', '9822098765', 'THERAPIST', TRUE),
(4, 'receptionist', 'dfd48f36338aa36228ebb9e204bba6b4e18db0b623e25c458901edc831fb18e9', 'Pooja Kulkarni (Front Desk)', 'desk@physioclinic.com', '9822055555', 'RECEPTIONIST', TRUE),
(5, 'rahul.v', '6dff47814e92e3a14c9f021d2d3ddd353a03e5bf3b0f49d53e80a1a3606c86d6', 'Rahul Verma', 'rahul.verma@gmail.com', '9890112233', 'PATIENT', TRUE),
(6, 'priya.p', '6dff47814e92e3a14c9f021d2d3ddd353a03e5bf3b0f49d53e80a1a3606c86d6', 'Priya Patil', 'priya.patil@gmail.com', '9890223344', 'PATIENT', TRUE),
(7, 'amit.k', '6dff47814e92e3a14c9f021d2d3ddd353a03e5bf3b0f49d53e80a1a3606c86d6', 'Amit Kulkarni', 'amit.k@gmail.com', '9890334455', 'PATIENT', TRUE);

-- ----------------------------------------------------------------------------
-- 2. THERAPISTS
-- ----------------------------------------------------------------------------
INSERT INTO therapists (id, user_id, specialization, license_number, experience_years, consultation_fee, available_days, slot_duration_minutes) VALUES
(1, 2, 'Orthopedic & Spine Rehabilitation', 'MH-PT-2012-0894', 12, 800.00, 'MON,TUE,WED,THU,FRI,SAT', 45),
(2, 3, 'Sports Injury & Return-to-Play', 'MH-PT-2016-1422', 8, 950.00, 'MON,TUE,WED,THU,FRI', 45);

-- ----------------------------------------------------------------------------
-- 3. PATIENTS
-- ----------------------------------------------------------------------------
INSERT INTO patients (id, user_id, date_of_birth, gender, blood_group, emergency_contact_name, emergency_contact_phone, primary_complaint, medical_history) VALUES
(1, 5, '1992-04-15', 'MALE', 'B+', 'Sunita Verma', '9890119988', 'Lower back stiffness and pain radiating down left thigh for 4 weeks.', 'No prior surgeries. Sedentary software job with 10+ hours screen time.'),
(2, 6, '2000-08-22', 'FEMALE', 'O+', 'Ramesh Patil', '9890228877', 'Post-Op Week 6: Left Knee Anterior Cruciate Ligament (ACL) Reconstruction.', 'Basketball athlete. Underwent arthroscopic ACL repair 6 weeks ago.'),
(3, 7, '1966-11-05', 'MALE', 'A+', 'Smita Kulkarni', '9890337766', 'Severe right shoulder pain with restricted overhead reach for 3 months.', 'Diagnosed with Type-2 Diabetes; Adhesive Capsulitis (Frozen Shoulder Stage 2).');

-- ----------------------------------------------------------------------------
-- 4. TREATMENT_PLANS
-- ----------------------------------------------------------------------------
INSERT INTO treatment_plans (id, patient_id, therapist_id, diagnosis, treatment_goal, start_date, estimated_end_date, target_sessions, completed_sessions, status) VALUES
(1, 1, 1, 'Lumbar Disc Bulge (L4-L5) with Radiculopathy', 'Eliminate radiating nerve pain, restore lumbar core stability, and achieve pain-free sitting.', '2026-10-01', '2026-11-30', 10, 4, 'ACTIVE'),
(2, 2, 2, 'Post-Op ACL Reconstruction (Phase 2 Rehabilitation)', 'Restore full active extension, achieve 120-degree flexion, and initiate closed-chain strengthening.', '2026-10-15', '2026-12-15', 16, 8, 'ACTIVE'),
(3, 3, 1, 'Adhesive Capsulitis (Right Frozen Shoulder - Resolving Phase)', 'Restore glenohumeral mobility to 160 deg flexion, reduce nocturnal pain to 0/10.', '2026-09-01', '2026-10-25', 8, 8, 'COMPLETED');

-- ----------------------------------------------------------------------------
-- 5. EXERCISES
-- ----------------------------------------------------------------------------
INSERT INTO exercises (id, name, target_muscle_group, category, instructions, default_sets, default_reps, default_hold_seconds) VALUES
(1, 'Pelvic Bridging with Ball Squeeze', 'Gluteus Maximus & Core Stabilizers', 'STRENGTH', 'Lie supine with knees bent. Squeeze medicine ball between knees, lift hips into bridge, hold and lower slowly.', 3, 12, 5),
(2, 'Prone Cobra Extension', 'Thoracic Extensors & Rhomboids', 'MOBILITY', 'Lie prone on mat. Elevate chest slightly, rotate palms outward, squeeze shoulder blades together without straining neck.', 3, 10, 5),
(3, 'Cat-Camel Spinal Flossing', 'Multifidus & Erector Spinae', 'MOBILITY', 'Quadruped position on all fours. Alternate gently between arching back upward and dipping toward floor.', 2, 10, 3),
(4, 'Isometric Quadriceps Sets', 'Quadriceps Femoris (VMO)', 'STRENGTH', 'Sit with leg extended. Press knee down into rolled towel, contracting thigh muscle firmly without lifting heel.', 3, 15, 6),
(5, 'Closed-Chain Terminal Knee Extension', 'Vastus Medialis Oblique', 'STRENGTH', 'Loop resistance band behind knee attached to stable anchor. Step back into slight bend, straighten knee against band.', 3, 12, 3),
(6, 'Wall Crawl Shoulder Flexion', 'Anterior Deltoid & Rotator Cuff', 'FLEXIBILITY', 'Face wall at arms length. Walk fingers upward slowly to tolerance, pause at peak elevation, slide down gently.', 3, 10, 5),
(7, 'Pendulum Shoulder Swings (Codman)', 'Glenohumeral Joint Capsule', 'MOBILITY', 'Lean forward with unaffected arm on table. Allow affected arm to hang freely, initiate gentle circular swinging momentum.', 2, 20, 0),
(8, 'Side-Lying Clamshells with Band', 'Gluteus Medius & Pelvic Stabilizers', 'STRENGTH', 'Lie on unaffected side with knees bent at 90 deg. Keep heels pressed together while rotating upper knee upward.', 3, 15, 3);

-- ----------------------------------------------------------------------------
-- 6. PLAN_EXERCISES (Prescribed HEP)
-- ----------------------------------------------------------------------------
INSERT INTO plan_exercises (id, plan_id, exercise_id, prescribed_sets, prescribed_reps, prescribed_hold_seconds, frequency_per_day, special_notes) VALUES
(1, 1, 1, 3, 12, 5, 2, 'Maintain steady diaphragmatic breathing; do not arch lower back excessively.'),
(2, 1, 2, 3, 10, 5, 2, 'Focus on mid-back squeeze, avoid neck hyperextension.'),
(3, 1, 3, 2, 10, 3, 3, 'Perform morning and evening to relieve stiffness.'),
(4, 2, 4, 3, 15, 6, 3, 'Critical for quad activation; ensure patella tracks straight.'),
(5, 2, 5, 3, 12, 3, 2, 'Use yellow medium resistance band; maintain upright posture.'),
(6, 2, 8, 3, 15, 3, 2, 'Do not roll pelvis backward during hip abduction.'),
(7, 3, 6, 3, 10, 5, 2, 'Walk fingers only to point of mild stretch, never sharp pain.'),
(8, 3, 7, 2, 20, 0, 3, 'Pure passive momentum; keep arm and shoulder muscles relaxed.');

-- ----------------------------------------------------------------------------
-- 7. PACKAGES_INVOICES
-- ----------------------------------------------------------------------------
INSERT INTO packages_invoices (id, patient_id, package_name, total_sessions, remaining_sessions, total_amount, discount_amount, net_amount, payment_status, payment_method) VALUES
(1, 1, 'Spine Rehabilitation Package (10 Sessions)', 10, 6, 8000.00, 800.00, 7200.00, 'PAID', 'UPI'),
(2, 2, 'Comprehensive ACL Return-to-Sport (16 Sessions)', 16, 8, 15200.00, 2200.00, 13000.00, 'PAID', 'CARD'),
(3, 3, 'Frozen Shoulder Mobility Protocol (8 Sessions)', 8, 0, 6400.00, 400.00, 6000.00, 'PAID', 'CASH');

-- ----------------------------------------------------------------------------
-- 8. SESSIONS
-- ----------------------------------------------------------------------------
INSERT INTO sessions (id, plan_id, therapist_id, patient_id, session_date, start_time, end_time, status, package_invoice_id) VALUES
-- Patient 1 (Rahul) Past Attended Sessions
(1, 1, 1, 1, '2026-10-05', '09:00:00', '09:45:00', 'ATTENDED', 1),
(2, 1, 1, 1, '2026-10-12', '09:00:00', '09:45:00', 'ATTENDED', 1),
(3, 1, 1, 1, '2026-10-19', '09:00:00', '09:45:00', 'ATTENDED', 1),
(4, 1, 1, 1, '2026-10-26', '09:00:00', '09:45:00', 'ATTENDED', 1),
-- Patient 1 Upcoming Scheduled Sessions
(5, 1, 1, 1, '2026-11-20', '09:00:00', '09:45:00', 'SCHEDULED', 1),
(6, 1, 1, 1, '2026-11-25', '09:00:00', '09:45:00', 'SCHEDULED', 1),

-- Patient 2 (Priya - ACL) Sessions
(7, 2, 2, 2, '2026-10-16', '10:00:00', '10:45:00', 'ATTENDED', 2),
(8, 2, 2, 2, '2026-10-23', '10:00:00', '10:45:00', 'ATTENDED', 2),
(9, 2, 2, 2, '2026-10-30', '10:00:00', '10:45:00', 'ATTENDED', 2),
(10, 2, 2, 2, '2026-11-06', '10:00:00', '10:45:00', 'ATTENDED', 2),
(11, 2, 2, 2, '2026-11-13', '10:00:00', '10:45:00', 'ATTENDED', 2),
(12, 2, 2, 2, '2026-11-20', '10:00:00', '10:45:00', 'SCHEDULED', 2),

-- Patient 3 (Amit - Completed Package)
(13, 3, 1, 3, '2026-09-05', '11:00:00', '11:45:00', 'ATTENDED', 3),
(14, 3, 1, 3, '2026-09-12', '11:00:00', '11:45:00', 'ATTENDED', 3),
(15, 3, 1, 3, '2026-09-19', '11:00:00', '11:45:00', 'ATTENDED', 3),
(16, 3, 1, 3, '2026-10-24', '11:00:00', '11:45:00', 'ATTENDED', 3);

-- ----------------------------------------------------------------------------
-- 9. SOAP_NOTES (Clinical Evidence for Attended Sessions)
-- ----------------------------------------------------------------------------
INSERT INTO soap_notes (id, session_id, pain_score_before, pain_score_after, range_of_motion_notes, muscle_strength_grade, subjective_notes, objective_findings, assessment_summary, plan_next_session, modalities_used) VALUES
(1, 1, 8, 5, 'Lumbar flexion limited to 45 deg by pain; SLR positive at 40 deg left.', '3+/5 MMT', 
'Patient reports constant aching pain in lower back with tingling radiating to left calf. Aggravated by prolonged desk sitting.', 
'Palpable spasm in bilateral erector spinae and left quadratus lumborum. Tender L4-L5 interspinous ligament.', 
'Acute lumbar radiculopathy secondary to L4-L5 disc protrusion. High irritability level.', 
'Initiate McKenzie extension in lying, lumbar traction, and ice application.', 
'Mechanical Lumbar Traction (18kg), Cryotherapy, Interferential Therapy (IFT) 4-pole 100Hz'),

(2, 2, 7, 4, 'Lumbar flexion improved to 55 deg; SLR negative till 55 deg.', '4-/5 MMT', 
'Morning stiffness duration decreased from 45 mins to 20 mins. Radiating tingling no longer reaches calf.', 
'Reduced paraspinal muscle guarding. Core abdominal engagement improved during basic pelvic tilt test.', 
'Radicular symptoms centralizing favorably. Decreasing neurodynamic irritability.', 
'Progress from static pelvic tilts to dynamic bridging with ball squeeze.', 
'IFT 4-pole, Moist Heat Pack, Manual Lumbar Mobilization Maitland Grade II'),

(3, 3, 5, 3, 'Lumbar flexion 70 deg without radicular pain; full lumbar extension.', '4/5 MMT', 
'Patient able to sit for 45 minutes continuously without tingling. Felt mild soreness after evening walk.', 
'No neural tension signs on Slump test. Multifidus activation palpated during prone alternate arm/leg lift.', 
'Subacute phase progressing on schedule. Significant pain relief and functional restoration.', 
'Advance to prone cobra extensions and standing resistance band pallof presses.', 
'Ultrasound Therapy 1MHz (1.2 W/cm2 continuous), Deep Tissue Myofascial Release'),

(4, 4, 4, 2, 'Full lumbar range of motion achieved; SLR 80 deg bilateral equal.', '4+/5 MMT', 
'No resting pain. Only minor tightness at end-range flexion after a full workday.', 
'Normal spinal curvature restored. Excellent abdominal brace control during single-leg bridge.', 
'Core endurance improving. Patient ready for ergonomic workstation transition.', 
'Begin functional lifting mechanics and progressive core endurance training.', 
'IFT 4-pole, Dynamic Core Stabilization Drills');
