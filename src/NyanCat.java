import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * @author Sad Potateos
 */
public class NyanCat {
    protected boolean drugged = false;
    protected String drawable;
    protected String name = "nyan.gif";
    protected int lives = 3;
    protected int y = 2 * 120;
    protected final int h = 120, w = 312;
    protected int score = 0;
    protected String playerName;
    protected boolean alive  = true;

    //drugNyan by mazen

    public NyanCat(int y, String name) {
        this.y = 120 * y;
        this.name = name;
        this.drawable = name;
    }

    public NyanCat() {
    }

    public void drugNyan() {
        drawable = "drug.gif";
        drugged = true;
        Timer timer;
        timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                drawable = name;
                drugged = false;
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    //functions to move cat up and down easily
    public void nyanUp() {
        if (y >= 120) {
            y -= 120;
        }
    }

    public void nyanDown() {
        if (y <= 360) {
            y += 120;
        }
    }

}
