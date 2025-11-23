package whackamole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class WhackAMoleApp extends JFrame {
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JLabel highScoreLabel;
    private GameGrid gameGrid;
    private int score = 0;
    private Thread gameThread;
    private HighScoreManager highScoreManager;
    private List<PlayerScore> highScores;

    public WhackAMoleApp() {
        setTitle("Whack-A-Mole");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize Data Manager
        highScoreManager = new HighScoreManager();
        try {
            highScores = highScoreManager.loadScores();
        } catch (HighScoreException e) {
            // [cite: 107] Handle loading error gracefully
            System.err.println("Error loading scores: " + e.getMessage());
            highScores = new ArrayList<>();
        }

        // --- Top Panel: Stats ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        topPanel.setBackground(new Color(50, 50, 50)); // Dark Gray
        
        scoreLabel = createStatLabel("Score: 0");
        highScoreLabel = createStatLabel("High Score: " + getHighestScore());
        timeLabel = createStatLabel("Time: 30s");

        topPanel.add(scoreLabel);
        topPanel.add(highScoreLabel);
        topPanel.add(timeLabel);
        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel: The Grid ---
        gameGrid = new GameGrid(e -> handleWhack(e));
        add(gameGrid, BorderLayout.CENTER);

        // --- Bottom Panel: Controls ---
        JPanel bottomPanel = new JPanel();
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.addActionListener(e -> startGame());
        
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.addActionListener(e -> System.exit(0));

        bottomPanel.add(startButton);
        bottomPanel.add(exitButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // [cite: 101] Window Listener to kill thread on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (gameThread != null && gameThread.isAlive()) {
                    gameThread.interrupt();
                }
            }
        });

        setVisible(true);
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        return label;
    }

    private void startGame() {
        if (gameThread != null && gameThread.isAlive()) return; 

        score = 0;
        updateScore(0);
        gameGrid.clearBoard();
        
        // [cite: 47] Create and Start Thread
        GameEngine engine = new GameEngine(this);
        gameThread = new Thread(engine);
        gameThread.start();
    }

    // [cite: 39] Polymorphic Whack Handling
    private void handleWhack(ActionEvent e) {
        int holeIndex = Integer.parseInt(e.getActionCommand());
        HoleOccupant occupant = gameGrid.getOccupant(holeIndex);

        if (occupant != null) {
            // Invoke polymorphic method
            int points = occupant.whack();
            updateScore(points);
            
            // Hide immediately
            gameGrid.setOccupant(holeIndex, null);
        }
    }

    // --- Methods for GameEngine to call (Thread Safe) ---

    public void updateTimer(int seconds) {
        timeLabel.setText("Time: " + seconds + "s");
    }

    public void updateScore(int points) {
        score += points;
        scoreLabel.setText("Score: " + score);
    }

    public void showOccupant(int index, HoleOccupant occupant) {
        gameGrid.setOccupant(index, occupant);
    }

    public void hideOccupant(int index) {
        gameGrid.setOccupant(index, null);
    }

    public void gameOver() {
        String name = JOptionPane.showInputDialog(this, "Game Over! Score: " + score + "\nEnter name:");
        if (name != null && !name.trim().isEmpty()) {
            highScores.add(new PlayerScore(name, score));
            try {
                highScoreManager.saveScores(highScores);
            } catch (HighScoreException ex) {
                ex.printStackTrace();
            }
        }
        highScoreLabel.setText("High Score: " + getHighestScore());
    }

    private int getHighestScore() {
        return highScores.stream().mapToInt(PlayerScore::getScore).max().orElse(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WhackAMoleApp::new);
    }
}