package gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;

public class WindowStateManager {
    private static final String CONFIG_FILE = System.getProperty("user.home") + "/robots_app_config.properties";

    public static void saveWindowState(JInternalFrame frame, String windowId) {
        if (frame == null) return;

        Properties props = loadProperties();

        Rectangle bounds = frame.getBounds();
        props.setProperty(windowId + ".x", String.valueOf(bounds.x));
        props.setProperty(windowId + ".y", String.valueOf(bounds.y));
        props.setProperty(windowId + ".width", String.valueOf(bounds.width));
        props.setProperty(windowId + ".height", String.valueOf(bounds.height));
        props.setProperty(windowId + ".isIcon", String.valueOf(frame.isIcon()));
        props.setProperty(windowId + ".isMaximum", String.valueOf(frame.isMaximum()));

        saveProperties(props);
    }

    public static void saveMainFrameState(JFrame frame) {
        Properties props = loadProperties();

        Rectangle bounds = frame.getBounds();
        props.setProperty("mainFrame.x", String.valueOf(bounds.x));
        props.setProperty("mainFrame.y", String.valueOf(bounds.y));
        props.setProperty("mainFrame.width", String.valueOf(bounds.width));
        props.setProperty("mainFrame.height", String.valueOf(bounds.height));
        props.setProperty("mainFrame.extendedState", String.valueOf(frame.getExtendedState()));

        saveProperties(props);
    }

    public static void restoreWindowState(JInternalFrame frame, String windowId) {
        Properties props = loadProperties();

        if (props.containsKey(windowId + ".x")) {
            try {
                int x = Integer.parseInt(props.getProperty(windowId + ".x"));
                int y = Integer.parseInt(props.getProperty(windowId + ".y"));
                int width = Integer.parseInt(props.getProperty(windowId + ".width"));
                int height = Integer.parseInt(props.getProperty(windowId + ".height"));
                boolean isIcon = Boolean.parseBoolean(props.getProperty(windowId + ".isIcon"));
                boolean isMaximum = Boolean.parseBoolean(props.getProperty(windowId + ".isMaximum"));

                frame.setBounds(x, y, width, height);
                if (isMaximum) frame.setMaximum(true);
                if (isIcon) frame.setIcon(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void restoreMainFrameState(JFrame frame) {
        Properties props = loadProperties();

        if (props.containsKey("mainFrame.x")) {
            try {
                int x = Integer.parseInt(props.getProperty("mainFrame.x"));
                int y = Integer.parseInt(props.getProperty("mainFrame.y"));
                int width = Integer.parseInt(props.getProperty("mainFrame.width"));
                int height = Integer.parseInt(props.getProperty("mainFrame.height"));
                int extendedState = Integer.parseInt(props.getProperty("mainFrame.extendedState"));

                frame.setBounds(x, y, width, height);
                frame.setExtendedState(extendedState);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream is = new FileInputStream(CONFIG_FILE)) {
            props.load(is);
        } catch (IOException ignored) {}
        return props;
    }

    private static void saveProperties(Properties props) {
        try (OutputStream os = new FileOutputStream(CONFIG_FILE)) {
            props.store(os, "Window states");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}