package gui;

import gui.RobotModel.RobotModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameVisualizer extends JPanel {
    private final List<RobotModel> robots;

    public GameVisualizer(ArrayList<RobotModel> robots) {
        this.robots = robots;

        Timer timer = new Timer("robots_updater", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                robots.parallelStream().forEach(robot -> {
                    synchronized(robot) {
                        robot.update(0.1);
                    }
                });
                SwingUtilities.invokeLater(() -> repaint());
            }
        }, 0, 30);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (RobotModel robot : robots) {
                    robot.setTarget(e.getX(), e.getY());
                }
            }
        });
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (RobotModel robot : robots) {
            drawRobot((Graphics2D) g, robot);
        }

        if (!robots.isEmpty() && robots.get(0).hasTarget()) {
            drawTarget((Graphics2D) g, robots.get(0));
        }
    }

    private void drawRobot(Graphics2D g, RobotModel model) {
        int x = (int) model.getX();
        int y = (int) model.getY();
        double direction = model.getDirection();

        AffineTransform t = AffineTransform.getRotateInstance(direction, x, y);
        g.setTransform(t);

        g.setColor(Color.MAGENTA);
        g.fillOval(x - 15, y - 5, 30, 10);
        g.setColor(Color.BLACK);
        g.drawOval(x - 15, y - 5, 30, 10);

        g.setColor(Color.WHITE);
        g.fillOval(x + 10, y - 2, 5, 5);
        g.setColor(Color.BLACK);
        g.drawOval(x + 10, y - 2, 5, 5);
    }

    private void drawTarget(Graphics2D g, RobotModel model) {
        if (!model.hasTarget()) return;
        g.setTransform(new AffineTransform());

        int targetX = model.getTargetX();
        int targetY = model.getTargetY();

        g.setColor(Color.RED);
        g.fillOval(targetX - 5, targetY - 5, 10, 10);
    }
}
