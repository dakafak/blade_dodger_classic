import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameImages {

    String startDirectory;
    Image background;
    Image playerLeft, playerRight;

    public GameImages(String startDirectory, int screenWidth, int screenHeight, GameController gameController){
        this.startDirectory = startDirectory;

        Image backgroundBaseImage = loadImage("main_large.png");
        Image mainSpriteImage = loadImage("sprites_basic.png");

        BufferedImage bufferedBackground = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_USHORT_555_RGB);
        Graphics g = bufferedBackground.getGraphics();
        short backgroundRepeat = (short) Math.ceil(screenWidth / (screenHeight * 1.0));
        for(short i = 0; i < backgroundRepeat; i++){
            g.drawImage(backgroundBaseImage, i*screenHeight, 0, screenHeight, screenHeight, gameController);
        }
        background = (new ImageIcon(bufferedBackground)).getImage();

        BufferedImage allSubImages = new BufferedImage(1024, 512, BufferedImage.TYPE_USHORT_555_RGB);

//        BufferedImage playerLeftBuffered = new BufferedImage(128, 128, BufferedImage.TYPE_USHORT_555_RGB);
//        Graphics plGraphics = playerLeftBuffered.getGraphics();
//        plGraphics.drawImage(allSubImages.getSubimage(512, 0, 128, 128), 0, 0, 128, 128, gameController);
        playerLeft = allSubImages.getSubimage(512, 0, 128, 128);
        playerRight = allSubImages.getSubimage(640, 0, 128, 128);
    }

    private Image loadImage(String imageUrl){
        try {
            return ImageIO.read(new File(startDirectory + "/" + imageUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
