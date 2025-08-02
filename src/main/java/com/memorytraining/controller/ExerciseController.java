package com.memorytraining.controller;

import com.memorytraining.model.ExerciseType;
import com.memorytraining.model.Score;
import com.memorytraining.service.DataService;
import com.memorytraining.service.ExerciseService;
import com.memorytraining.view.ExerciseWindow;
import javafx.stage.Window;

/**
 * Controller for managing exercise execution
 */
public class ExerciseController {
    private DataService dataService;
    private ExerciseService exerciseService;
    
    public ExerciseController(DataService dataService) {
        this.dataService = dataService;
        this.exerciseService = new ExerciseService();
    }
    
    public void startExercise(ExerciseType exerciseType, Window parentWindow) {
        // Get current level for this exercise type
        int currentLevel = getCurrentLevel(exerciseType);
        
        // Create and show exercise window
        ExerciseWindow exerciseWindow = new ExerciseWindow(
            exerciseType, currentLevel, exerciseService, this::onExerciseCompleted
        );
        
        exerciseWindow.show(parentWindow);
    }
    
    private int getCurrentLevel(ExerciseType exerciseType) {
        var user = dataService.getCurrentUser();
        if (user == null) return 1;
        
        // Get the best score for this exercise type to determine level
        Score bestScore = user.getBestScore(exerciseType);
        if (bestScore == null) {
            return user.getPreferences().getDefaultDifficulty();
        }
        
        // Use adaptive difficulty if enabled
        if (user.getPreferences().isAdaptiveDifficultyEnabled()) {
            return Math.min(bestScore.getLevel() + 1, 10);
        } else {
            return user.getPreferences().getDefaultDifficulty();
        }
    }
    
    private void onExerciseCompleted(Score score) {
        var user = dataService.getCurrentUser();
        if (user != null) {
            user.addScore(score);
            
            // Update adaptive level if enabled
            if (user.getPreferences().isAdaptiveDifficultyEnabled()) {
                updateAdaptiveLevel(score);
            }
            
            // Save data
            dataService.saveData();
        }
    }
    
    private void updateAdaptiveLevel(Score score) {
        // Calculate next level based on performance
        double accuracy = score.getAccuracy() / 100.0;
        long avgResponseTime = score.getTimeSpentMs() / score.getTotalQuestions();
        
        int nextLevel = exerciseService.calculateNextLevel(
            score.getLevel(), accuracy, avgResponseTime
        );
        
        // The next level calculation is already handled in the score
        // This method could be used for additional logic if needed
    }
}