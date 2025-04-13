package gui;

import java.util.Observable;

public class RobotModel extends Observable {
    private volatile double robotX = 100;
    private volatile double robotY = 100;
    private volatile double direction = 0;
    private volatile int targetX = 150;
    private volatile int targetY = 100;

    public static final double MAX_VELOCITY = 0.1;
    public static final double MAX_ANGULAR_VELOCITY = 0.001;

    public void update() {
        double distance = distance(targetX, targetY, robotX, robotY);
        if (distance < 0.5) return;

        double velocity = MAX_VELOCITY;
        double angleToTarget = angleTo(robotX, robotY, targetX, targetY);
        double angularVelocity = calculateAngularVelocity(angleToTarget);

        move(velocity, angularVelocity, 10);
        setChanged();
        notifyObservers();
    }

    public void setTarget(int x, int y) {
        this.targetX = x;
        this.targetY = y;
        setChanged();
        notifyObservers();
    }

    public double getX() { return robotX; }
    public double getY() { return robotY; }
    public double getDirection() { return direction; }

    private double calculateAngularVelocity(double angleToTarget) {
        double diff = angleToTarget - direction;
        diff = normalizeAngle(diff);
        return Math.signum(diff) * MAX_ANGULAR_VELOCITY;
    }

    private void move(double velocity, double angularVelocity, double duration) {
        velocity = clamp(velocity, 0, MAX_VELOCITY);
        angularVelocity = clamp(angularVelocity, -MAX_ANGULAR_VELOCITY, MAX_ANGULAR_VELOCITY);

        double newX = robotX + velocity / angularVelocity *
                (Math.sin(direction + angularVelocity * duration) - Math.sin(direction));
        if (!Double.isFinite(newX)) {
            newX = robotX + velocity * duration * Math.cos(direction);
        }

        double newY = robotY - velocity / angularVelocity *
                (Math.cos(direction + angularVelocity * duration) - Math.cos(direction));
        if (!Double.isFinite(newY)) {
            newY = robotY + velocity * duration * Math.sin(direction);
        }

        robotX = newX;
        robotY = newY;
        direction = normalizeAngle(direction + angularVelocity * duration);
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

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}