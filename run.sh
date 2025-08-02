#!/bin/bash

# Lost Art Memory Training - Run Script
# This script launches the Java memory training application

echo "Starting Lost Art Memory Training..."

# Check if Maven is available
if command -v mvn &> /dev/null; then
    echo "Running with Maven..."
    mvn clean javafx:run
else
    echo "Maven not found. Attempting to run with java directly..."
    
    # Try to find JavaFX in common locations
    JAVAFX_PATH=""
    
    # Common JavaFX installation paths
    if [ -d "/usr/share/openjfx/lib" ]; then
        JAVAFX_PATH="/usr/share/openjfx/lib"
    elif [ -d "/opt/javafx/lib" ]; then
        JAVAFX_PATH="/opt/javafx/lib"
    elif [ -d "$HOME/.m2/repository/org/openjfx" ]; then
        # Use Maven repository
        JAVAFX_PATH="$HOME/.m2/repository/org/openjfx/javafx-controls/17.0.2/javafx-controls-17.0.2-linux.jar:$HOME/.m2/repository/org/openjfx/javafx-fxml/17.0.2/javafx-fxml-17.0.2-linux.jar:$HOME/.m2/repository/org/openjfx/javafx-graphics/17.0.2/javafx-graphics-17.0.2-linux.jar:$HOME/.m2/repository/org/openjfx/javafx-base/17.0.2/javafx-base-17.0.2-linux.jar"
    fi
    
    if [ -n "$JAVAFX_PATH" ]; then
        echo "Found JavaFX at: $JAVAFX_PATH"
        
        # Build classpath
        CP="target/classes"
        if [ -f "$HOME/.m2/repository/org/json/json/20230227/json-20230227.jar" ]; then
            CP="$CP:$HOME/.m2/repository/org/json/json/20230227/json-20230227.jar"
        fi
        
        java --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml \
             -cp "$CP" com.memorytraining.App
    else
        echo "JavaFX not found. Please install JavaFX or use Maven to run the application."
        echo "Try: sudo apt install openjfx"
        exit 1
    fi
fi