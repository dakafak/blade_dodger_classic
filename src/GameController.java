import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.SecureRandom;
import java.util.ArrayList;

public class GameController extends JPanel {

    long originalStartTime;
    long lastUpdateTime;
    long frameRateResetTime;
    long updateTimeDifference;
    long runningTime;

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
    int arenaDrawingHeightCenter;
    int arenaDrawingWidthCenter;
    int scaledSize;

    int startNewGameFontSize = 8;
    int startScreenFontSize = 12;
    Font startNewGameFont;
    Font startScreenFont;
    Font gameStatFont;

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

        arenaHeight = 100;
        arenaWidth = 150;

        double arenaHeightScale = .7;
        arenaDrawingWidthCenter = screenWidth / 2;
        arenaDrawingHeightCenter = screenHeight / 2;
        scaledSize = (int)Math.round((arenaHeightScale * screenHeight) / arenaHeight);

        startNewGameFont = new Font(Font.MONOSPACED, Font.PLAIN, scaledSize*startNewGameFontSize);
        gameStatFont = new Font(Font.MONOSPACED, Font.PLAIN, scaledSize*startNewGameFontSize);

        startScreenFont = new Font(Font.MONOSPACED, Font.PLAIN, scaledSize*startScreenFontSize);
    }

    public void start() {
        continueRunning = true;

        while(continueRunning){
            updateTimeDifference = System.currentTimeMillis() - lastUpdateTime;
            runningTime = System.currentTimeMillis() - originalStartTime;

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
            if(startDelay <= 0) {
                setPlayerDirection();
                player.move(deltaUpdate, -arenaWidth / 2, arenaWidth / 2, -arenaHeight / 2, arenaHeight / 2);//TODO add a variable for the half sizes of these to avoid additional divide operations every update

                for (Blade blade : blades) {
                    blade.move(deltaUpdate, -arenaWidth / 2, arenaWidth / 2, -arenaHeight / 2, arenaHeight / 2);//TODO add a variable for the half sizes of these to avoid additional divide operations every update
                    checkBladeCollisions(blade);
                }

                checkGoldCollisions();
            }

            startDelay -= updateTimeDifference;
        }

        repaint();
    }

    private void checkGoldCollisions(){
        for(int i = 0; i < goldCoins.size(); i++){
            Gold gold = goldCoins.get(i);
            if(gold.getGoldBounds().intersects(player.getPlayerBounds())){
                goldCoins.remove(gold);
                money++;
            }
        }
    }

    private void checkBladeCollisions(Blade blade){

    }

    private void setPlayerDirection(){
        if(wPressed && aPressed){//TODO fix this up with split up down directions and then translate that into these directions below - sooo pressing a and d without s or w stops the players movement ---
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
        } else {
            player.setDirection(null);
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(gameImages.background, 0, 0, this);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Update Delta: " + String.valueOf(deltaUpdate), 10, 30);
        if(gameState == GameState.main) {
            g2d.setFont(startScreenFont);
            g2d.setColor(Color.WHITE);
            String startString = "Press 'Space' to start";
            g2d.drawString(startString, arenaDrawingWidthCenter - g2d.getFontMetrics().stringWidth(startString)/2, arenaDrawingHeightCenter);
        } else if(gameState == GameState.game) {
            g.drawRect(getTranslatedX(-arenaWidth/2), getTranslatedY(-arenaHeight/2), getTranslatedDrawingSize(arenaWidth), getTranslatedDrawingSize(arenaHeight));

            g2d.setColor(Color.WHITE);
            g2d.drawString("Update Delta: " + String.valueOf(deltaUpdate), 10, 30);

            g2d.drawImage(player.getMovementImage(gameImages), getTranslatedX(player.getX()), getTranslatedY(player.getY()) - getStutterOffset(1), getTranslatedDrawingSize(player.getSize()), getTranslatedDrawingSize(player.getSize()), this);

            for(Blade blade : blades){
                g2d.drawImage(gameImages.bladeImages[(int)Math.floor((runningTime % 400) / 100)], getTranslatedX(blade.getX()), getTranslatedY(blade.getY()), getTranslatedDrawingSize(blade.getSize()), getTranslatedDrawingSize(blade.getSize()), this);
            }

            for(Gold coin : goldCoins){
                g2d.drawImage(gameImages.goldImage, getTranslatedX(coin.getX()), getTranslatedY(coin.getY()), getTranslatedDrawingSize(coin.getSize()), getTranslatedDrawingSize(coin.getSize()), this);
            }

            g2d.drawImage(gameImages.allImages, 0, 0, gameImages.allImages.getWidth(this)/2, gameImages.allImages.getHeight(this)/2, this);

            checkForAndDrawStartInfo(g2d);

            g2d.setFont(gameStatFont);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Level: " + currentLevel, getTranslatedX(-arenaWidth/2), getTranslatedY(-arenaHeight/2) - 5);

            g2d.setColor(Color.RED);
            g2d.drawString("Lives: " + currentLevel, getTranslatedX(-arenaWidth/2 + arenaWidth/3), getTranslatedY(-arenaHeight/2) - 5);

            g2d.setColor(Color.YELLOW);
            g2d.drawString("Gold: " + money, getTranslatedX(-arenaWidth/2 + (2 * arenaWidth/3)), getTranslatedY(-arenaHeight/2) - 5);
        }
    }

    private void checkForAndDrawStartInfo(Graphics2D g2d){
        if(startDelay > 0){
            String startString;
            g2d.setFont(startNewGameFont);

            if(startDelay > startDelayReadyTimeStamp){
                g2d.setColor(Color.RED);
                startString = "READY";

            } else if(startDelay > startDelaySetTimeStamp){
                g2d.setColor(Color.BLUE);
                startString = "SET";
            } else {
                g2d.setColor(Color.GREEN);
                startString = "DODGE";
            }

            g2d.drawString(startString, arenaDrawingWidthCenter - g2d.getFontMetrics().stringWidth(startString)/2, arenaDrawingHeightCenter);
        }
    }

    private int getTranslatedDrawingSize(double inputedGameSize){
        return (int)Math.round(inputedGameSize * scaledSize);
    }

    private int getTranslatedX(double inputedGameXcoordinate){
        return arenaDrawingWidthCenter + (int)Math.round(inputedGameXcoordinate * scaledSize);
    }

    private int getTranslatedY(double inputedGameYcoordinate){
        return arenaDrawingHeightCenter + (int)Math.round(inputedGameYcoordinate * scaledSize);
    }

    private int getStutterOffset(int stutterAmount){
        return ((int)Math.round((runningTime % 500) / 500.0) * scaledSize * stutterAmount);
    }

    boolean wPressed;
    boolean aPressed;
    boolean sPressed;
    boolean dPressed;

    public void keyDown(KeyEvent ke) {
        if(gameState == GameState.game) {
            if (ke.getKeyCode() == KeyEvent.VK_W) {
                wPressed = true;
            }
            if (ke.getKeyCode() == KeyEvent.VK_A) {
                aPressed = true;
            }
            if (ke.getKeyCode() == KeyEvent.VK_S) {
                sPressed = true;
            }
            if (ke.getKeyCode() == KeyEvent.VK_D) {
                dPressed = true;
            }
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

            if(ke.getKeyCode() == KeyEvent.VK_R){
                resetGame();
            }
        }
    }

    int characterToBladeDistanceMinimum = 20;
    Player player;
    Blade[] blades;
    ArrayList<Gold> goldCoins;
    int currentLevel;
    boolean readyForNextLevel;
    int lives;
    int money;
    int difficulty = 1;
    int startDelay = 0;
    int startDelayReadyTimeStamp;
    int startDelaySetTimeStamp;

    private void setupNewGame(){
        lives = 3;
        currentLevel = 1;
        money = 0;
        resetGame();
    }

    private void resetGame(){
        startDelay = 1000;
        startDelayReadyTimeStamp = (startDelay / 3) * 2;
        startDelaySetTimeStamp = startDelay / 3;

        player = new Player();
        blades = new Blade[4];
        goldCoins = new ArrayList<>(4);

        for(int i = 0; i < blades.length; i++) {
            Blade newBlade = new Blade(
                    getRandomPositionForObjectWhileAvoidingCenter(arenaWidth, characterToBladeDistanceMinimum),
                    getRandomPositionForObjectWhileAvoidingCenter(arenaHeight, characterToBladeDistanceMinimum),
                    currentLevel,
                    difficulty);
            blades[i] = newBlade;
        }

        for(int i = 0; i < 4; i++) {
            Gold newCoin = new Gold(
                    getRandomPositionForObjectWhileAvoidingCenter(arenaWidth, characterToBladeDistanceMinimum),
                    getRandomPositionForObjectWhileAvoidingCenter(arenaHeight, characterToBladeDistanceMinimum));
            goldCoins.add(newCoin);
        }
    }

    private void advanceGame(){
        currentLevel++;
    }

    private double getRandomPositionForObjectWhileAvoidingCenter(int dimensionSize, int characterToBladeDistanceMinimum){
        double updatedCoordinate = (Math.random() * (dimensionSize*.9 + dimensionSize*.05)) - dimensionSize/2;

        if(updatedCoordinate > -characterToBladeDistanceMinimum/2 &&
                updatedCoordinate < characterToBladeDistanceMinimum/2){
            if(Math.random() < .5){
                updatedCoordinate -= characterToBladeDistanceMinimum;
            } else {
                updatedCoordinate += characterToBladeDistanceMinimum;
            }
        }

        return updatedCoordinate;
    }

}
