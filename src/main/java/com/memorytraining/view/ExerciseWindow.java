package com.memorytraining.view;

import com.memorytraining.model.ExerciseType;
import com.memorytraining.model.Score;
import com.memorytraining.service.ExerciseService;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Window for conducting memory exercises
 */
public class ExerciseWindow {
    private Stage stage;
    private ExerciseType exerciseType;
    private int level;
    private ExerciseService exerciseService;
    private Consumer<Score> onCompleted;
    
    private VBox contentPane;
    private Label instructionLabel;
    private Label sequenceLabel;
    private VBox inputPane;
    private Button submitButton;
    private ProgressBar progressBar;
    
    private List<?> currentSequence;
    private List<String> userInputs;
    private long startTime;
    private boolean inInputPhase;
    private int currentInputIndex;
    
    public ExerciseWindow(ExerciseType exerciseType, int level, 
                         ExerciseService exerciseService, Consumer<Score> onCompleted) {
        this.exerciseType = exerciseType;
        this.level = level;
        this.exerciseService = exerciseService;
        this.onCompleted = onCompleted;
        this.userInputs = new ArrayList<>();
        
        initializeStage();
        initializeComponents();
        setupLayout();
    }
    
    private void initializeStage() {
        stage = new Stage();
        stage.setTitle(exerciseType.getDisplayName() + " - Level " + level);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setWidth(600);
        stage.setHeight(500);
    }
    
    private void initializeComponents() {
        contentPane = new VBox(20);
        contentPane.setPadding(new Insets(30));
        contentPane.setAlignment(Pos.CENTER);
        
        // Header
        Label titleLabel = new Label(exerciseType.getDisplayName());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Label levelLabel = new Label("Level " + level);
        levelLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        
        // Instructions
        instructionLabel = new Label();
        instructionLabel.setWrapText(true);
        instructionLabel.setAlignment(Pos.CENTER);
        instructionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        
        // Sequence display
        sequenceLabel = new Label();
        sequenceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sequenceLabel.setAlignment(Pos.CENTER);
        sequenceLabel.setWrapText(true);
        sequenceLabel.setMaxWidth(500);
        
        // Input area
        inputPane = new VBox(10);
        inputPane.setAlignment(Pos.CENTER);
        
        // Progress bar
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);
        
        // Submit button
        submitButton = new Button("Submit Answer");
        submitButton.setOnAction(e -> submitInput());
        submitButton.setDisable(true);
        
