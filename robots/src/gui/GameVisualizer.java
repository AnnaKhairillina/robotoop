package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

public class GameVisualizer extends JPanel {
    private final RobotModel model;

    public GameVisualizer(RobotModel model) {
        this.model = model;

        Timer timer = new Timer("model_updater", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.update();
                repaint();
            }
        }, 0, 10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setTarget(e.getX(), e.getY());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, model);
        drawTarget(g2d, model);
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
        if (!model.hasTarget()) {
            return;
        }

        g.setTransform(new AffineTransform());

        int targetX = model.getTargetX();
        int targetY = model.getTargetY();

        g.setColor(Color.RED);
        g.fillOval(targetX - 5, targetY - 5, 10, 10);
    }
}
