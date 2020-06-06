import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
//John Koomson, Abel Legesse, and Naila Thevenot

import java.io.IOException;

public class Pellets {
    BufferedImage image;
    Pair position;
    Pair velocity;
    boolean isReleased;

    //Constructor
    public Pellets(Pair position) {
        this.position = position;
        velocity = new Pair(0, 0);
        isReleased = false;
        try {
            image = ImageIO.read(new File("bullet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Draw pellets
    public void drawPellets(Graphics g) {
        g.drawImage(image, (int) (position.x - image.getWidth() / 2), (int) (position.y + 5 - image.getHeight() / 2), null);
    }

    //Update pellets
    public void updatePellets(double time) {
        position = position.add(velocity.times(time));
    }
}