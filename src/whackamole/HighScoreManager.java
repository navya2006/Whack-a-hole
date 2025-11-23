package whackamole;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoreManager {
    private static final String FILE_NAME = "scores.dat";


    public void saveScores(List<PlayerScore> scores) throws HighScoreException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            throw new HighScoreException("Failed to save scores.", e);
        }
    }


    @SuppressWarnings("unchecked")
    public List<PlayerScore> loadScores() throws HighScoreException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<PlayerScore>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // [cite: 106] Wrapping the exception
            throw new HighScoreException("Failed to load scores.", e);
        }
    }
}