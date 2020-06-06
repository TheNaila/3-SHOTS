//John Koomson, Abel Legesse, and Naila Thevenot

import java.awt.*;

public class LifeBoard {
    private int x;
    private int y;
    private int x2;
    private int y2;
    private String s;
    boolean isTime;

    //Constructor
    public LifeBoard(int x, int y, String s, int x2, int y2) {
        this.x = x;
        this.y = y;
        this.s = s;
        this.x2 = x2;
        this.y2 = y2;
        isTime = false;
    }

    //Draw health bar. Highlight with yellow if object can be damaged
    public void draw(Graphics g, int score) {
        g.setFont(new Font("Times New Roman", Font.BOLD, 12));
        g.drawString(this.s, x2, y2);
        Color k = g.getColor();
        g.setColor(Color.black);
        g.drawRect(x, y, 100, 20);
        if (score <= 30) {
            g.setColor(Color.red);
        } else if (score < 70) {
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.green);
        }
        g.fillRect(x, y, score, 20);
        if (isTime) {
            g.setColor(Color.orange);
            g.drawRect(x, y, 100, 20);
        }
        g.setColor(k);
    }
}