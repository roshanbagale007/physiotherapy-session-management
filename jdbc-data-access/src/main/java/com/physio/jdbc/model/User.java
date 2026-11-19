package com.physio.jdbc.model;

import java.sql.Timestamp;

/**
 * Model representing a user in the Physiotherapy Clinic.
 * Supports RBAC: ADMIN, THERAPIST, RECEPTIONIST, PATIENT.
 */
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private boolean active;
    private Timestamp createdAt;

    public User() {}

    public User(Long id, String username, String passwordHash, String fullName, String email, String phone, String role, boolean active) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("User[ID=%d, Name='%s', Role=%s, Email='%s']", id, fullName, role, email);
    }
}
