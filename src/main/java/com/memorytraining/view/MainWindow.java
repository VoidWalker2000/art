package com.memorytraining.view;

import com.memorytraining.controller.ExerciseController;
import com.memorytraining.model.ExerciseType;
import com.memorytraining.service.DataService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Main window of the Lost Art Memory Training application
 */
public class MainWindow {
    private BorderPane root;
    private DataService dataService;
    private ExerciseController exerciseController;
    private Label welcomeLabel;
    private VBox statsContainer;
    
    public MainWindow(DataService dataService) {
        this.dataService = dataService;
        this.exerciseController = new ExerciseController(dataService);
        initializeComponents();
        setupLayout();
        updateUserInfo();
    }
    
    private void initializeComponents() {
        root = new BorderPane();
        root.getStyleClass().add("main-window");
        
        // Header
        VBox header = createHeader();
        root.setTop(header);
        
        // Main content
        ScrollPane centerPane = createMainContent();
        root.setCenter(centerPane);
        
        // Footer
        HBox footer = createFooter();
        root.setBottom(footer);
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.getStyleClass().add("header");
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER);
        
        // App title
        Label titleLabel = new Label("Lost Art");
        titleLabel.getStyleClass().add("app-title");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        
        Label subtitleLabel = new Label("Memory Training");
        subtitleLabel.getStyleClass().add("app-subtitle");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        
        welcomeLabel = new Label();
        welcomeLabel.getStyleClass().add("welcome-label");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        
        header.getChildren().addAll(titleLabel, subtitleLabel, welcomeLabel);
        return header;
    }
    
    private ScrollPane createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        // Description
        Label descLabel = new Label("Improve your memory with scientifically proven exercises.\nTrain your brain daily and see your memory capabilities expand beyond what they once were.");
        descLabel.getStyleClass().add("description-label");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(600);
        descLabel.setAlignment(Pos.CENTER);
        descLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        
        // Exercise selection
        VBox exerciseSection = createExerciseSection();
        
        // Statistics section
        statsContainer = createStatsSection();
        
        mainContent.getChildren().addAll(descLabel, exerciseSection, statsContainer);
        
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("main-content");
        
        return scrollPane;
    }
    
    private VBox createExerciseSection() {
        VBox exerciseSection = new VBox(15);
        exerciseSection.setAlignment(Pos.CENTER);
        exerciseSection.setMaxWidth(800);
        
        Label sectionTitle = new Label("Choose Your Exercise");
        sectionTitle.getStyleClass().add("section-title");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        GridPane exerciseGrid = new GridPane();
        exerciseGrid.setHgap(15);
        exerciseGrid.setVgap(15);
        exerciseGrid.setAlignment(Pos.CENTER);
        
        // Create exercise cards
        int col = 0, row = 0;
        for (ExerciseType type : ExerciseType.values()) {
            VBox exerciseCard = createExerciseCard(type);
            exerciseGrid.add(exerciseCard, col, row);
            
            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }
        
        exerciseSection.getChildren().addAll(sectionTitle, exerciseGrid);
        return exerciseSection;
    }
    
    private VBox createExerciseCard(ExerciseType type) {
        VBox card = new VBox(10);
        card.getStyleClass().add("exercise-card");
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(250);
        card.setMinHeight(150);
        
        Label titleLabel = new Label(type.getDisplayName());
        titleLabel.getStyleClass().add("exercise-title");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Label descLabel = new Label(type.getDescription());
        descLabel.getStyleClass().add("exercise-description");
        descLabel.setWrapText(true);
        descLabel.setAlignment(Pos.CENTER);
        descLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        
        Button startButton = new Button("Start Training");
        startButton.getStyleClass().add("start-button");
        startButton.setOnAction(e -> startExercise(type));
        
        card.getChildren().addAll(titleLabel, descLabel, startButton);
        
        // Add hover effect
        card.setOnMouseEntered(e -> card.getStyleClass().add("exercise-card-hover"));
        card.setOnMouseExited(e -> card.getStyleClass().remove("exercise-card-hover"));
        
        return card;
    }
    
    private VBox createStatsSection() {
        VBox statsSection = new VBox(15);
        statsSection.setAlignment(Pos.CENTER);
        statsSection.setMaxWidth(600);
        
        Label sectionTitle = new Label("Your Progress");
        sectionTitle.getStyleClass().add("section-title");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(10);
        statsGrid.setAlignment(Pos.CENTER);
        
        statsSection.getChildren().addAll(sectionTitle, statsGrid);
        return statsSection;
    }
    
    private HBox createFooter() {
        HBox footer = new HBox(20);
        footer.getStyleClass().add("footer");
        footer.setPadding(new Insets(15));
        footer.setAlignment(Pos.CENTER);
        
        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> openSettings());
        
        Button aboutButton = new Button("About");
        aboutButton.setOnAction(e -> showAbout());
        
        footer.getChildren().addAll(settingsButton, aboutButton);
        return footer;
    }
    
    private void setupLayout() {
        // Additional layout configuration if needed
    }
    
    private void updateUserInfo() {
        if (dataService.getCurrentUser() != null) {
            welcomeLabel.setText("Welcome back, " + dataService.getCurrentUser().getUsername() + "!");
            updateStats();
        }
    }
    
    private void updateStats() {
        if (dataService.getCurrentUser() != null) {
            var user = dataService.getCurrentUser();
            
            // Clear existing stats
            statsContainer.getChildren().clear();
            
            Label sectionTitle = new Label("Your Progress");
            sectionTitle.getStyleClass().add("section-title");
            sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            
            GridPane statsGrid = new GridPane();
            statsGrid.setHgap(20);
            statsGrid.setVgap(10);
            statsGrid.setAlignment(Pos.CENTER);
            
            // Add stat items
            addStatItem(statsGrid, "Exercises Completed", String.valueOf(user.getTotalExercisesCompleted()), 0, 0);
            addStatItem(statsGrid, "Current Streak", String.valueOf(user.getCurrentStreak()) + " days", 1, 0);
            addStatItem(statsGrid, "Longest Streak", String.valueOf(user.getLongestStreak()) + " days", 0, 1);
            addStatItem(statsGrid, "Member Since", user.getCreatedAt().toLocalDate().toString(), 1, 1);
            
            statsContainer.getChildren().addAll(sectionTitle, statsGrid);
        }
    }
    
    private void addStatItem(GridPane grid, String label, String value, int col, int row) {
        VBox statBox = new VBox(5);
        statBox.setAlignment(Pos.CENTER);
        statBox.getStyleClass().add("stat-box");
        statBox.setPadding(new Insets(15));
        
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-value");
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        Label labelLabel = new Label(label);
        labelLabel.getStyleClass().add("stat-label");
        labelLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        
        statBox.getChildren().addAll(valueLabel, labelLabel);
        grid.add(statBox, col, row);
    }
    
    private void startExercise(ExerciseType type) {
        exerciseController.startExercise(type, root.getScene().getWindow());
        // Refresh stats after exercise completion
        updateStats();
    }
    
    private void openSettings() {
        // TODO: Implement settings dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings");
        alert.setHeaderText(null);
        alert.setContentText("Settings dialog will be implemented soon!");
        alert.showAndWait();
    }
    
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Lost Art");
        alert.setHeaderText("Lost Art - Memory Training");
        alert.setContentText("A Java implementation of the Lost Art memory training application.\n\n" +
                            "Improve your memory with scientifically proven exercises.\n" +
                            "Train your brain daily and see your memory capabilities expand.\n\n" +
                            "Version 1.0.0");
        alert.showAndWait();
    }
    
    public Parent getRoot() {
        return root;
    }
}