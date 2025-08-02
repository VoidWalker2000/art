package com.memorytraining.service;

import com.memorytraining.model.ExerciseType;
import com.memorytraining.util.WordGenerator;

import java.util.*;

/**
 * Service for managing memory exercises
 */
public class ExerciseService {
    private static final String[] COLORS = {
        "Red", "Blue", "Green", "Yellow", "Orange", "Purple", "Pink", "Cyan", 
        "Magenta", "Brown", "Gray", "Black", "White", "Lime", "Navy", "Maroon"
    };
    
    private static final String[] BASE_WORDS = {
        "Apple", "House", "Ocean", "Mountain", "Forest", "River", "Garden", "Castle",
        "Bridge", "Sunset", "Rainbow", "Thunder", "Lightning", "Whisper", "Journey",
        "Adventure", "Mystery", "Treasure", "Dragon", "Phoenix", "Crystal", "Diamond",
        "Emerald", "Sapphire", "Golden", "Silver", "Ancient", "Modern", "Future"
    };
    
    private Random random;
    private WordGenerator wordGenerator;
    
    public ExerciseService() {
        this.random = new Random();
        this.wordGenerator = new WordGenerator();
    }
    
    /**
     * Generate a word memory exercise based on difficulty level
     */
    public List<String> generateWordSequence(int level) {
        int sequenceLength = Math.min(3 + level, 15); // Start with 3 words, max 15
        List<String> sequence = new ArrayList<>();
        
        List<String> availableWords = new ArrayList<>(Arrays.asList(BASE_WORDS));
        Collections.shuffle(availableWords, random);
        
        for (int i = 0; i < sequenceLength && i < availableWords.size(); i++) {
            sequence.add(availableWords.get(i));
        }
        
        return sequence;
    }
    
    /**
     * Generate a number memory exercise based on difficulty level
     */
    public List<Integer> generateNumberSequence(int level) {
        int sequenceLength = Math.min(3 + level, 20); // Start with 3 numbers, max 20
        List<Integer> sequence = new ArrayList<>();
        
        // For easier levels, use single digits. For harder levels, use larger numbers
        int maxNumber = level <= 3 ? 9 : (level <= 6 ? 99 : 999);
        
        for (int i = 0; i < sequenceLength; i++) {
            sequence.add(random.nextInt(maxNumber + 1));
        }
        
        return sequence;
    }
    
    /**
     * Generate a color memory exercise based on difficulty level
     */
    public List<String> generateColorSequence(int level) {
        int sequenceLength = Math.min(3 + level, 12); // Start with 3 colors, max 12
        List<String> sequence = new ArrayList<>();
        
        for (int i = 0; i < sequenceLength; i++) {
            sequence.add(COLORS[random.nextInt(COLORS.length)]);
        }
        
        return sequence;
    }
    
    /**
     * Generate spatial positions for spatial memory exercise
     */
    public List<Position> generateSpatialSequence(int level) {
        int sequenceLength = Math.min(3 + level, 15);
        List<Position> sequence = new ArrayList<>();
        int gridSize = Math.min(3 + (level / 2), 6); // Grid grows with level
        
        Set<Position> usedPositions = new HashSet<>();
        
        for (int i = 0; i < sequenceLength; i++) {
            Position pos;
            do {
                pos = new Position(random.nextInt(gridSize), random.nextInt(gridSize));
            } while (usedPositions.contains(pos) && usedPositions.size() < gridSize * gridSize);
            
            sequence.add(pos);
            usedPositions.add(pos);
        }
        
        return sequence;
    }
    
    /**
     * Get display time for sequence based on difficulty and type
     */
    public int getDisplayTimeMs(ExerciseType type, int level, int sequenceLength) {
        int baseTimePerItem = switch (type) {
            case WORD_MEMORY -> 1500; // 1.5 seconds per word
            case NUMBER_MEMORY -> 1000; // 1 second per number
            case COLOR_MEMORY -> 1200; // 1.2 seconds per color
            case SPATIAL_MEMORY -> 800; // 0.8 seconds per position
            case SEQUENCE_MEMORY -> 1000; // 1 second per action
        };
        
        // Reduce time as level increases
        double difficultyMultiplier = Math.max(0.3, 1.0 - (level * 0.1));
        return (int) (baseTimePerItem * sequenceLength * difficultyMultiplier);
    }
    
    /**
     * Calculate score based on performance
     */
    public double calculateScore(int correctAnswers, int totalQuestions, long timeSpentMs, 
                               int level, ExerciseType exerciseType) {
        double accuracy = (double) correctAnswers / totalQuestions;
        double baseScore = accuracy * 100;
        
        // Bonus points for higher levels
        double levelBonus = level * 10;
        
        // Time bonus (faster completion gets more points, but accuracy is more important)
        double expectedTimeMs = getDisplayTimeMs(exerciseType, level, totalQuestions) * 2;
        double timeBonus = Math.max(0, (expectedTimeMs - timeSpentMs) / expectedTimeMs * 20);
        
        return Math.min(200, baseScore + levelBonus + timeBonus);
    }
    
    /**
     * Determine next difficulty level based on performance
     */
    public int calculateNextLevel(int currentLevel, double accuracy, long avgResponseTime) {
        if (accuracy >= 0.9 && avgResponseTime < 3000) { // 90% accuracy, under 3 seconds avg
            return Math.min(currentLevel + 1, 10);
        } else if (accuracy < 0.6) { // Below 60% accuracy
            return Math.max(currentLevel - 1, 1);
        }
        return currentLevel; // Stay at same level
    }
    
    /**
     * Position class for spatial memory exercises
     */
    public static class Position {
        private final int x, y;
        
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() { return x; }
        public int getY() { return y; }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Position position = (Position) obj;
            return x == position.x && y == position.y;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
        
        @Override
        public String toString() {
            return String.format("(%d,%d)", x, y);
        }
    }
}