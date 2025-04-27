package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

import gui.state.StatefulInternalFrame;

public class GameWindow extends StatefulInternalFrame {
    private final GameVisualizer m_visualizer;

    public GameWindow(ArrayList<RobotModel> model) {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer(model);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
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
