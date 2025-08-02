package com.memorytraining;

import com.memorytraining.service.DataService;
import com.memorytraining.view.MainWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main JavaFX Application class for Lost Art Memory Training
 * This is the entry point for the application
 */
public class App extends Application {
    
    private static final String APP_TITLE = "Lost Art - Memory Training";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    
    private DataService dataService;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize data service
            dataService = new DataService();
            dataService.initializeData();
            
            // Create main window
            MainWindow mainWindow = new MainWindow(dataService);
            Scene scene = new Scene(mainWindow.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
            
            // Load CSS styles
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.centerOnScreen();
            primaryStage.show();
            
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void stop() throws Exception {
        // Save user data before closing
        if (dataService != null) {
            dataService.saveData();
        }
        super.stop();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}