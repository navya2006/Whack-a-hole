package whackamole;

import javax.swing.ImageIcon;

public class Mole extends HoleOccupant {
    @Override
    public int whack() {
        return 100;
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon("resources/mole.png");
    }
}