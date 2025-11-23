package whackamole;

import javax.swing.ImageIcon;

public class BonusMole extends HoleOccupant {
    @Override
    public int whack() {
        return 1000;
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon("resources/bonus.png");
    }
}   