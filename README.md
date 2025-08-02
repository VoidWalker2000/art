# Lost Art - Memory Training (Java Edition)

A Java implementation of the Lost Art memory training application, built with JavaFX and designed to improve cognitive function through scientifically proven memory exercises.

## Features

### 🧠 Multiple Memory Exercises
- **Word Memory**: Remember and recall sequences of words
- **Number Memory**: Remember and recall sequences of numbers  
- **Color Memory**: Remember and recall color patterns
- **Spatial Memory**: Remember and recall spatial arrangements
- **Sequence Memory**: Remember and recall sequences of actions

### 📊 Adaptive Training System
- Difficulty automatically adjusts based on your performance
- Progressive leveling system (1-10)
- Intelligent scoring algorithm that considers accuracy, speed, and level

### 📈 Progress Tracking
- Detailed statistics and performance metrics
- Exercise completion tracking
- Personal best scores and streaks
- Historical performance data

### 🎨 Modern User Interface
- Clean, intuitive JavaFX interface
- Responsive design with attractive gradients and animations
- Professional styling inspired by the original web application
- Dark theme support (future enhancement)

### 💾 Data Persistence
- Automatic saving of user progress and preferences
- JSON-based data storage in user's home directory
- Cross-session persistence of scores and statistics

## System Requirements

- **Java**: JDK 17 or higher
- **Operating System**: Windows, macOS, or Linux
- **Memory**: 512 MB RAM minimum
- **Storage**: 100 MB available space

## Installation & Setup

### Prerequisites
1. Install Java 17 or higher
2. Install Maven 3.6+ (optional, for building from source)

### Quick Start

#### Option 1: Run with Maven (Recommended)
```bash
# Clone or download the project
cd lost-art-java

# Run the application
mvn clean javafx:run
```

#### Option 2: Build and Run JAR
```bash
# Build the project
mvn clean package

# Run the application
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/lost-art-java-1.0.0.jar
```

#### Option 3: IDE Setup
1. Import the Maven project into your IDE
2. Ensure JavaFX is properly configured
3. Run the `com.memorytraining.App` main class

## How to Use

### Getting Started
1. Launch the application
2. The welcome screen shows your current progress and available exercises
3. Select an exercise type from the main screen
4. Follow the on-screen instructions for each exercise

### Exercise Types Explained

#### Word Memory
- Memorize sequences of words (3-15 words depending on level)
- Type the words back in the correct order
- Focus on creating mental associations between words

#### Number Memory  
- Remember sequences of numbers (3-20 numbers depending on level)
- Numbers range from single digits to 3-digit numbers based on difficulty
- Type the numbers back in exact sequence

#### Color Memory
- Memorize sequences of colors (3-12 colors depending on level)  
- Click the color buttons in the correct order
- Great for visual memory training

#### Spatial Memory
- Remember positions that light up on a grid (3×3 to 6×6 grid)
- Click the positions in the correct sequence
- Excellent for spatial reasoning skills

#### Sequence Memory
- Remember sequences of actions or events
- Repeat the sequence in the correct order
- Builds sequential processing abilities

### Difficulty & Adaptive Training

The application uses an intelligent adaptive system:
- **Level 1-3**: Beginner (shorter sequences, more time)
- **Level 4-6**: Intermediate (medium sequences, moderate time)  
- **Level 7-10**: Advanced (longer sequences, less time)

Performance criteria for level advancement:
- **Level Up**: 90%+ accuracy with fast response times
- **Level Down**: Below 60% accuracy
- **Stay Same**: 60-89% accuracy

### Scoring System

Scores are calculated based on:
- **Accuracy** (most important): Percentage of correct answers
- **Level Bonus**: Higher levels provide more points
- **Time Bonus**: Faster completion times earn bonus points
- **Maximum Score**: 200 points per exercise

## Data Storage

User data is automatically saved to:
- **Windows**: `%USERPROFILE%\.lost-art-memory\`
- **macOS**: `~/.lost-art-memory/`
- **Linux**: `~/.lost-art-memory/`

Data includes:
- User preferences and settings
- Exercise completion history  
- Personal best scores
- Statistical information

## Development

### Project Structure
```
src/main/java/com/memorytraining/
├── App.java                    # Main application entry point
├── controller/                 # Application controllers
│   └── ExerciseController.java
├── model/                      # Data models
│   ├── ExerciseType.java
│   ├── Score.java
│   ├── User.java
│   └── UserPreferences.java
├── service/                    # Business logic services
│   ├── DataService.java
│   └── ExerciseService.java
├── util/                       # Utility classes
│   └── WordGenerator.java
└── view/                       # UI components
    ├── ExerciseWindow.java
    └── MainWindow.java

src/main/resources/
├── css/
│   └── styles.css             # Application styling
├── fxml/                      # FXML layouts (future)
└── images/                    # Application images
```

### Building from Source
```bash
# Clone the repository
git clone <repository-url>
cd lost-art-java

# Build the project
mvn clean compile

# Run tests
mvn test

# Package the application
mvn package

# Run the application
mvn javafx:run
```

### Architecture

The application follows the MVC (Model-View-Controller) pattern:

- **Models**: Data structures for User, Score, Exercise types
- **Views**: JavaFX-based UI components  
- **Controllers**: Business logic and user interaction handling
- **Services**: Data persistence and exercise generation

### Key Technologies
- **JavaFX 17**: Modern UI framework
- **Maven**: Build automation and dependency management
- **JSON**: Data serialization format
- **CSS**: Styling and theming

## Future Enhancements

### Planned Features
- [ ] Settings dialog for user preferences
- [ ] Additional exercise types (Pattern Recognition, Reaction Time)
- [ ] Advanced statistics and charts
- [ ] Multiple user profiles
- [ ] Export/import functionality
- [ ] Sound effects and audio cues
- [ ] Timed daily challenges
- [ ] Achievement system
- [ ] Dark/Light theme toggle

### Technical Improvements
- [ ] FXML-based UI layouts
- [ ] Unit test coverage
- [ ] Internationalization support  
- [ ] Database persistence option
- [ ] Cloud synchronization
- [ ] Performance optimizations

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java naming conventions
- Add JavaDoc comments for public methods
- Include unit tests for new features
- Maintain the existing code style
- Update documentation as needed

## Troubleshooting

### Common Issues

#### JavaFX Runtime Issues
```
Error: JavaFX runtime components are missing
```
**Solution**: Ensure JavaFX is properly installed and included in module path.

#### Memory Issues
```
OutOfMemoryError during exercise generation
```
**Solution**: Increase JVM heap size with `-Xmx1g` parameter.

#### Data Loading Errors
```
Error loading user data: JSON parsing failed
```
**Solution**: Delete corrupted data file in `~/.lost-art-memory/` directory.

### Performance Tips
- Close other applications to free up memory
- Run on Java 17+ for optimal performance
- Ensure adequate disk space for data storage

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Inspired by the original Lost Art memory training web application
- Built with JavaFX for cross-platform compatibility
- Uses scientifically-backed memory training methodologies
- Thanks to the cognitive science research community for memory training insights

## Version History

### v1.0.0 (Current)
- Initial release with core functionality
- Five exercise types implemented
- Adaptive difficulty system
- Progress tracking and statistics
- Modern JavaFX user interface
- Data persistence and user profiles

---

**Happy training! 🧠✨**

Improve your memory one exercise at a time with Lost Art.