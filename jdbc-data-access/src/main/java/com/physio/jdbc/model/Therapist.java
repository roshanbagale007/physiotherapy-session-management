package com.physio.jdbc.model;

import java.math.BigDecimal;

/**
 * Model representing a Physiotherapist (Doctor).
 */
public class Therapist {
    private Long id;
    private Long userId;
    private String fullName;      // Joined from users
    private String email;         // Joined from users
    private String specialization;
    private String licenseNumber;
    private int experienceYears;
    private BigDecimal consultationFee;
    private String availableDays;
    private int slotDurationMinutes;

    public Therapist() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public BigDecimal getConsultationFee() { return consultationFee; }
    public void setConsultationFee(BigDecimal consultationFee) { this.consultationFee = consultationFee; }

    public String getAvailableDays() { return availableDays; }
    public void setAvailableDays(String availableDays) { this.availableDays = availableDays; }

    public int getSlotDurationMinutes() { return slotDurationMinutes; }
    public void setSlotDurationMinutes(int slotDurationMinutes) { this.slotDurationMinutes = slotDurationMinutes; }

    @Override
    public String toString() {
        return String.format("Therapist[ID=%d, Name='%s', Spec='%s', Exp=%d yrs, Fee=₹%.2f]",
                id, fullName, specialization, experienceYears, consultationFee);
    }
}
