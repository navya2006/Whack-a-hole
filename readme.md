# 🔨 Whack-A-Mole (Java Capstone Project)

**An advanced, multi-threaded arcade game built with Java Swing, demonstrating core OOP principles, concurrency, and event-driven programming.**

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Swing](https://img.shields.io/badge/GUI-Swing-blue.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Complete-brightgreen.svg)]()

## 🚀 Overview

**Whack-A-Mole** is a fast-paced reflex game developed as a Capstone Project to demonstrate mastery of Java application development. Unlike simple script-based games, this project is engineered with a robust architecture featuring:

- **Multithreading:** A dedicated `GameEngine` thread manages game logic independently of the User Interface (UI) to prevent freezing.
- **Polymorphism:** Moles, Bombs, and Bonus items share a common abstract contract but behave differently when clicked.
- **Persistence:** High scores are serialized and saved locally, persisting between game sessions.
- **Robust Error Handling:** Custom checked and unchecked exceptions manage file I/O and game state integrity.

---

## 🏗️ Architecture & Class Design (UML)

The following UML Class Diagram illustrates the Object-Oriented structure of the application, highlighting the relationships between the UI, the Game Engine, and the polymorphic Game Objects.

```mermaid
classDiagram
    class WhackAMoleApp {
        +main(args)
        -GameGrid gameGrid
        -GameEngine engine
        -HighScoreManager highScoreManager
        +updateScore(int)
        +updateTimer(int)
        +gameOver()
    }

    class GameEngine {
        <<Runnable>>
        -boolean isRunning
        -int timeRemaining
        +run()
        -spawnRandomOccupant()
    }

    class GameGrid {
        -JButton[] buttons
        -HoleOccupant[] occupants
        +setOccupant(index, occupant)
        +getOccupant(index)
        +clearBoard()
    }

    class HoleOccupant {
        <<Abstract>>
        +whack() int
        +getImage() ImageIcon
        +getType() String
    }

    class Mole {
        +whack() returns 100
        +getImage() mole.png
    }
    class Bomb {
        +whack() returns -500
        +getImage() bomb.png
    }
    class BonusMole {
        +whack() returns 1000
        +getImage() bonus.png
    }

    class HighScoreManager {
        +saveScores(List scores)
        +loadScores() List
    }

    class PlayerScore {
        <<Serializable>>
        -String name
        -int score
    }

    %% Relationships
    WhackAMoleApp *-- GameEngine : Creates & Starts Thread
    WhackAMoleApp *-- GameGrid : Manages UI
    WhackAMoleApp --> HighScoreManager : Uses
    GameEngine --> WhackAMoleApp : Updates UI (via SwingUtilities)
    GameGrid o-- HoleOccupant : Contains
    HoleOccupant <|-- Mole : Inherits
    HoleOccupant <|-- Bomb : Inherits
    HoleOccupant <|-- BonusMole : Inherits
    HighScoreManager ..> PlayerScore : Serializes