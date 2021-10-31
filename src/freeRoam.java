

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Mazen
 */
public class freeRoam extends JPanel implements ActionListener {

    protected ArrayList<Edible> edibles = new ArrayList<Edible>();
    protected static NyanCat[] cats;
    protected ImageIcon[] catsIcons;
    protected int nCats;
    protected int normInt = 950, spcount = 0;
    public static String[] normals = {"fish.png", "dry.png", "far.png", "chicken.png", "kosa.png", "brocc.png", "naizak.png"};
    public static String[] specials = {"potato.png", "mar.png"};
    protected static Timer timerNorm, timerMove;
    protected static Clip music = null;

    public freeRoam(int x) {
        if (x == 1) {
            cats = new NyanCat[1];
            cats[0] = new NyanCat(2, "nyan.gif");
            catsIcons = new ImageIcon[1];
            nCats = 1;
            super.setSize(960, 605);
        } else if (x == 2) {
            cats = new NyanCat[2];
            cats[0] = new NyanCat(1, "dub.gif");
            cats[1] = new NyanCat(3, "jazz.gif");
            nCats = 2;
            catsIcons = new ImageIcon[2];
            super.setSize(965, 605);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        ImageIcon bg = new ImageIcon("./Drawables/bg.gif");
        g.drawImage(bg.getImage(), 0, 0, 960, 600, this);

        for (int i = 0; i < nCats; i++) {
            catsIcons[i] = new ImageIcon("./Drawables/" + cats[i].drawable);
        }

        for (int i = 0; !edibles.isEmpty() && i < edibles.size(); i++) {
            if (this.edibles.get(i).x <= -120) {
                this.edibles.remove(i);
                System.out.println("delete an element");
                continue;
            }
            ImageIcon edibleIcon = new ImageIcon("./Drawables/" + edibles.get(i).drawable);
            g.drawImage(edibleIcon.getImage(), this.edibles.get(i).x, this.edibles.get(i).y, 120, 120, this);
        }

        for (int i = 0; i < nCats; i++) {
            g.drawImage(catsIcons[i].getImage(), -40, cats[i].y, cats[i].w, cats[i].h, this);
        }
    }

    void listenToMe() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (cats[0].drugged) {
                        cats[0].nyanDown();
                    } else {
                        cats[0].nyanUp();
                    }
                    System.out.println("up");
                    repaint();
                    e.setKeyCode(0);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (cats[0].drugged) {
                        cats[0].nyanUp();
                    } else {
                        cats[0].nyanDown();
                    }
                    System.out.println("down");
                    repaint();
                    e.setKeyCode(0);
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    killRoam();
                    if (nCats == 1) {
                        JOptionPane.showMessageDialog(null, "Game Over! Score: " + cats[0].score);
                        killRoam();
                        writeScore(cats[0].playerName, cats[0].score);
                    } else if (nCats == 2) {
                        JOptionPane.showMessageDialog(null, "Game Over!\nPlayer 1 Score: " + cats[0].score + "\n" + "Player 2 Score: " + cats[1].score);
                        killRoam();
                        writeScore(cats[0].playerName, cats[0].score);
                        writeScore(cats[1].playerName, cats[1].score);
                    }
                    timerMove.stop();
                    timerNorm.stop();
                }
                if (nCats == 2) {
                    if (e.getKeyCode() == KeyEvent.VK_W) {
                        if (cats[1].drugged) {
                            cats[1].nyanDown();
                        } else {
                            cats[1].nyanUp();
                        }
                        System.out.println("up");
                        repaint();
                        e.setKeyCode(0);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_S) {
                        if (cats[1].drugged) {
                            cats[1].nyanUp();
                        } else {
                            cats[1].nyanDown();
                        }
                        System.out.println("down");
                        repaint();
                        e.setKeyCode(0);
                    }

                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addNormals() {
        Random rand = new Random();
        int ind = rand.nextInt(7);
        String normalName = normals[ind];
        if (ind == 0 || ind == 1) {
            edibles.add(new Edible(100, 0, normalName, "win.wav"));
        } else if (normalName.equals("kosa.png")) {
            edibles.add(new Edible(0, -1, normalName, "loseScore.wav"));
        } else if (normalName.equals("far.png")) {
            edibles.add(new Edible(100, 0, normalName, "ratDie.wav"));
        } else if (normalName.equals("brocc.png")) {
            edibles.add(new Edible(-100, 0, normalName, "loseScore.wav"));
        } else if (normalName.equals("naizak.png")) {
            edibles.add(new Edible(0, -3, normalName, "loseGame.wav"));
        }else{
            edibles.add(new Edible(100, 0, normalName, "win.wav"));
        }
    }

    public void addSpecials() {
        Random rand = new Random();
        int ind = rand.nextInt(2);
        String spName = specials[ind];
        if (spName.equals("potato.png")) {
            edibles.add(new Edible(100, 1, spName, "good.wav"));
        } else {
            edibles.add(new Edible(100, 0, spName, "loseScore.wav"));
        }
    }

    public int collisionWithEdible(NyanCat cat) {
        for (int i = 0; i < this.edibles.size(); i++) {
            if (edibles.get(i).x <= 262 && edibles.get(i).x >= 170 && edibles.get(i).y == cat.y) {
                return i;
            }
        }
        return -1;
    }

    void startTimers() {
        timerNorm = new Timer(normInt, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                spcount++;
                if (spcount % 4 == 0) {
                    addSpecials();
                } else {
                    addNormals();
                }
            }

        });
        timerNorm.setRepeats(true);
        timerNorm.start();


