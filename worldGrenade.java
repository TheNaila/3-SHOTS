//John Koomson, Abel Legesse, and Naila Thevenot

import java.awt.*;

class worldGrenade {
    Grenade[] grenade;

    //Track how many grenades have been released
    int index;

    //Constructor
    public worldGrenade(int num, Pair position) {
        grenade = new Grenade[num];
        for (int i = 0; i < grenade.length; i++) {
            grenade[i] = new Grenade(new Pair(position.x, position.y));
        }
        index = 0;
    }

    //Draw grenades that have been released
    public void draw(Graphics g) {
        for (int i = 0; i < index; i++) {
            if (grenade[i] != null) {
                grenade[i].draw(g);
            }
        }
    }

    //Update grenades that have been released
    public void update(double time) {
        for (int i = 0; i < index; i++) {
            if (grenade[i] != null) {
                grenade[i].update(time);
            }
        }
    }
}