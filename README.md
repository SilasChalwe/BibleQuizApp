
# Bible Quiz App

A JavaFX-based Bible quiz application that provides an interactive learning experience with multiple levels, scoring system, and responsive design for both desktop and mobile platforms.

## 🎯 Overview

The Bible Quiz App is a comprehensive quiz application built using JavaFX that tests users' knowledge of biblical content through a structured level-based system. The app features beautiful UI animations, sound effects, and a progressive difficulty system that keeps users engaged while learning.

## 🏗️ Architecture

The application follows a clean, modular architecture with clear separation of concerns:

```
src/main/java/com/nextinnomind/biblequizapp/
├── 🎮 Core Application
├── 🎯 Controllers (MVC Pattern)
├── 📱 Display Adapters
├── 🚀 Loaders & Data Management
├── ⚙️ Managers & Services
├── 📊 Data Models
├── 🎨 Styling Components
├── 🛠️ Utilities
└── 🧩 Custom Widgets
```

## 📂 Project Structure

### Core Application
- **`biblequizapp.java`** - Main application class
- **`Launcher.java`** - Application entry point

### Controllers (MVC Pattern)
- **`onboardingController.java`** - Handles user onboarding flow
- **`LevelsViewController.java`** - Manages level selection interface
- **`QuizViewController.java`** - Controls quiz gameplay logic
- **`ScoreViewController.java`** - Manages score display and progression

### Display Adapters
- **`DesktopDisplay.java`** - Desktop-optimized UI rendering
- **`MobileDisplay.java`** - Mobile-responsive UI components

### Loaders & Data Management
- **`DataLoader.java`** - Loads quiz questions and game data
- **`ImageLoader.java`** - Handles image resource loading
- **`ScoreLoader.java`** - Manages score persistence and retrieval
- **`ViewLoader.java`** - Loads FXML views dynamically

### Managers & Services
- **`AudioManager.java`** - Handles sound effects and audio feedback
- **`ShutdownManager.java`** - Manages graceful application shutdown
- **`SplashScreenManager.java`** - Controls startup splash screen
- **`TimerManager.java`** - Manages quiz timers and countdowns
- **`ViewModeSelectorManager.java`** - Switches between display modes

### Data Models
- **`Question.java`** - Represents quiz questions with answers
- **`LevelScore.java`** - Tracks scoring data for each level

### Styling Components
- **`DialogStyle.java`** - Custom dialog styling
- **`LevelStyle.java`** - Level selection UI styling
- **`QuizViewStyles.java`** - Quiz interface styling

### Utilities
- **`JsonUtils.java`** - JSON parsing and data serialization
- **`ScoreMessageHelper.java`** - Generates contextual score messages
- **`SoundPlayer.java`** - Audio playback utility

### Custom Widgets
- **`LevelCard.java`** - Interactive level selection cards
- **`ScoreAnimator.java`** - Animated score displays
- **`ScoreUIConstants.java`** - UI constants for scoring
- **`Star.java`** - Star rating widget component

## 📁 Resources Structure

### Assets
- **CSS Styling** (`assets/css/`)
    - `colors.css` - Color scheme definitions
    - `style.css` - Main application styling

- **Game Data** (`assets/data/`)
    - `questions.json` - Quiz questions database
    - `scores.json` - User progress and scores

- **Images** (`assets/img/`)
    - UI icons and backgrounds
    - Game state images (game-over, won, splash)
    - Religious imagery and branding

- **Audio** (`assets/sounds/`)
    - `correct.wav` - Correct answer feedback
    - `level-unlocked.wav` - Level progression sound
    - `splash.wav` - Startup audio

### Views (FXML)
- **`onboarding-view.fxml`** - Welcome and tutorial screens
- **`levels-view.fxml`** - Level selection interface
- **`quiz-view.fxml`** - Main quiz gameplay view
- **`score-view.fxml`** - Results and scoring display

## 🎮 How It Works

### 1. Application Flow
```
Splash Screen → Onboarding → Level Selection → Quiz → Score → Repeat
```

### 2. Key Components Interaction

**Startup Process:**
1. `Launcher.java` initializes the application
2. `SplashScreenManager` displays the splash screen with audio
3. `onboardingController` guides new users through the app
4. `ViewModeSelectorManager` adapts UI for device type

**Quiz Gameplay:**
1. `LevelsViewController` presents available levels using `LevelCard` widgets
2. `DataLoader` loads questions from `questions.json`
3. `QuizViewController` manages question flow and user responses
4. `TimerManager` handles time-based challenges
5. `AudioManager` provides immediate feedback for answers

**Scoring System:**
1. `ScoreViewController` calculates and displays results
2. `ScoreAnimator` creates engaging visual feedback
3. `ScoreLoader` persists progress to `scores.json`
4. Star ratings unlock new levels progressively

### 3. Responsive Design
- **Desktop Mode**: Full-featured interface with mouse interactions
- **Mobile Mode**: Touch-optimized controls and layouts
- Dynamic scaling based on screen size and device capabilities

### 4. Data Management
- **Questions**: Stored in JSON format for easy modification
- **Scores**: Persistent local storage tracks user progress
- **Images/Audio**: Lazy-loaded for optimal performance

## 🚀 Features

- ✅ **Progressive Level System** - Unlock levels by achieving high scores
- ✅ **Multi-Platform Support** - Responsive design for desktop and mobile
- ✅ **Rich Audio Feedback** - Sound effects enhance user experience
- ✅ **Persistent Scoring** - Track progress across sessions
- ✅ **Beautiful Animations** - Smooth transitions and visual feedback
- ✅ **Customizable Styling** - CSS-based theming system
- ✅ **JSON-Based Content** - Easy to modify questions and add content

## 🛠️ Technical Stack

- **Framework**: JavaFX for cross-platform GUI
- **Architecture**: MVC (Model-View-Controller) pattern
- **Data Format**: JSON for questions and scores
- **Styling**: CSS for UI theming
- **Audio**: WAV format for sound effects
- **Logging**: Log4j2 for application logging

## 📱 Platform Compatibility

The app automatically detects the runtime environment and adapts:
- **Desktop**: Windows, macOS, Linux with full mouse support
- **Mobile**: Touch-optimized interface with gesture controls
- **Responsive**: Dynamic layout adjustment based on screen size

## 🎯 User Experience

The app provides a seamless learning experience through:
1. **Intuitive Navigation** - Clear visual hierarchy and flow
2. **Immediate Feedback** - Audio and visual responses to user actions
3. **Progress Tracking** - Visual indicators of achievement and growth
4. **Accessibility** - Support for different input methods and screen sizes

This architecture ensures the Bible Quiz App is maintainable, extensible, and provides an engaging user experience across different platforms while maintaining clean separation of concerns and following established design patterns.