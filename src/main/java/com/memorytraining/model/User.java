package com.memorytraining.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user of the memory training application
 */
public class User {
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private int totalExercisesCompleted;
    private int currentStreak;
    private int longestStreak;
    private List<Score> scores;
    private UserPreferences preferences;
    
    public User() {
        this.createdAt = LocalDateTime.now();
        this.lastLoginAt = LocalDateTime.now();
        this.scores = new ArrayList<>();
        this.preferences = new UserPreferences();
        this.totalExercisesCompleted = 0;
        this.currentStreak = 0;
        this.longestStreak = 0;
    }
    
    public User(String username) {
        this();
        this.username = username;
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    
    public int getTotalExercisesCompleted() { return totalExercisesCompleted; }
    public void setTotalExercisesCompleted(int totalExercisesCompleted) { 
        this.totalExercisesCompleted = totalExercisesCompleted; 
    }
    
    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }
    
    public int getLongestStreak() { return longestStreak; }
    public void setLongestStreak(int longestStreak) { this.longestStreak = longestStreak; }
    
    public List<Score> getScores() { return scores; }
    public void setScores(List<Score> scores) { this.scores = scores; }
    
    public UserPreferences getPreferences() { return preferences; }
    public void setPreferences(UserPreferences preferences) { this.preferences = preferences; }
    
    // Utility methods
    public void addScore(Score score) {
        this.scores.add(score);
        this.totalExercisesCompleted++;
    }
    
    public double getAverageScore(ExerciseType exerciseType) {
        return scores.stream()
                .filter(score -> score.getExerciseType() == exerciseType)
                .mapToDouble(Score::getScore)
                .average()
                .orElse(0.0);
    }
    
    public Score getBestScore(ExerciseType exerciseType) {
        return scores.stream()
                .filter(score -> score.getExerciseType() == exerciseType)
                .max((s1, s2) -> Double.compare(s1.getScore(), s2.getScore()))
                .orElse(null);
    }
}