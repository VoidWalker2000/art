package com.memorytraining.util;

import java.util.*;

/**
 * Utility class for generating words for memory exercises
 */
public class WordGenerator {
    private static final String[] ADJECTIVES = {
        "Beautiful", "Mysterious", "Ancient", "Golden", "Silver", "Bright", "Dark", 
        "Peaceful", "Stormy", "Gentle", "Fierce", "Elegant", "Rustic", "Modern",
        "Colorful", "Transparent", "Solid", "Liquid", "Frozen", "Burning"
    };
    
    private static final String[] NOUNS = {
        "Castle", "Forest", "Ocean", "Mountain", "Valley", "River", "Lake", "Desert",
        "City", "Village", "Garden", "Tower", "Bridge", "Palace", "Temple", "Cave",
        "Island", "Meadow", "Cliff", "Waterfall", "Prairie", "Volcano", "Glacier"
    };
    
    private static final String[] ANIMALS = {
        "Eagle", "Wolf", "Bear", "Lion", "Tiger", "Elephant", "Dolphin", "Whale",
        "Fox", "Rabbit", "Deer", "Horse", "Butterfly", "Dragon", "Phoenix", "Unicorn"
    };
    
    private Random random = new Random();
    
    public List<String> generateWordSet(int count, WordCategory category) {
        List<String> words = new ArrayList<>();
        String[] sourceArray = getSourceArray(category);
        
        List<String> availableWords = new ArrayList<>(Arrays.asList(sourceArray));
        Collections.shuffle(availableWords, random);
        
        for (int i = 0; i < count && i < availableWords.size(); i++) {
            words.add(availableWords.get(i));
        }
        
        return words;
    }
    
    public String generateCompoundWord() {
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];
        return adjective + " " + noun;
    }
    
    private String[] getSourceArray(WordCategory category) {
        return switch (category) {
            case ADJECTIVES -> ADJECTIVES;
            case NOUNS -> NOUNS;
            case ANIMALS -> ANIMALS;
            case MIXED -> combineArrays(ADJECTIVES, NOUNS, ANIMALS);
        };
    }
    
    private String[] combineArrays(String[]... arrays) {
        List<String> combined = new ArrayList<>();
        for (String[] array : arrays) {
            combined.addAll(Arrays.asList(array));
        }
        return combined.toArray(new String[0]);
    }
    
    public enum WordCategory {
        ADJECTIVES, NOUNS, ANIMALS, MIXED
    }
}