import javax.swing.*;
import java.awt.*;

public class GameController extends JPanel {

    long originalStartTime;
    long lastUpdateTime;
    long frameRateResetTime;
    long updateTimeDifference;

    boolean continueRunning;
    short updateCap = 7;
    short baseDeltaTime = 10;
    float deltaUpdate = 1.0f;

    private GameImages gameImages;
    int screenWidth;
    int screenHeight;
    short backgroundRepeat;

    public GameController(Dimension screenSize){
        //TODO in loop - tell state to run it's
        super.setSize(screenSize);
        this.screenWidth = screenSize.width;
        this.screenHeight = screenSize.height;
        backgroundRepeat = (short) Math.ceil(screenWidth / (screenHeight * 1.0));

        originalStartTime = System.currentTimeMillis();
        lastUpdateTime = System.currentTimeMillis();
        frameRateResetTime = System.currentTimeMillis() + 1000;

        gameImages = new GameImages("Images", screenWidth, screenHeight, this);

        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void start() {
        continueRunning = true;

        while(continueRunning){
            updateTimeDifference = System.currentTimeMillis() - lastUpdateTime;

            if(updateTimeDifference >= updateCap) {
                deltaUpdate = ((updateTimeDifference) * 1.0f) / baseDeltaTime;

                //TODO add update call here
                refresh();

                lastUpdateTime = System.currentTimeMillis();
            }
        }
    }

    private void refresh(){
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(gameImages.background, 0, 0, this);

        int arenaHeight = (int)Math.round(.7 * screenHeight);
        int arenaWidth = (int)Math.round((4.0 / 3) * screenHeight);
        g.drawRect((int)Math.round(screenWidth / 2.0 - arenaWidth / 2.0), (int)Math.round(screenHeight / 2.0 - arenaHeight / 2.0), arenaWidth, arenaHeight);

        g2d.setColor(Color.WHITE);
        g2d.drawString("Update Delta: " + String.valueOf(deltaUpdate), 10, 30);
        g2d.setColor(Color.RED);
        g2d.fillRect(100 + (int)(System.currentTimeMillis() % 1500), 100, 50, 50);
    }

}
