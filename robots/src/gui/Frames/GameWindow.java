package gui.Frames;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import gui.GameVisualizer;
import gui.RobotsManager;
import gui.state.StatefulInternalFrame;

public class GameWindow extends StatefulInternalFrame {
    private final GameVisualizer visualizer;

    public GameWindow(RobotsManager robotsManager) {
        super("Игровое поле", true, true, true, true);
        visualizer = new GameVisualizer(robotsManager);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        setSize(400, 400);
    }

    @Override
    public Map<String, String> getWindowState() {
        Map<String, String> state = super.getWindowState();
        return state;
    }

    @Override
    public void restoreState(Map<String, String> state) {
        super.restoreState(state);
    }
}
