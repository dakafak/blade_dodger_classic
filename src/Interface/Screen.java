package Interface;

import javax.swing.*;
import java.awt.event.KeyEvent;

public abstract class Screen extends JPanel {

    ScreenType screenType;

    public abstract void pressKey(KeyEvent event);

    public ScreenType getScreenType() {
        return screenType;
    }
}
