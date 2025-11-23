package whackamole;

import javax.swing.ImageIcon;

public class Bomb extends HoleOccupant {
    @Override
    public int whack() {
        return -500;
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon("resources/bomb.png");
    }
}