        contentPane.getChildren().addAll(
            titleLabel, levelLabel, instructionLabel, 
            sequenceLabel, inputPane, progressBar, submitButton
        );
    }
    
    private void setupLayout() {
        Scene scene = new Scene(contentPane);
        stage.setScene(scene);
    }
    
    public void show(Window parent) {
        if (parent != null) {
            stage.initOwner(parent);
        }
        stage.show();
        startExercise();
    }
    
    private void startExercise() {
        // Generate sequence based on exercise type
        switch (exerciseType) {
            case WORD_MEMORY -> currentSequence = exerciseService.generateWordSequence(level);
            case NUMBER_MEMORY -> currentSequence = exerciseService.generateNumberSequence(level);
            case COLOR_MEMORY -> currentSequence = exerciseService.generateColorSequence(level);
            case SPATIAL_MEMORY -> currentSequence = exerciseService.generateSpatialSequence(level);
            case SEQUENCE_MEMORY -> currentSequence = exerciseService.generateWordSequence(level); // Simplified
        }
        
        // Show instructions
        showInstructions();
        
        // Start sequence display after a brief pause
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> showSequence());
        pause.play();
    }
    
    private void showInstructions() {
        String instructions = switch (exerciseType) {
            case WORD_MEMORY -> "Memorize the sequence of words that will appear. Then type them back in the correct order.";
            case NUMBER_MEMORY -> "Memorize the sequence of numbers that will appear. Then type them back in the correct order.";
            case COLOR_MEMORY -> "Memorize the sequence of colors that will appear. Then select them in the correct order.";
            case SPATIAL_MEMORY -> "Memorize the positions that light up. Then click them in the correct order.";
            case SEQUENCE_MEMORY -> "Memorize the sequence of actions. Then repeat them in the correct order.";
        };
        
        instructionLabel.setText(instructions);
        sequenceLabel.setText("Get ready...");
    }
    
    private void showSequence() {
        startTime = System.currentTimeMillis();
        
        int displayTime = exerciseService.getDisplayTimeMs(exerciseType, level, currentSequence.size());
        int itemTime = displayTime / currentSequence.size();
        
        showSequenceItem(0, itemTime);
    }
    
    private void showSequenceItem(int index, int itemTime) {
        if (index >= currentSequence.size()) {
            startInputPhase();
            return;
        }
        
        Object item = currentSequence.get(index);
        sequenceLabel.setText(item.toString());
        
        double progress = (double) (index + 1) / currentSequence.size();
        progressBar.setProgress(progress);
        
        PauseTransition pause = new PauseTransition(Duration.millis(itemTime));
        pause.setOnFinished(e -> {
            if (index < currentSequence.size() - 1) {
                sequenceLabel.setText("...");
                PauseTransition shortPause = new PauseTransition(Duration.millis(200));
                shortPause.setOnFinished(e2 -> showSequenceItem(index + 1, itemTime));
                shortPause.play();
            } else {
                showSequenceItem(index + 1, itemTime);
            }
        });
        pause.play();
    }
    
    private void startInputPhase() {
        inInputPhase = true;
        currentInputIndex = 0;
        
        instructionLabel.setText("Now enter the sequence in the correct order:");
        sequenceLabel.setText("");
        progressBar.setProgress(0);
        
        createInputControls();
        submitButton.setDisable(false);
    }
    
    private void createInputControls() {
        inputPane.getChildren().clear();
        
        if (exerciseType == ExerciseType.COLOR_MEMORY) {
            createColorButtons();
        } else if (exerciseType == ExerciseType.SPATIAL_MEMORY) {
            createSpatialGrid();
        } else {
            createTextInput();
        }
    }
    
    private void createTextInput() {
        TextField textField = new TextField();
        textField.setPromptText("Enter item #" + (currentInputIndex + 1));
        textField.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        textField.setMaxWidth(300);
        
        textField.setOnAction(e -> {
            String input = textField.getText().trim();
            if (!input.isEmpty()) {
                userInputs.add(input);
                currentInputIndex++;
                
                if (currentInputIndex < currentSequence.size()) {
                    textField.clear();
                    textField.setPromptText("Enter item #" + (currentInputIndex + 1));
                    updateProgress();
                } else {
                    textField.setDisable(true);
                    submitButton.setText("Complete Exercise");
                    updateProgress();
                }
            }
        });
        
        inputPane.getChildren().add(textField);
        Platform.runLater(textField::requestFocus);
    }
    
    private void createColorButtons() {
        // Simplified color selection
        String[] colors = {"Red", "Blue", "Green", "Yellow", "Orange", "Purple", "Pink", "Brown"};
        
        GridPane colorGrid = new GridPane();
        colorGrid.setHgap(10);
        colorGrid.setVgap(10);
        colorGrid.setAlignment(Pos.CENTER);
        
        int col = 0, row = 0;
        for (String color : colors) {
            Button colorButton = new Button(color);
            colorButton.setPrefSize(80, 40);
            colorButton.setOnAction(e -> selectColor(color));
            
            colorGrid.add(colorButton, col, row);
            col++;
            if (col >= 4) {
                col = 0;
                row++;
            }
        }
        
        Label progressLabel = new Label("Select color #" + (currentInputIndex + 1));
        progressLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        
        inputPane.getChildren().addAll(progressLabel, colorGrid);
    }
    
    private void createSpatialGrid() {
        // Simplified spatial grid
        GridPane spatialGrid = new GridPane();
        spatialGrid.setHgap(5);
        spatialGrid.setVgap(5);
        spatialGrid.setAlignment(Pos.CENTER);
        
        int gridSize = Math.min(3 + (level / 2), 6);
        
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Button gridButton = new Button();
                gridButton.setPrefSize(50, 50);
                gridButton.setStyle("-fx-background-color: lightgray;");
                
                final int finalRow = row, finalCol = col;
                gridButton.setOnAction(e -> selectPosition(finalCol, finalRow));
                
                spatialGrid.add(gridButton, col, row);
            }
        }
        
        Label progressLabel = new Label("Click position #" + (currentInputIndex + 1));
        progressLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        
        inputPane.getChildren().addAll(progressLabel, spatialGrid);
    }
    
    private void selectColor(String color) {
        userInputs.add(color);
        currentInputIndex++;
        
        if (currentInputIndex < currentSequence.size()) {
            updateColorProgress();
        } else {
            submitButton.setText("Complete Exercise");
            updateProgress();
        }
    }
    
    private void selectPosition(int x, int y) {
        userInputs.add(x + "," + y);
        currentInputIndex++;
        
        if (currentInputIndex < currentSequence.size()) {
            updateSpatialProgress();
        } else {
            submitButton.setText("Complete Exercise");
            updateProgress();
        }
    }
    
    private void updateColorProgress() {
        inputPane.getChildren().clear();
        createColorButtons();
        updateProgress();
    }
    
    private void updateSpatialProgress() {
        inputPane.getChildren().clear();
        createSpatialGrid();
        updateProgress();
    }
    
    private void updateProgress() {
        double progress = (double) currentInputIndex / currentSequence.size();
        progressBar.setProgress(progress);
    }
    
    private void submitInput() {
        if (currentInputIndex < currentSequence.size()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete");
            alert.setHeaderText(null);
            alert.setContentText("Please complete all items in the sequence.");
            alert.showAndWait();
            return;
        }
        
        completeExercise();
    }
    
    private void completeExercise() {
        long endTime = System.currentTimeMillis();
        long timeSpent = endTime - startTime;
        
        // Calculate score
        int correctAnswers = calculateCorrectAnswers();
        int totalQuestions = currentSequence.size();
        
        double scoreValue = exerciseService.calculateScore(
            correctAnswers, totalQuestions, timeSpent, level, exerciseType
        );
        
        Score score = new Score(exerciseType, scoreValue, level, timeSpent, 
                               correctAnswers, totalQuestions);
        
        showResults(score);
        
        if (onCompleted != null) {
            onCompleted.accept(score);
        }
        
        stage.close();
    }
    
    private int calculateCorrectAnswers() {
        int correct = 0;
        
        for (int i = 0; i < Math.min(userInputs.size(), currentSequence.size()); i++) {
            String userInput = userInputs.get(i);
            String expectedAnswer = currentSequence.get(i).toString();
            
            if (exerciseType == ExerciseType.SPATIAL_MEMORY) {
                // Special handling for spatial positions
                ExerciseService.Position expectedPos = (ExerciseService.Position) currentSequence.get(i);
                String expectedPosStr = expectedPos.getX() + "," + expectedPos.getY();
                if (userInput.equals(expectedPosStr)) {
                    correct++;
                }
            } else if (userInput.equalsIgnoreCase(expectedAnswer)) {
                correct++;
            }
        }
        
        return correct;
    }
    
    private void showResults(Score score) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exercise Complete");
        alert.setHeaderText("Results for " + exerciseType.getDisplayName());
        
        String message = String.format(
            "Score: %.1f points\n" +
            "Accuracy: %s\n" +
            "Time: %s\n" +
            "Level: %d\n\n" +
            "Correct answers: %d/%d",
            score.getScore(),
            score.getFormattedAccuracy(),
            score.getFormattedTime(),
            score.getLevel(),
            score.getCorrectAnswers(),
            score.getTotalQuestions()
        );
        
        alert.setContentText(message);
        alert.showAndWait();
    }
}