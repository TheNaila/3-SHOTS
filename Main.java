//John Koomson, Abel Legesse, and Naila Thevenot

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main extends JPanel implements KeyListener, ActionListener {
    public static final int WIDTH = 1250;
    public static final int HEIGHT = 700;
    public static final int FPS = 60;
    public static JFrame frame;
    public static Plane plane;
    private BufferedImage image;
    private BufferedImage startImage;
    private BufferedImage win;
    private BufferedImage lose;

    //The shooter that moves left and right when you press a or d.
    private Shooter MyShooter;

    //K is what helps the plane bounce.
    private int k = 1;

    //Array of pellets
    private WorldPellets Worldpellets;
    private WorldPellets Otherpellets;
    private WorldPellets[] BothWorlds;
    private int currPelIndex = 0;
    private int otherPelIndex = 1;
    private static boolean run = true;

    //Index of ball fired
    private int index;

    //Keeps track of score and health
    private int score = 100;
    private int planescore = 100;
    private LifeBoard Scoreboard;
    private LifeBoard Planeboard;

    //Creates buttons and associated booleans
    private static JButton startbutton;
    static boolean startgame;
    static boolean tocontinue;
    private static JButton pausebutton;
    private static JButton continuebutton;
    static boolean endgame;
    private static JButton instructionsbutton;
    private static JButton restartbutton;
    private static JButton exitbutton;
    static boolean level2;
    private static JButton nextlevelbutton;
    static boolean firstlevel = true;

    //Keeps track of number of successful collisions for a given pellet array so to know when to reload shooter
    private int trackCollision = 0;
    private int otherCollision = 0;

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    class Runner implements Runnable {
        public void run() {
            while (run) {
                //Update the shooter, pellets, and grenades.
                MyShooter.update(1.0 / (double) FPS);
                Worldpellets.update(1.0 / (double) FPS);
                Otherpellets.update(1.0 / (double) FPS);

                //scoreboard
                if (score == 0) {
                    endgame = true;
                    startgame = false;
                }
                //Controlling game booleans based on buttons clicked
                if (endgame) {
                    run = false;
                }
                if (tocontinue) {
                    repaint();
                }
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    //Keyboard Functions
    public synchronized void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        //If I press d,
        if (c == 'd' || c == 'D') {
            //go through the pellet array and move the pellets that have not been released to the right
            //Move shooter to the right.
            MyShooter.velocity.x = 200;

        }
        //If I press a,
        if (c == 'a' || c == 'A') {
            //go through the pellet array and move the pellets that have not been released to the left
            //Move shooter to the left.
            MyShooter.velocity.x = -200;

        }
        //If I press the spacebar,
        if (Character.isWhitespace(c)) {
            if (tocontinue) {

                //Take the pellet at index, set its velocity to 0 and shoot it up.
                if (index < 10 && Worldpellets.World[index] != null) {
                    Worldpellets.World[index].velocity.x = 0;
                    Worldpellets.World[index].velocity.y = -400;

                    //Then let the program know that pellet has been released so it does not move it when I press 'a' or 'd'
                    Worldpellets.World[index].isReleased = true;

                    //Increase the index so I do not shoot the same pellet twice (Practically impossible)
                    index++;
                }

                //Check reload
                //Makes sure to reload when you hit at least 3 bullets with each bullet set
                if (trackCollision >= 3 && index == Worldpellets.World.length) {
                    otherCollision = trackCollision;
                    Otherpellets = Worldpellets;
                    if (currPelIndex == 0) {
                        currPelIndex = 1;
                    } else {
                        currPelIndex = 0;
                    }
                    BothWorlds[currPelIndex] = new WorldPellets(10, MyShooter.position);
                    Worldpellets = BothWorlds[currPelIndex];
                    index = 0;
                    trackCollision = 0;
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == 'd' || c == 'D' || c == 'a' || c == 'A') {
            //If i let go of 'a' or 'd', stop moving shooter and pellets (that have not been released) to the left or right.
            MyShooter.velocity.x = 0;
        }
    }

    public synchronized void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public Main() {
        //Create Instances of shooter, pellets, and grenade.
        MyShooter = new Shooter();
        BothWorlds = new WorldPellets[2];
        BothWorlds[0] = new WorldPellets(10, MyShooter.position);
        BothWorlds[1] = new WorldPellets(10, MyShooter.position);
        Worldpellets = BothWorlds[currPelIndex];
        Otherpellets = BothWorlds[otherPelIndex];
        Scoreboard = new LifeBoard(80, 2, "YOUR LIFE:", 2, 18);
        Planeboard = new LifeBoard(80, 24, "AEROPLANE:", 2, 40);
        addKeyListener(this);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        Thread mainThread = new Thread(new Runner());
        mainThread.start();
        plane = new Plane();

        try {
            image = ImageIO.read(new File("bushBack.png"));
            startImage = ImageIO.read(new File("startscreen.jpg"));
            win = ImageIO.read(new File("success.png"));
            lose = ImageIO.read(new File("nicetry.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //FRAME
        frame = new JFrame("SHOOTER!!!");
        JPanel jPanel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main mainInstance = new Main();
        frame.setContentPane(mainInstance);
        frame.pack();
        if (firstlevel) {
            new RepeatTask(3);
        }

        //Start button
        startbutton = new JButton("Start Game");
        startbutton.setVisible(true);
        startbutton.setFocusable(false);

        //Instructions button
        instructionsbutton = new JButton("How to Play");
        instructionsbutton.setVisible(true);
        instructionsbutton.setFocusable(false);
        jPanel.add(instructionsbutton);

        //Pause button
        pausebutton = new JButton("Pause");
        pausebutton.setFocusable(false);
        pausebutton.setVisible(false);

        //Continue button
        continuebutton = new JButton("Continue");
        continuebutton.setFocusable(false);
        continuebutton.setVisible(false);

        //Replay button
        restartbutton = new JButton("Restart");
        restartbutton.setVisible(false);
        restartbutton.setFocusable(false);

        //Exit button
        exitbutton = new JButton("Exit Game");
        exitbutton.setVisible(true);
        exitbutton.setFocusable(false);

        //Nextlevel button
        nextlevelbutton = new JButton("Next Level");
        nextlevelbutton.setVisible(false);
        nextlevelbutton.setFocusable(false);

        jPanel.add(startbutton);
        jPanel.add(pausebutton);
        jPanel.add(continuebutton);
        jPanel.add(nextlevelbutton);
        jPanel.add(restartbutton);
        jPanel.add(exitbutton);

        frame.add(jPanel);
        frame.setVisible(true);

        //Using start listener
        ActionListener startListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startbutton.setVisible(false);
                pausebutton.setVisible(true);
                continuebutton.setVisible(true);
                instructionsbutton.setVisible(false);
                restartbutton.setVisible(true);
                tocontinue = true;
                startgame = true;
            }
        };
        startbutton.addActionListener(startListener);

        //Using instructions listener
        ActionListener instructionsListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionsbutton.setVisible(false);
                if (!startgame) {
                    startbutton.setVisible(true);
                }
                Graphics g = frame.getGraphics();
                g.setColor(Color.WHITE);
                g.setFont(new Font("Times New Roman", Font.ITALIC, 25));
                g.drawString("How to Play:", 500, 245);
                g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
                g.drawString("1. Hit Space Bar to Shoot the Grenades", 500, 275);
                g.drawString("2. Use A to move left. Use D to move right.", 500, 300);
                g.drawString("3. Your objective is to destroy the plane while making sure the grenades ", 500, 325);
                g.drawString("   don't hit the ground. If a grenade hits the ground, your health reduces by 10.", 500, 350);
                g.drawString("4. You have 10 bullets to begin with. When you fire all of them,   ", 500, 375);
                g.drawString("   it reloads as long as you shot at least 3 grenades down.", 500, 400);
                g.drawString("5. For each set of bullets, you can only shoot down the aeroplane", 500, 425);
                g.drawString("   after shooting down at least 6 grenades. Shooting the aeroplane reduces its", 500, 450);
                g.drawString("   health by 10. This will happen when the plane's health bar is highlighted yellow.", 500, 475);
                g.drawString("6. Press pause button to check instructions in game.   ", 500, 500);
            }
        };
        instructionsbutton.addActionListener(instructionsListener);

        //Using pause listener
        ActionListener pauseListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tocontinue = false;
                pausebutton.setVisible(false);
                instructionsbutton.setVisible(true);
            }
        };
        pausebutton.addActionListener(pauseListener);

        //Using continue listener
        ActionListener continueListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tocontinue = true;
                pausebutton.setVisible(true);
                instructionsbutton.setVisible(false);
            }
        };
        continuebutton.addActionListener(continueListener);

        //Using nextlevel listener
        ActionListener nextleveListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextlevelbutton.setVisible(false);
                firstlevel = false;
                endgame = false;
                run = true;
                frame.setContentPane(new Main());
                frame.pack();
                frame.setVisible(true);
                startgame = true;
                jPanel.add(pausebutton);
                jPanel.add(continuebutton);
                jPanel.add(instructionsbutton);
                jPanel.add(nextlevelbutton);
                pausebutton.setVisible(true);
                continuebutton.setVisible(true);
                frame.add(jPanel);
                level2 = true;
                tocontinue = true;
            }
        };
        nextlevelbutton.addActionListener(nextleveListener);

        //Using restart listener
        ActionListener restartListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endgame = false;
                run = true;
                frame.setContentPane(new Main());
                frame.pack();
                startgame = true;
                jPanel.add(pausebutton);
                jPanel.add(continuebutton);
                jPanel.add(instructionsbutton);
                jPanel.add(nextlevelbutton);
                jPanel.add(restartbutton);
                jPanel.add(exitbutton);
                pausebutton.setVisible(true);
                continuebutton.setVisible(true);
                frame.add(jPanel);
                tocontinue = true;
                frame.setVisible(true);
            }
        };
        restartbutton.addActionListener(restartListener);

        //Using exit listener
        ActionListener exitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        };
        exitbutton.addActionListener(exitListener);
    }

    public void paintComponent(Graphics g) {
        //Displays the start screen image
        g.drawImage(startImage, 0, 0, null);

        if (startgame) {
            //BACKGROUND.
            g.drawImage(image, 0, 0, null);

            //Draw all objects (components of game)
            Worldpellets.draw(g);
            Otherpellets.draw(g);
            MyShooter.draw(g);
            plane.draw(g);
            Scoreboard.draw(g, score);
            Worldpellets.drawPelletsBar(g);
            Planeboard.draw(g, planescore);

            for (int i = 0; i < 10; i++) {
                if (Worldpellets.World[i] != null && !Worldpellets.World[i].isReleased) {
                    Worldpellets.World[i].position = new Pair(MyShooter.position.x + 52, MyShooter.position.y + 85);
                }
                if (Otherpellets.World[i] != null && !Otherpellets.World[i].isReleased) {
                    Otherpellets.World[i].position = new Pair(MyShooter.position.x + 52, MyShooter.position.y + 85);
                }
            }

            //Makes screen wrap around
            if (MyShooter.position.x >= WIDTH) {
                MyShooter.position.x = 0;
                for (int i = 0; i < Worldpellets.World.length; i++) {
                    if (Worldpellets.World[i] != null) {
                        Worldpellets.World[i].position.x = 0;
                    }
                }
            } else if (MyShooter.position.x <= 0) {
                MyShooter.position.x = WIDTH;
                for (int i = 0; i < Worldpellets.World.length; i++) {

                    if (Worldpellets.World[i] != null) {
                        Worldpellets.World[i].position.x = WIDTH;
                    }
                }

            }
            //Plane movement and bouncing at both ends.
            plane.velocity.x = 200 * k;
            plane.update(1.0 / (double) FPS);
            if (plane.sposition.x <= 0) {
                k = 1;
            }
            if (plane.sposition.x + plane.image.getWidth(frame) >= WIDTH) {
                k = -1;
            }

            //Go through all the grenades currently on screen, if it is below screen, set it to null and reduce score by 10.
            for (int i = 0; i < plane.grenades.index; i++) {
                if (plane.grenades.grenade[i] != null) {
                    if (plane.grenades.grenade[i].position.y >= HEIGHT) {
                        score = score - 10;
                        plane.grenades.grenade[i] = null;
                    }
                }
            }

            //Collision detection
            for (int i = 0; i < plane.grenades.index; i++) {
                for (int j = 0; j < Worldpellets.numPellets; j++) {
                    if (plane.grenades.grenade[i] != null && Worldpellets.World[j] != null && plane.grenades.grenade[i].position.y <= HEIGHT - 140) {
                        if (Math.sqrt(Math.pow(plane.grenades.grenade[i].position.x + 10 - Worldpellets.World[j].position.x, 2) +
                                Math.pow(plane.grenades.grenade[i].position.y + 10 - Worldpellets.World[j].position.y, 2)) <= 19.5) {
                            plane.grenades.grenade[i] = null;
                            Worldpellets.World[j] = null;
                            trackCollision++;
                        }
                    }
                }
            }
            for (int i = 0; i < plane.grenades.index; i++) {
                for (int j = 0; j < Otherpellets.numPellets; j++) {
                    if (plane.grenades.grenade[i] != null && Otherpellets.World[j] != null && plane.grenades.grenade[i].position.y <= HEIGHT - 140) {
                        if (Math.sqrt(Math.pow(plane.grenades.grenade[i].position.x + 10 - Otherpellets.World[j].position.x, 2) +
                                Math.pow(plane.grenades.grenade[i].position.y + 10 - Otherpellets.World[j].position.y, 2)) <= 19.5) {
                            plane.grenades.grenade[i] = null;
                            Otherpellets.World[j] = null;
                            trackCollision++;
                        }
                    }
                }
            }

            //AEROPLANE COLLISION DETECTION
            //If grenade.x is within with of aeroplane and grenade.y is within height, reduce score by 10
            //Set plane isTime to true to let the player know the plane can be shot
            if (plane.grenades.index > 19 || trackCollision >= 6) {
                Planeboard.isTime = true;
                for (int i = 0; i < Worldpellets.numPellets; i++) {
                    if (planescore != 0 && Worldpellets.World[i] != null && Worldpellets.World[i].position.y + 9.5 <= plane.sposition.y + plane.image.getHeight(null) &&
                            Worldpellets.World[i].position.y > 0 && Worldpellets.World[i].position.x - 9.5 >= plane.sposition.x && Worldpellets.World[i].position.x +
                            9.5 <= plane.sposition.x + plane.image.getWidth(null)) {
                        planescore = planescore - 10;
                        Worldpellets.World[i] = null;
                    }
                }
            } else {
                Planeboard.isTime = false;
            }
            if (Otherpellets.World[9] == null || Otherpellets.World[9].position.y < 0) {
                otherCollision = 0;
            }
            if (plane.grenades.index > 19 || otherCollision >= 6) {
                Planeboard.isTime = true;
                for (int i = 0; i < Otherpellets.numPellets; i++) {
                    if (planescore != 0 && Otherpellets.World[i] != null && Otherpellets.World[i].position.y + 9.5 <= plane.sposition.y + plane.image.getHeight(null) &&
                            Otherpellets.World[i].position.y > 0 && Otherpellets.World[i].position.x - 9.5 >= plane.sposition.x && Otherpellets.World[i].position.x +
                            9.5 <= plane.sposition.x + plane.image.getWidth(null)) {
                        planescore = planescore - 10;
                        Otherpellets.World[i] = null;
                    }
                }
            }

            //Make plane fall if the player has successfully destroyed it
            if (planescore <= 0) {
                plane.velocity.y = 60;
                plane.velocity.x = 0;
            }
        }

        //Control game conditions
        if (plane.sposition.y >= HEIGHT) {
            endgame = true;
        }
        if ((index >= Worldpellets.World.length && trackCollision < 3 && (Worldpellets.World[Worldpellets.World.length - 1] == null ||
                Worldpellets.World[Worldpellets.World.length - 1].position.y < 0)) && planescore != 0) {
            startgame = false;
            endgame = true;
        }
        if (endgame) {
            startgame = false;
            pausebutton.setVisible(false);
            continuebutton.setVisible(false);
            instructionsbutton.setVisible(false);
            g.clearRect(0, 0, WIDTH, HEIGHT);
            g.drawImage(image, 0, 0, frame);

            if (plane.sposition.y >= HEIGHT && score > 0) {
                g.drawImage(win, 0, 0, null);
                if (!level2) {
                    nextlevelbutton.setVisible(true);
                }
            } else {
                g.drawImage(lose, 0, 0, null);
            }
            restartbutton.setVisible(true);
        }
        if (level2) {
            for (int i = 0; i < plane.grenades.grenade.length; i++) {
                if (plane.grenades.grenade[i] != null) {
                    plane.grenades.grenade[i].velocity.y = 70;
                    if (endgame) {
                        frame.remove(nextlevelbutton);
                        nextlevelbutton.setVisible(false);
                    }
                }
            }
        }
    }
}