import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameImages {

    String startDirectory;
    Image background;
    public GameImages(String startDirectory, int screenWidth, int screenHeight, GameController gameController){
        this.startDirectory = startDirectory;

        Image backgroundBaseImage = loadImage("main_large.png");
        BufferedImage bufferedBackground = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_USHORT_555_RGB);
        Graphics g = bufferedBackground.getGraphics();
        short backgroundRepeat = (short) Math.ceil(screenWidth / (screenHeight * 1.0));
        for(short i = 0; i < backgroundRepeat; i++){
            g.drawImage(backgroundBaseImage, i*screenHeight, 0, screenHeight, screenHeight, gameController);
        }
        background = (new ImageIcon(bufferedBackground)).getImage();
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
