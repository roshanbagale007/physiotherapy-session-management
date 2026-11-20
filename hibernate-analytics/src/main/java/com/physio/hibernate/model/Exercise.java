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

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "target_muscle_group", nullable = false, length = 100)
    private String targetMuscleGroup;

    @Column(name = "difficulty_level", length = 20)
    private String difficultyLevel = "BEGINNER";

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "video_url", length = 255)
    private String videoUrl;

    public Exercise() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTargetMuscleGroup() { return targetMuscleGroup; }
    public void setTargetMuscleGroup(String targetMuscleGroup) { this.targetMuscleGroup = targetMuscleGroup; }

    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    @Override
    public String toString() {
        return String.format("Exercise #%d: %s [%s | %s]", id, name, targetMuscleGroup, difficultyLevel);
    }
}
