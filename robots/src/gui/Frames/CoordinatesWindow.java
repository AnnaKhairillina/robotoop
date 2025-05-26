package gui.Frames;

import gui.RobotModel;
import gui.RobotsManager;
import gui.state.StatefulInternalFrame;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public class CoordinatesWindow extends StatefulInternalFrame implements Observer {
    private final JPanel robotsPanel = new JPanel();
    private final RobotsManager robotsManager;

    public CoordinatesWindow(RobotsManager robotsManager) {
        super("Координаты роботов", true, true, true, true);
        this.robotsManager = robotsManager;

        setLayout(new BorderLayout());
        robotsPanel.setLayout(new BoxLayout(robotsPanel, BoxLayout.Y_AXIS));
        robotsPanel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(robotsPanel, BorderLayout.CENTER);

        robotsManager.getRobots().forEach(robot -> robot.addObserver(this));

        setSize(250, 200);
        updateAll();
    }


    @Override
    public void update(Observable o, Object arg) {
        updateAll();
    }

    private void updateAll() {
        robotsPanel.removeAll();
        for (int i = 0; i < robotsManager.getRobots().size(); i++) {
            RobotModel robot = robotsManager.getRobots().get(i);
            JLabel label = new JLabel(
                    String.format("Robot %d: X: %.2f, Y: %.2f", i + 1, robot.getX(), robot.getY())
            );
            label.setFont(new Font("Monospaced", Font.PLAIN, 14));
            robotsPanel.add(label);
        }
        robotsPanel.revalidate();
        robotsPanel.repaint();
    }
}