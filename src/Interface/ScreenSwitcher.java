package Interface;

import javax.swing.*;
import java.util.Map;

public class ScreenSwitcher {

    Map<ScreenType, Screen> screens;
    JFrame mainFrame;

    public ScreenSwitcher(JFrame frame){
        mainFrame = frame;
    }

    public void switchPanel(Screen currentScreen, ScreenType nextScreen, boolean disposeCurrentScreen, Map<String, Object> screenParameters){
        if(screens.containsKey(nextScreen) && screens.get(nextScreen) != null){
            mainFrame.removeAll();
            mainFrame.add(screens.get(nextScreen));
        } else {
            Screen newScreen = null;
            switch (nextScreen) {
                case MAIN_MENU:
                    newScreen = new MainMenuScreen(screenParameters);
                    break;
                case GAME:
                    newScreen = new GameScreen(screenParameters);
                    break;
            }

            if(newScreen != null){
                screens.put(nextScreen, newScreen);
                mainFrame.removeAll();
                mainFrame.add(newScreen);
            }
        }

        if(disposeCurrentScreen){
            if(currentScreen.getScreenType() != null) {
                screens.remove(currentScreen.getScreenType());
            }
        }
    }

}
