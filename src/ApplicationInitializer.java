import javax.swing.*;
import java.awt.*;

public class ApplicationInitializer {

    public static void main(String[] args){
        ApplicationInitializer newApp = new ApplicationInitializer();
    }

    JFrame mainFrame;
    private GameController gameController;

    public ApplicationInitializer(){
        mainFrame = new JFrame("Blade Dodger Classic");
        mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        mainFrame.setUndecorated(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        setupGameController();
    }

    private void setupGameController(){
        gameController = new GameController(Toolkit.getDefaultToolkit().getScreenSize());
        mainFrame.setContentPane(gameController);
        gameController.start();
    }

}
