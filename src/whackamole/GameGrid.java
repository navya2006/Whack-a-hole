package whackamole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameGrid extends JPanel {
    private JButton[] buttons;
    private HoleOccupant[] occupants; //  Collection of abstract types
    private static final int ROWS = 3;
    private static final int COLS = 5;

    public GameGrid(ActionListener actionListener) {
        // [cite: 64] Using GridLayout for Swing
        setLayout(new GridLayout(ROWS, COLS, 10, 10));
        buttons = new JButton[ROWS * COLS];
        occupants = new HoleOccupant[ROWS * COLS];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setActionCommand(String.valueOf(i)); // ID is the index
            buttons[i].addActionListener(actionListener);   // [cite: 66] Attach listener
            buttons[i].setFocusable(false);
            buttons[i].setBackground(Color.DARK_GRAY);
            buttons[i].setIcon(new ImageIcon("resources/empty.png")); 
            add(buttons[i]);
        }
    }

    public void setOccupant(int index, HoleOccupant occupant) {
        if (index < 0 || index >= buttons.length) {
            // [cite: 110] Unrecoverable error
            throw new InvalidGameStateException("Attempted to access invalid hole index: " + index);
        }
        
        occupants[index] = occupant;
        
        if (occupant != null) {
            buttons[index].setIcon(occupant.getImage());
        } else {
            buttons[index].setIcon(new ImageIcon("resources/empty.png"));
        }
    }

    public HoleOccupant getOccupant(int index) {
        return occupants[index];
    }
    
    public void clearBoard() {
        for(int i = 0; i < buttons.length; i++) {
            setOccupant(i, null);
        }
    }
}