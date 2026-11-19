package com.physio.jdbc.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model representing a Physiotherapy Patient intake profile.
 */
public class Patient {
    private Long id;
    private Long userId;
    private String fullName; // Joined from users
    private String email;    // Joined from users
    private String phone;    // Joined from users
    private Date dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String primaryComplaint;
    private String medicalHistory;
    private Timestamp registeredAt;

    public Patient() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }

    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }

    public String getPrimaryComplaint() { return primaryComplaint; }
    public void setPrimaryComplaint(String primaryComplaint) { this.primaryComplaint = primaryComplaint; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public Timestamp getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(Timestamp registeredAt) { this.registeredAt = registeredAt; }

    @Override
    public String toString() {
        return String.format("Patient[ID=%d, Name='%s', Gender=%s, BloodGroup=%s, Complaint='%s']",
                id, fullName, gender, bloodGroup, primaryComplaint);
    }
}