        timerMove = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                for (Edible edible : edibles) {
                    if (nCats == 1) {
                        Edible.step = ((cats[0].score + 800) / 400);
                    } else {
                        Edible.step = ((cats[0].score + cats[1].score) + 1600) / 800;
                    }
                    edible.move();

                }
                if (Edible.step < 16) {
                    timerNorm.setDelay(1000 - Edible.step * 50);
                }
                repaint();
                for (int i = 0; i < nCats; i++) {
                    int colliderInd = collisionWithEdible(cats[i]);
                    if (colliderInd != -1) {
                        Edible collider = edibles.get(colliderInd);
                        try {
                            collider.playSound();
                        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                            e.printStackTrace();
                        }
                        if (collider.drawable.equals("mar.png")) {
                            if(cats[i].alive) cats[i].drugNyan();
                        }

                        cats[i].lives += collider.lives;

                        if (i==0) HappyPotatoes.p1Lives.setText(""+cats[i].lives+"          ");
                        else if (i==1) HappyPotatoes.p2Lives.setText(""+cats[i].lives+"          ");

                        if (cats[i].lives > 3) {
                            cats[i].lives = 3;
                            if (i==0) HappyPotatoes.p1Lives.setText(""+cats[i].lives+"          ");
                            else if (i==1) HappyPotatoes.p2Lives.setText(""+cats[i].lives+"          ");
                        }

                        if(cats[i].alive) cats[i].score += collider.score;
                        if(i==0) HappyPotatoes.p1score.setText(""+cats[i].score+", Lives: ");
                        else if (i==1) HappyPotatoes.p2score.setText(""+cats[i].score+", Lives: ");
                        edibles.remove(colliderInd);
                    }

                    if (nCats == 1 && cats[0].lives <= 0) {
                        JOptionPane.showMessageDialog(null, "Game Over! Score: " + cats[0].score);
                        killRoam();
                        writeScore(cats[0].playerName, cats[0].score);
                    } else if (nCats == 2 && cats[0].lives <= 0 && cats[1].lives <= 0) {
                        JOptionPane.showMessageDialog(null, "Game Over!\nPlayer 1 Score: " + cats[0].score + "\n" + "Player 2 Score: " + cats[1].score);
                        killRoam();
                        writeScore(cats[0].playerName, cats[0].score);
                        writeScore(cats[1].playerName, cats[1].score);
                    } else if (cats[0].lives<=0){
                        cats[0].alive=false;
                        cats[0].lives=-10000000;
                        cats[0].y=10000000;
                    } else if(nCats==2){ if(cats[1].lives<=0) {
                        cats[1].alive = false;
                        cats[1].lives = -10000000;
                        cats[1].y = 10000000;
                    }
                    }


                }
            }
        });
        timerMove.setRepeats(true);
        timerMove.start();
    }

    public static void killRoam() {
        HappyPotatoes.splash.remove(HappyPotatoes.roam);
        HappyPotatoes.splash.add(HappyPotatoes.splashpan);
        HappyPotatoes.splash.setSize(700, 700);
        HappyPotatoes.splash.setLocationRelativeTo(null);
        HappyPotatoes.splash.remove(HappyPotatoes.scorespan);
        HappyPotatoes.splashpan.remove(HappyPotatoes.onecat);
        HappyPotatoes.splashpan.remove(HappyPotatoes.twocats);
        HappyPotatoes.splashpan.remove(HappyPotatoes.bgLabel);
        HappyPotatoes.splashpan.add(HappyPotatoes.newGame);
        HappyPotatoes.splashpan.add(HappyPotatoes.hallOfFame);
        HappyPotatoes.splashpan.add(HappyPotatoes.bgLabel);
        if(music != null)
            music.close();
        timerMove.stop();
        timerNorm.stop();
    }



    private void writeScore(String name, int score){
        FileOutputStream writer = null;
        System.out.println("Start Printing ---------------");
        try {
            String content = name + " " + Integer.toString(score) + "\n";
            File datFile = new File("data.txt");
            byte[] contentInBytes = content.getBytes();
            if(datFile.exists())
                Files.write(Paths.get("data.txt"), contentInBytes, StandardOpenOption.APPEND);
            else{
                writer = new FileOutputStream("data.txt");
                writer.write(contentInBytes);
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
