package com.memorytraining.model;

import java.time.LocalDateTime;

/**
 * Represents a score from a completed memory exercise
 */
public class Score {
    private ExerciseType exerciseType;
    private double score;
    private int level;
    private long timeSpentMs;
    private int correctAnswers;
    private int totalQuestions;
    private LocalDateTime completedAt;
    private double accuracy;
    
    public Score() {
        this.completedAt = LocalDateTime.now();
    }
    
    public Score(ExerciseType exerciseType, double score, int level, 
                 long timeSpentMs, int correctAnswers, int totalQuestions) {
        this();
        this.exerciseType = exerciseType;
        this.score = score;
        this.level = level;
        this.timeSpentMs = timeSpentMs;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.accuracy = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;
    }
    
    // Getters and Setters
    public ExerciseType getExerciseType() { return exerciseType; }
    public void setExerciseType(ExerciseType exerciseType) { this.exerciseType = exerciseType; }
    
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public long getTimeSpentMs() { return timeSpentMs; }
    public void setTimeSpentMs(long timeSpentMs) { this.timeSpentMs = timeSpentMs; }
    
    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { 
        this.correctAnswers = correctAnswers; 
        updateAccuracy();
    }
    
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { 
        this.totalQuestions = totalQuestions; 
        updateAccuracy();
    }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public double getAccuracy() { return accuracy; }
    
    private void updateAccuracy() {
        this.accuracy = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;
    }
    
    // Utility methods
    public String getFormattedTime() {
        long seconds = timeSpentMs / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    public String getFormattedAccuracy() {
        return String.format("%.1f%%", accuracy);
    }
    
    @Override
    public String toString() {
        return String.format("%s - Score: %.1f, Level: %d, Accuracy: %s, Time: %s",
                exerciseType.getDisplayName(), score, level, getFormattedAccuracy(), getFormattedTime());
    }
}