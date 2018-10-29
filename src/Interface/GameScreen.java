package Interface;

import java.awt.event.KeyEvent;
import java.util.Map;

public class GameScreen extends Screen {

    public GameScreen(Map<String, Object> screenParameters) {
        super();
        screenType = ScreenType.GAME;
    }

    @Override
    public void pressKey(KeyEvent event) {

    }
}
