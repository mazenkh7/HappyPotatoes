import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;


/**
 * * @author Mazen
 */
public class HappyPotatoes implements ActionListener {

    protected static JFrame splash = new JFrame("Happy Potatoes");
    protected static JPanel splashpan = new JPanel();
    protected static JPanel scorespan = new JPanel();
    protected static freeRoam roam;
    protected static String[] players = new String[2];
    protected static JLabel p2 = new JLabel();
    protected static JLabel p1 = new JLabel();
    protected static JLabel p1score = new JLabel("0, Lives: ");
    protected static JLabel p2score = new JLabel("0, Lives: ");
    protected static JLabel p1Lives = new JLabel("3          ");
    protected static JLabel p2Lives = new JLabel("3          ");
    protected static JLabel onecat, twocats, newGame, hallOfFame, bgLabel;
    protected static JButton back = new JButton("back");

    private static void initRoam(int x) throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
        roam = new freeRoam(x);

        // play Music
        String bip = "./Sounds/Nyan.wav";
        File soundFile = new File(bip);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);

        audioClip.open(audioStream);
        audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        roam.music = audioClip;
        audioClip.start();
        // End play Music

        splash.remove(splashpan);
        splash.setSize(960, 660);
        splash.add(roam);
        splash.setLocationRelativeTo(null);
        roam.setFocusable(true);
        roam.requestFocus();
        roam.listenToMe();
        roam.startTimers();
        freeRoam.cats[0].playerName=players[0];
        if(freeRoam.cats.length == 2)
            freeRoam.cats[1].playerName = players[1];
        p1.setText(players[0]+"'s Score: ");
        scorespan.add(p1);
        scorespan.add(p1score);
        scorespan.add(p1Lives);
        p1.setForeground(Color.white);
        p1score.setForeground(Color.white);
        p1Lives.setForeground(Color.white);

        if(x==2) {
            freeRoam.cats[1].name = players[1];
            p2.setText(players[1]+"'s Score: ");
            scorespan.add(p2);
            scorespan.add(p2score);
            scorespan.add(p2Lives);
            p2.setForeground(Color.white);
            p2score.setForeground(Color.white);
            p2Lives.setForeground(Color.white);
        }
        scorespan.setBounds(0,605,960,100);
        scorespan.setBackground(Color.black);
        scorespan.setForeground(Color.white);
        splash.add(scorespan,BorderLayout.PAGE_END);
    }

    public static void main(String[] args) {
        splash.setSize(700, 700);
        ImageIcon frameIcon = new ImageIcon("./Drawables/icon.png");
        splash.setIconImage(frameIcon.getImage());
        splash.setResizable(false);
        splash.setLocationRelativeTo(null);
        splash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splashpan.setBounds(0, 0, 700, 700);
        ImageIcon splashBg = new ImageIcon("./Drawables/sbg.png");
        bgLabel = new JLabel();
        newGame = new JLabel();
        hallOfFame = new JLabel();
        onecat = new JLabel();
        twocats = new JLabel();

        ImageIcon ngIcon = new ImageIcon("./Drawables/ng.png");
        ImageIcon ngaltIcon = new ImageIcon("./Drawables/ngalt.png");
        ImageIcon hofIcon = new ImageIcon("./Drawables/hof.png");
        ImageIcon hofaltIcon = new ImageIcon("./Drawables/hofalt.png");
        ImageIcon oc = new ImageIcon("./Drawables/oc.png");
        ImageIcon ocalt = new ImageIcon("./Drawables/ocalt.png");
        ImageIcon tc = new ImageIcon("./Drawables/tc.png");
        ImageIcon tcalt = new ImageIcon("./Drawables/tcalt.png");

        newGame.setIcon(ngIcon);
        hallOfFame.setIcon(hofIcon);

        onecat.setIcon(oc);
        twocats.setIcon(tc);

        newGame.setBounds(120, 200, 443, 167);
        hallOfFame.setBounds(120, 400, 443, 167);

        onecat.setBounds(120, 200, 443, 167);
        twocats.setBounds(120, 400, 443, 167);

        splashpan.add(newGame);
        splashpan.add(hallOfFame);


        newGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                splashpan.remove(newGame);
                splashpan.remove(hallOfFame);
                splashpan.remove(bgLabel);

                splashpan.add(onecat);
                splashpan.add(twocats);
                splashpan.add(bgLabel);

                splashpan.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                newGame.setIcon(ngaltIcon);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                newGame.setIcon(ngIcon);
            }
        });
        hallOfFame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                splashpan.remove(HappyPotatoes.bgLabel);
                splashpan.remove(HappyPotatoes.newGame);
                splashpan.remove(HappyPotatoes.hallOfFame);


                // todo implement hall of fame panel
                ArrayList<ArrayList<String>> data = null;
                try {
                    Map<Integer, ArrayList<String>> scores = getScores();
                    if(scores != null) {
                        data = new ArrayList<ArrayList<String>>();
                        for (Map.Entry<Integer, ArrayList<String>> entry : scores.entrySet()) {
                            for (String name : entry.getValue()) {
                                ArrayList<String> pair = new ArrayList<String>();
                                pair.add(name);
                                pair.add(entry.getKey().toString());
                                data.add(pair);
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] columns = {"Name ", "Score "};
                if(data != null) {
                    String[][] toAppear = new String[data.size()][];
                    int x = 0;
                    for (int i = data.size() - 1; i >= 0; i--) {
                        ArrayList<String> row = data.get(i);
                        toAppear[x] = row.toArray(new String[row.size()]);
                        x++;
                    }
                    // todo remove after testing
                    for (int i = 0; i < toAppear.length; i++) {
                        for (int j = 0; j < toAppear[i].length; j++) {
                            System.out.println(toAppear[i][j]);
                        }
                    }
                    // todo fix JTable does not appear
                    final JTable jt = new JTable(toAppear, columns);
                    jt.setBounds(0, 0, 700, 600);
                    jt.setEnabled(false);
                    JScrollPane sp=new JScrollPane(jt);
                    back.setBounds(310,610,100,20);
                    back.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            splashpan.setFocusable(true);
                            splashpan.remove(sp);
                            splashpan.add(newGame);
                            splashpan.add(hallOfFame);
                            splashpan.add(bgLabel);
                            splashpan.remove(back);
                            splash.repaint();
                        }
                    });

                    sp.setBounds(0, 0, 700, 600);
                    splashpan.add(sp);
                    splashpan.add(back);
                }

                splashpan.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                hallOfFame.setIcon(hofaltIcon);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                hallOfFame.setIcon(hofIcon);
            }
        });
        onecat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                String playerName;
                playerName = JOptionPane.showInputDialog("Please input player's name: ");
                if(playerName != null && (!playerName.equals("")))
                    players[0] = playerName;
                else
                    players[0] = "Unknown";
                try {
                    initRoam(1);
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                onecat.setIcon(ocalt);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                onecat.setIcon(oc);
            }
        });
        twocats.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                String playerName1, playerName2;
                playerName1 = JOptionPane.showInputDialog("Please input player 1's name: ");
                playerName2 = JOptionPane.showInputDialog("Please input player 2's name: ");
                if(playerName1 != null && !(playerName1.equals("")))
                    players[0] = playerName1;
                else
                    players[0] = "Unknown";
                if(playerName2 != null && !(playerName2.equals("")))
                    players[1] = playerName2;
                else
                    players[1] = "Unknown";
                try {
                    initRoam(2);
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                twocats.setIcon(tcalt);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                twocats.setIcon(tc);
            }
        });
        splashpan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode()==KeyEvent.VK_ESCAPE){
                    splashpan.remove(onecat);
                    splashpan.remove(twocats);
                    splashpan.remove(bgLabel);
                    splashpan.add(newGame);
                    splashpan.add(hallOfFame);
                    splashpan.add(bgLabel);
                    splashpan.repaint();
                }
            }
        });
        splashpan.setFocusable(true);
        bgLabel.setIcon(splashBg);
        bgLabel.setBounds(0, 0, 700, 700);

        splash.add(splashpan);
        splashpan.add(bgLabel);
        splashpan.setLayout(null);
        splash.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
    }

    private static Map<Integer, ArrayList<String>> getScores() throws IOException {
        Map<Integer, ArrayList<String>> scores = new TreeMap<Integer, ArrayList<String>>();
        File file = new File("data.txt");
        if(file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String str = new String(data, "UTF-8");
            String lines[] = str.split("\\r?\\n");
            for (String line : lines) {
                String playerData[] = line.split(" ");
                scores.putIfAbsent(Integer.parseInt(playerData[1]), new ArrayList<String>());
                scores.get(Integer.parseInt(playerData[1])).add(playerData[0]);
            }
            System.out.println(scores.toString());
            return scores;
        }
        return null;
    }

}
