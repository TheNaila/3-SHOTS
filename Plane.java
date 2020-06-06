//John Koomson, Abel Legesse, and Naila Thevenot

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class Plane {
    Image image;
    Pair sposition;
    Pair velocity;

    //Create instance of worldGrenades
    worldGrenade grenades;

    //Constructor
    public Plane() {
        sposition = new Pair((double) Main.WIDTH / 2, 0);
        velocity = new Pair(0, 0);
        try {
            image = ImageIO.read(new File("StickerPlane.png"));
            image = image.getScaledInstance((int) (.25 * Main.HEIGHT * 1.96), (int) (.25 * Main.HEIGHT), Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        grenades = new worldGrenade(20, new Pair(sposition.x + ((double) image.getWidth(null) / 2), sposition.y + ((double) image.getHeight(null) / 2)));
    }

    //Draw plane and grenades
    public void draw(Graphics g) {
        grenades.draw(g);
        g.drawImage(image, (int) sposition.x, (int) sposition.y, Main.frame);
    }

    //Update plane and change position of unreleased grenades to be the same as the plane
    public void update(double time) {
        for (int i = grenades.index; i < grenades.grenade.length; i++) {
            grenades.grenade[i].position = new Pair(sposition.x + ((double) image.getWidth(null) / 2), sposition.y + ((double) image.getHeight(null) / 2));
        }
        grenades.update(time);
        sposition = sposition.add(velocity.times(time));
    }


}