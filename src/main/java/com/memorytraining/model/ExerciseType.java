package com.memorytraining.model;

/**
 * Enum representing different types of memory exercises
 * Based on the original Lost Art memory training app
 */
public enum ExerciseType {
    WORD_MEMORY("Word Memory", "Remember and recall sequences of words"),
    NUMBER_MEMORY("Number Memory", "Remember and recall sequences of numbers"),
    COLOR_MEMORY("Color Memory", "Remember and recall color patterns"),
    SEQUENCE_MEMORY("Sequence Memory", "Remember and recall sequences of actions"),
    SPATIAL_MEMORY("Spatial Memory", "Remember and recall spatial arrangements");
    
    private final String displayName;
    private final String description;
    
    ExerciseType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}