import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Sad Potatoes
 */
public class Edible{
    protected String drawable;
    protected int score;
    protected int lives;
    protected String sound;
    private Random rand = new Random();
    protected int x=960,y=rand.nextInt(5)*120,w=120,h=120;
    protected static int step=2;

    public Edible(int score, int lives, String drawable) {
        this.drawable=drawable;
        this.score=score;
        this.lives=lives;
    }
    public Edible(int score, int lives, String drawable, String sound) {
        this.drawable=drawable;
        this.score=score;
        this.lives=lives;
        this.sound = sound;
    }
    public void move() {
        x -= step;
    }
    public void playSound() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        // play Music
        String bip = "./Sounds/" + sound;
        File soundFile = new File(bip);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);
        audioClip.open(audioStream);
        audioClip.start();
        // End play Music
    }
}
