//John Koomson, Abel Legesse, and Naila Thevenot

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Grenade {
    private BufferedImage image;
    private Image[] sprites;
    private int i = 0;
    Pair position;
    Pair velocity;

    //Constructor
    public Grenade(Pair position){
        this.position = position;
        double k = Math.random() * 40;
        double l = Math.random() * 80;
        velocity = new Pair(l - k, 50);
    }

    //Draw grenades
    public void draw(Graphics g) {
        g.setColor(Color.black);
        try {
            image = ImageIO.read(new File("gss.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sprites = new Image[4];
        for (int i = 0; i < 4; i++){
            sprites[i] = image.getSubimage(i * 30, 0, 30, 30);
        }
        g.drawImage(sprites[i], (int)position.x, (int)position.y, null);
    }

    //Update grenades
    public void update(double time) {
        if (Math.round(position.y) % 5 == 0){
            i++;
        }
        if (i >= 3){
            i = 0;
        }
        if (this.position.x >= Main.WIDTH - 20 || this.position.x <= 0){
            velocity.x = -velocity.x;
        }
        position = position.add(velocity.times(time));
    }
}
