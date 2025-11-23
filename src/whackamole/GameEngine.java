package whackamole;

import javax.swing.SwingUtilities;
import java.util.Random;

// [cite: 45] Implements Runnable
public class GameEngine implements Runnable {
    private WhackAMoleApp app;
    private volatile boolean isRunning = true;
    private int timeRemaining = 30;
    private Random random = new Random();

    public GameEngine(WhackAMoleApp app) {
        this.app = app;
    }

    @Override
    public void run() {
        try {
            // [cite: 50] Game Loop
            while (isRunning && timeRemaining > 0) {
                // 1. Tick: Wait 1 second
                Thread.sleep(1000); // [cite: 54] Sleep call
                timeRemaining--;

                // 2. Logic: Pick a hole and spawn something
                int randomHole = random.nextInt(15);
                HoleOccupant newOccupant = spawnRandomOccupant();

                // 3. UI Update: MUST use invokeLater [cite: 76]
                SwingUtilities.invokeLater(() -> {
                    app.updateTimer(timeRemaining);
                    app.showOccupant(randomHole, newOccupant);
                });

                // Let the mole stay up for 0.8 seconds, then hide it
                Thread.sleep(800); 
                SwingUtilities.invokeLater(() -> {
                    app.hideOccupant(randomHole);
                });
            }

            // Loop finished: Trigger Game Over
            SwingUtilities.invokeLater(() -> app.gameOver());

        } catch (InterruptedException e) {
            // [cite: 102] Graceful shutdown on interruption
            System.out.println("Game stopped.");
            isRunning = false;
        }
    }

    private HoleOccupant spawnRandomOccupant() {
        double chance = random.nextDouble();
        if (chance < 0.2) return new Bomb();      // 20% chance
        if (chance < 0.3) return new BonusMole(); // 10% chance
        return new Mole();                        // 70% chance
    }
}