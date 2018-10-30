import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
        mainFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                gameController.keyDown(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                gameController.keyUp(e);
            }
        });
        gameController.start();
    }

}
