import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameImages {

    String startDirectory;
    Image background;
    Image playerLeft, playerRight;
    Image allImages;
    Image[] bladeImages;

    public GameImages(String startDirectory, int screenWidth, int screenHeight, GameController gameController){
        this.startDirectory = startDirectory;

        Image backgroundBaseImage = loadImage("main_large.png");

        BufferedImage bufferedBackground = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedBackground.getGraphics();
        short backgroundRepeat = (short) Math.ceil(screenWidth / (screenHeight * 1.0));
        for(short i = 0; i < backgroundRepeat; i++){
            g.drawImage(backgroundBaseImage, i*screenHeight, 0, screenHeight, screenHeight, null);
        }
        background = (new ImageIcon(bufferedBackground)).getImage();

        BufferedImage bufferedSprites = getBufferedImageFromFile("sprites_basic.png");
        allImages = (new ImageIcon(bufferedSprites)).getImage();

        playerLeft = bufferedSprites.getSubimage(512, 0, 128, 128);
        playerRight = bufferedSprites.getSubimage(640, 0, 128, 128);

        bladeImages = new Image[4];
        bladeImages[0] = bufferedSprites.getSubimage(128, 0, 128, 128);
        bladeImages[1] = bufferedSprites.getSubimage(128, 128, 128, 128);
        bladeImages[2] = bufferedSprites.getSubimage(128, 256, 128, 128);
        bladeImages[3] = bufferedSprites.getSubimage(128, 384, 128, 128);
    }

    private Image loadImage(String imageUrl){
        try {
            return ImageIO.read(new File(startDirectory + "/" + imageUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private BufferedImage getBufferedImageFromFile(String filePath){
        Image imageFile = loadImage(filePath);

        BufferedImage bufferedImage = new BufferedImage(imageFile.getWidth(null), imageFile.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics imageGraphics = bufferedImage.getGraphics();
        imageGraphics.drawImage(imageFile, 0, 0, imageFile.getWidth(null), imageFile.getHeight(null), null);

        return bufferedImage;
    }

}
