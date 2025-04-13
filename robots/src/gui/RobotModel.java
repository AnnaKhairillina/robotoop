package gui;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class RobotModel extends Observable {
    private volatile double x = 100;
    private volatile double y = 100;
    private volatile double direction = 0;

    private volatile int targetX = 150;
    private volatile int targetY = 100;

    private final double maxVelocity = 0.1;
    private final double maxAngularVelocity = 0.001;

    public RobotModel() {
        Timer timer = new Timer("Robot Model Timer", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updatePosition();
            }
        }, 0, 10);
    }

    public void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getDirection() { return direction; }

    private void updatePosition() {
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < 0.5) return;

        double angleToTarget = Math.atan2(dy, dx);
        angleToTarget = normalize(angleToTarget);

        double angularVelocity = 0;
        if (angleToTarget > direction) angularVelocity = maxAngularVelocity;
        if (angleToTarget < direction) angularVelocity = -maxAngularVelocity;

        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);

        double velocity = maxVelocity;

        double newX = x + velocity / angularVelocity *
                (Math.sin(direction + angularVelocity * 10) - Math.sin(direction));
        if (!Double.isFinite(newX)) newX = x + velocity * 10 * Math.cos(direction);

        double newY = y - velocity / angularVelocity *
                (Math.cos(direction + angularVelocity * 10) - Math.cos(direction));
        if (!Double.isFinite(newY)) newY = y + velocity * 10 * Math.sin(direction);

        x = newX;
        y = newY;
        direction = normalize(direction + angularVelocity * 10);

        setChanged();
        notifyObservers();
    }

    private double normalize(double angle) {
        while (angle < 0) angle += 2 * Math.PI;
        while (angle >= 2 * Math.PI) angle -= 2 * Math.PI;
        return angle;
    }

    private double applyLimits(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
