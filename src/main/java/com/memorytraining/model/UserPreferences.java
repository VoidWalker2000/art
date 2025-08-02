package com.memorytraining.model;

/**
 * Stores user preferences and settings
 */
public class UserPreferences {
    private boolean soundEnabled;
    private boolean animationsEnabled;
    private int defaultDifficulty;
    private boolean adaptiveDifficultyEnabled;
    private String theme;
    private int reminderFrequencyDays;
    private boolean remindersEnabled;
    
    public UserPreferences() {
        // Default settings
        this.soundEnabled = true;
        this.animationsEnabled = true;
        this.defaultDifficulty = 1;
        this.adaptiveDifficultyEnabled = true;
        this.theme = "light";
        this.reminderFrequencyDays = 1;
        this.remindersEnabled = false;
    }
    
    // Getters and Setters
    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean soundEnabled) { this.soundEnabled = soundEnabled; }
    
    public boolean isAnimationsEnabled() { return animationsEnabled; }
    public void setAnimationsEnabled(boolean animationsEnabled) { this.animationsEnabled = animationsEnabled; }
    
    public int getDefaultDifficulty() { return defaultDifficulty; }
    public void setDefaultDifficulty(int defaultDifficulty) { this.defaultDifficulty = defaultDifficulty; }
    
    public boolean isAdaptiveDifficultyEnabled() { return adaptiveDifficultyEnabled; }
    public void setAdaptiveDifficultyEnabled(boolean adaptiveDifficultyEnabled) { 
        this.adaptiveDifficultyEnabled = adaptiveDifficultyEnabled; 
    }
    
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    
    public int getReminderFrequencyDays() { return reminderFrequencyDays; }
    public void setReminderFrequencyDays(int reminderFrequencyDays) { 
        this.reminderFrequencyDays = reminderFrequencyDays; 
    }
    
    public boolean isRemindersEnabled() { return remindersEnabled; }
    public void setRemindersEnabled(boolean remindersEnabled) { this.remindersEnabled = remindersEnabled; }
}