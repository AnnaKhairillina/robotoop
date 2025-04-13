package gui;

import gui.RobotModel;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JInternalFrame {
    public GameWindow(RobotModel model) {
        super("Игровое поле", true, true, true, true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new GameVisualizer(model), BorderLayout.CENTER);
        getContentPane().add(panel);
        setSize(400, 400);
    }
}
