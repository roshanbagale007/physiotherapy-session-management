package com.physio.hibernate.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Unit 5.2 & 5.4: Master Clinical Exercise Entity with Second-Level Cache (Ehcache).
 */
@Entity
@Table(name = "exercises")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "com.physio.hibernate.model.Exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "target_muscle_group", nullable = false, length = 100)
    private String targetMuscleGroup;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "default_sets")
    private Integer defaultSets = 3;

    @Column(name = "default_reps")
    private Integer defaultReps = 10;

    @Column(name = "default_hold_seconds")
    private Integer defaultHoldSeconds = 0;

    public Exercise() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTargetMuscleGroup() { return targetMuscleGroup; }
    public void setTargetMuscleGroup(String targetMuscleGroup) { this.targetMuscleGroup = targetMuscleGroup; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public Integer getDefaultSets() { return defaultSets; }
    public void setDefaultSets(Integer defaultSets) { this.defaultSets = defaultSets; }

    public Integer getDefaultReps() { return defaultReps; }
    public void setDefaultReps(Integer defaultReps) { this.defaultReps = defaultReps; }

    public Integer getDefaultHoldSeconds() { return defaultHoldSeconds; }
    public void setDefaultHoldSeconds(Integer defaultHoldSeconds) { this.defaultHoldSeconds = defaultHoldSeconds; }

    @Override
    public String toString() {
        return String.format("Exercise #%d: %s [%s | %s] (%d sets x %d reps, %ds hold)",
                id, name, targetMuscleGroup, category, defaultSets, defaultReps, defaultHoldSeconds);
    }
}
