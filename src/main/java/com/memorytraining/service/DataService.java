package com.memorytraining.service;

import com.memorytraining.model.User;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service for managing user data persistence
 */
public class DataService {
    private static final String DATA_DIR = System.getProperty("user.home") + "/.lost-art-memory";
    private static final String USER_DATA_FILE = "user_data.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    private User currentUser;
    private Path dataDirectory;
    private Path userDataPath;
    
    public DataService() {
        this.dataDirectory = Paths.get(DATA_DIR);
        this.userDataPath = dataDirectory.resolve(USER_DATA_FILE);
        createDataDirectoryIfNotExists();
    }
    
    public void initializeData() {
        loadUserData();
        if (currentUser == null) {
            currentUser = new User("Default User");
            saveData();
        }
    }
    
    private void createDataDirectoryIfNotExists() {
        try {
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }
    
    public void saveData() {
        try {
            JSONObject userJson = userToJson(currentUser);
            try (FileWriter writer = new FileWriter(userDataPath.toFile())) {
                writer.write(userJson.toString(2));
            }
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }
    
    private void loadUserData() {
        try {
            if (Files.exists(userDataPath)) {
                String content = Files.readString(userDataPath);
                JSONObject userJson = new JSONObject(content);
                currentUser = jsonToUser(userJson);
            }
        } catch (IOException e) {
            System.err.println("Error loading user data: " + e.getMessage());
        }
    }
    
    private JSONObject userToJson(User user) {
        JSONObject json = new JSONObject();
        json.put("username", user.getUsername());
        json.put("createdAt", user.getCreatedAt().format(DATE_FORMATTER));
        json.put("lastLoginAt", user.getLastLoginAt().format(DATE_FORMATTER));
        json.put("totalExercisesCompleted", user.getTotalExercisesCompleted());
        json.put("currentStreak", user.getCurrentStreak());
        json.put("longestStreak", user.getLongestStreak());
        
        // Add preferences
        JSONObject prefsJson = new JSONObject();
        prefsJson.put("soundEnabled", user.getPreferences().isSoundEnabled());
        prefsJson.put("animationsEnabled", user.getPreferences().isAnimationsEnabled());
        prefsJson.put("defaultDifficulty", user.getPreferences().getDefaultDifficulty());
        prefsJson.put("adaptiveDifficultyEnabled", user.getPreferences().isAdaptiveDifficultyEnabled());
        prefsJson.put("theme", user.getPreferences().getTheme());
        json.put("preferences", prefsJson);
        
        return json;
    }
    
    private User jsonToUser(JSONObject json) {
        User user = new User();
        user.setUsername(json.getString("username"));
        
        if (json.has("createdAt")) {
            user.setCreatedAt(LocalDateTime.parse(json.getString("createdAt"), DATE_FORMATTER));
        }
        if (json.has("lastLoginAt")) {
            user.setLastLoginAt(LocalDateTime.parse(json.getString("lastLoginAt"), DATE_FORMATTER));
        }
        
        user.setTotalExercisesCompleted(json.optInt("totalExercisesCompleted", 0));
        user.setCurrentStreak(json.optInt("currentStreak", 0));
        user.setLongestStreak(json.optInt("longestStreak", 0));
        
        // Load preferences
        if (json.has("preferences")) {
            JSONObject prefsJson = json.getJSONObject("preferences");
            user.getPreferences().setSoundEnabled(prefsJson.optBoolean("soundEnabled", true));
            user.getPreferences().setAnimationsEnabled(prefsJson.optBoolean("animationsEnabled", true));
            user.getPreferences().setDefaultDifficulty(prefsJson.optInt("defaultDifficulty", 1));
            user.getPreferences().setAdaptiveDifficultyEnabled(prefsJson.optBoolean("adaptiveDifficultyEnabled", true));
            user.getPreferences().setTheme(prefsJson.optString("theme", "light"));
        }
        
        return user;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}