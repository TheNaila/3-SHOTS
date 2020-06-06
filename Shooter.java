//John Koomson, Abel Legesse, and Naila Thevenot

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Shooter {
    //The shooter has starting point x and y;
    //The shooter cannot move up hence its upward velocity is 0 and acceleration is 0.
    //However, its horizontal velocity and acceleration changes based on keyboard input.
    public Pair position;
    Pair velocity;
    private BufferedImage image;

    //Constructor
    public Shooter() {
        try {
            image = ImageIO.read(new File("tank.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        position = new Pair(Math.random() * (Main.WIDTH - image.getWidth()), Main.HEIGHT - image.getHeight() - 5);
        velocity = new Pair(0, 0);
    }

    //Draw the shooter
    public void draw(Graphics g) {
        g.drawImage(image, (int) position.x, (int) position.y, null);
    }

    //Update the shooter
    public void update(double time) {
        position = position.add(velocity.times(time));
    }
}