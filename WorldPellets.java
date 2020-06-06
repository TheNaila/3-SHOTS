//John Koomson, Abel Legesse, and Naila Thevenot

import java.awt.*;


public class WorldPellets {

    //Stores how many pellets are in the game
    int numPellets;

    Pellets[] World;

    //Constructor
    public WorldPellets(int numPellets, Pair position) {
        this.numPellets = numPellets;
        World = new Pellets[numPellets];
        for (int i = 0; i < numPellets; i++) {
            World[i] = new Pellets(position);
        }
    }

    //Draw unreleased pellets
    public void draw(Graphics g) {
        for (int i = 0; i < this.numPellets; i++) {
            if (World[i] != null) {
                World[i].drawPellets(g);
            }
        }
    }

    //Used to update a pellet that has been released
    public void update(double time) {
        for (int i = 0; i < numPellets; i++) {
            if (World[i] != null) {
                World[i].updatePellets(time);
            }
        }
    }

    //Draw how many pellets the shooter has available
    public void drawPelletsBar(Graphics g) {
        g.setFont(new Font("Times New Roman", Font.BOLD, 12));
        g.drawString("Bullets:", Main.WIDTH - 150, 18);
        Color k = g.getColor();
        int numbullets = 0;
        for (int i = 0; i < numPellets; i++) {
            if (World[i] != null && !World[i].isReleased) {
                numbullets++;
            }
        }
        g.setColor(Color.black);
        int x = Main.WIDTH - 100;
        for (int i = 0; i < numbullets; i++) {
            g.fillRect(x, 10, 7, 10);
            x = x + 8;
        }
        g.setColor(k);

    }

}