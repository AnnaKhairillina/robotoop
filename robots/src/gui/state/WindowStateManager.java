package gui.state;

import gui.Frames.GameWindow;
import gui.Frames.LogWindow;
import gui.Frames.MainApplicationFrame;
import log.Logger;
import javax.swing.JInternalFrame;
import java.io.*;
import java.util.*;

public class WindowStateManager {
    private static final String CONFIG_PATH = System.getProperty("user.home") + "/app_window_states.properties";
    private static final String MAIN_WINDOW_KEY = "mainWindow";

    public static void saveStates(MainApplicationFrame mainFrame) {
        Properties props = new Properties();
        Map<String, String> mainState = mainFrame.getWindowState();
        mainState.forEach((key, value) ->
                props.setProperty(MAIN_WINDOW_KEY + "." + key, value));

        for (JInternalFrame frame : mainFrame.getDesktopPane().getAllFrames()) {
            if (frame instanceof Stateful) {
                String windowId = frame instanceof LogWindow ? "logWindow" :
                        frame instanceof GameWindow ? "gameWindow" :
                                "window_" + frame.hashCode();
                ((Stateful)frame).getWindowState().forEach((key, value) ->
                        props.setProperty(windowId + "." + key, value));
            }
        }

        try (OutputStream out = new FileOutputStream(CONFIG_PATH)) {
            props.store(out, "Windows states");
        } catch (IOException e) {
            Logger.error("Ошибка сохранения состояний окон: " + e.getMessage());
        }
    }

    public static Map<String, Map<String, String>> loadStates() {
        Map<String, Map<String, String>> states = new HashMap<>();
        Properties props = new Properties();

        try (InputStream in = new FileInputStream(CONFIG_PATH)) {
            props.load(in);

            props.stringPropertyNames().forEach(key -> {
                String[] parts = key.split("\\.", 2);
                if (parts.length == 2) {
                    states.computeIfAbsent(parts[0], k -> new HashMap<>())
                            .put(parts[1], props.getProperty(key));
                }
            });
        } catch (IOException e) {
            Logger.debug("Не удалось загрузить состояния окон: " + e.getMessage());
        }

        return states;
    }
}