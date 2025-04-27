package gui;

import java.util.Observable;

public class RobotModel extends Observable {
    private volatile double robotX = 100;
    private volatile double robotY = 100;
    private volatile double direction = 0;
    private volatile int targetX = 150;
    private volatile int targetY = 100;
    private volatile boolean hasTarget = false;

    public static final double MAX_VELOCITY = 5.0;
    public static final double MAX_ANGULAR_VELOCITY = 0.05;

    public void update() {
        if (!hasTarget) return;

        double distance = distance(targetX, targetY, robotX, robotY);
        if (distance < 0.5) {
            hasTarget = false;
            setChanged();
            notifyObservers();
            return;
        }

        double velocity = Math.min(MAX_VELOCITY, distance * 0.1);
        double angleToTarget = angleTo(robotX, robotY, targetX, targetY);
        double angularVelocity = calculateAngularVelocity(angleToTarget);

        move(velocity, angularVelocity, 0.1);
        setChanged();
        notifyObservers();
    }

    public void setTarget(int x, int y) {
        this.targetX = x;
        this.targetY = y;
        this.hasTarget = true;
        setChanged();
        notifyObservers();
    }

    public double getX() { return robotX; }
    public double getY() { return robotY; }
    public double getDirection() { return direction; }
    public int getTargetX() { return targetX; }
    public int getTargetY() { return targetY; }
    public boolean hasTarget() { return hasTarget; }

    private double calculateAngularVelocity(double angleToTarget) {
        double diff = angleToTarget - direction;
        diff = normalizeAngle(diff);
        return diff * 0.1;
    }

    private void move(double velocity, double angularVelocity, double duration) {
        direction = normalizeAngle(direction + angularVelocity * duration);
        robotX += velocity * Math.cos(direction) * duration;
        robotY += velocity * Math.sin(direction) * duration;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        return normalizeAngle(Math.atan2(toY - fromY, toX - fromX));
    }

    private static double normalizeAngle(double angle) {
        while (angle < 0) angle += 2 * Math.PI;
        while (angle >= 2 * Math.PI) angle -= 2 * Math.PI;
        return angle;
    }
}
