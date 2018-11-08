import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.SecureRandom;

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
        arenaWidth = 130;

        double arenaHeightScale = .7;
        arenaDrawingWidthCenter = screenWidth / 2;
        arenaDrawingHeightCenter = screenHeight / 2;
        scaledSize = (int)Math.round((arenaHeightScale * screenHeight) / arenaHeight);
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
            setPlayerDirection();
            player.move(deltaUpdate, -arenaWidth/2, arenaWidth/2, -arenaHeight/2, arenaHeight/2);//TODO add a variable for the half sizes of these to avoid additional divide operations every update
            for(Blade blade : blades) {
                blade.move(deltaUpdate, -arenaWidth / 2, arenaWidth / 2, -arenaHeight / 2, arenaHeight / 2);//TODO add a variable for the half sizes of these to avoid additional divide operations every update
            }
        }

        repaint();
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
            g2d.setColor(Color.WHITE);
            g2d.drawString("Press 'Space' to start", screenWidth/2, screenHeight/2);
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

        if(ke.getKeyCode() == KeyEvent.VK_ENTER){
            resetGame();
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

    int characterToBladeDistanceMinimum = 20;
    Player player;
    Blade[] blades;
    Gold[] goldCoins;
    int currentLevel;//TODO add new method for continuing a level that shares the same general setup like player and blades, but change how level is set or added to
    private void setupNewGame(){
        currentLevel = 1;
        resetGame();
    }

    private void resetGame(){
        player = new Player();
        blades = new Blade[4];
        goldCoins = new Gold[4];

        for(int i = 0; i < blades.length; i++) {
            Blade newBlade = new Blade(
                    getRandomPositionForObjectWhileAvoidingCenter(arenaWidth, characterToBladeDistanceMinimum),
                    getRandomPositionForObjectWhileAvoidingCenter(arenaHeight, characterToBladeDistanceMinimum),
                    currentLevel);
            blades[i] = newBlade;
        }

        for(int i = 0; i < goldCoins.length; i++) {
            Gold newCoin = new Gold(
                    getRandomPositionForObjectWhileAvoidingCenter(arenaWidth, characterToBladeDistanceMinimum),
                    getRandomPositionForObjectWhileAvoidingCenter(arenaHeight, characterToBladeDistanceMinimum));
            goldCoins[i] = newCoin;
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
