import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

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
    GameState gameState;

    int arenaWidth;
    int arenaHeight;
    int arenaDrawingHeight;
    int arenaDrawingWidth;
    int scaledSize;

    public GameController(Dimension screenSize){
        //TODO in loop - tell state to run it's
        super.setSize(screenSize);
        this.screenWidth = screenSize.width;
        this.screenHeight = screenSize.height;
        this.gameState = GameState.main;
        backgroundRepeat = (short) Math.ceil(screenWidth / (screenHeight * 1.0));

        originalStartTime = System.currentTimeMillis();
        lastUpdateTime = System.currentTimeMillis();
        frameRateResetTime = System.currentTimeMillis() + 1000;

        gameImages = new GameImages("Images", screenWidth, screenHeight, this);

        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.black));

        arenaWidth = 100;
        arenaHeight = (int)Math.round((4.0 / 3) * arenaWidth);

        arenaDrawingWidth = (int)(Math.round(screenHeight) * .7);
        arenaDrawingHeight = (int)(Math.round((arenaHeight / arenaWidth) * screenHeight) * .7);
        scaledSize = arenaDrawingWidth / 10;
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
        if(gameState == GameState.game){
            setPlayerDirection();
            player.move(deltaUpdate);
        }

        repaint();
    }

    private void setPlayerDirection(){
        if(wPressed && aPressed){
            player.setDirection(PlayerDirection.upleft);
        } else if(wPressed && dPressed){
            player.setDirection(PlayerDirection.upright);
        } else if(sPressed && aPressed){
            player.setDirection(PlayerDirection.downleft);
        } else if(sPressed && dPressed){
            player.setDirection(PlayerDirection.downright);
        } else if(wPressed){
            player.setDirection(PlayerDirection.up);
        } else if(sPressed){
            player.setDirection(PlayerDirection.down);
        } else if(aPressed){
            player.setDirection(PlayerDirection.left);
        } else if(dPressed){
            player.setDirection(PlayerDirection.right);
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(gameImages.background, 0, 0, this);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Update Delta: " + String.valueOf(deltaUpdate), 10, 30);
        if(gameState == GameState.main) {
            g2d.setColor(Color.WHITE);
            g2d.drawString("Press 'Space' to start", screenWidth/2, screenHeight/2);
        } else if(gameState == GameState.game) {
            g.drawRect((int)Math.round(screenWidth / 2.0 - arenaDrawingWidth / 2.0), (int)Math.round(screenHeight / 2.0 - arenaDrawingHeight / 2.0), arenaDrawingWidth, arenaDrawingHeight);

            g2d.setColor(Color.WHITE);
            g2d.drawString("Update Delta: " + String.valueOf(deltaUpdate), 10, 30);

            g2d.drawImage(gameImages.playerLeft, getTranslatedX(), getTranslatedY(), scaledSize, scaledSize, this);
        }
    }

    private int getTranslatedX(){
        return (int)Math.round(arenaDrawingWidth * ((player.getX() - arenaWidth/2) / arenaWidth));
    }

    private int getTranslatedY(){
        return (int)Math.round(arenaDrawingHeight * ((player.getY() - arenaHeight/2) / arenaHeight));
    }

    boolean wPressed;
    boolean aPressed;
    boolean sPressed;
    boolean dPressed;

    public void keyDown(KeyEvent ke) {
        if(ke.getKeyCode() == KeyEvent.VK_W){
            wPressed = true;
        }
        if(ke.getKeyCode() == KeyEvent.VK_A){
            aPressed = true;
        }
        if(ke.getKeyCode() == KeyEvent.VK_S){
            sPressed = true;
        }
        if(ke.getKeyCode() == KeyEvent.VK_D){
            dPressed = true;
        }
    }

    public void keyUp(KeyEvent ke) {
        if(gameState == GameState.main) {
            if(ke.getKeyCode() == KeyEvent.VK_SPACE) {
                setupNewGame();
                gameState = GameState.game;
            }
        } else if(gameState == GameState.game) {
            if(ke.getKeyCode() == KeyEvent.VK_W){
                wPressed = false;
            }
            if(ke.getKeyCode() == KeyEvent.VK_A){
                aPressed = false;
            }
            if(ke.getKeyCode() == KeyEvent.VK_S){
                sPressed = false;
            }
            if(ke.getKeyCode() == KeyEvent.VK_D){
                dPressed = false;
            }
        }
    }

    Player player;
    private void setupNewGame(){
        player = new Player();
    }

}
