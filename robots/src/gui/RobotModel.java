package gui;


import java.util.Observable;

public class RobotModel extends Observable {
    protected volatile double robotX = 100;
    protected volatile double robotY = 100;
    protected volatile double direction = 0;
    protected volatile int targetX = 150;
    protected volatile int targetY = 100;
    protected volatile boolean hasTarget = false;

    protected MovementStrategy strategy;

    public static final double MAX_VELOCITY = 5.0;
    public static final double MAX_ANGULAR_VELOCITY = 0.05;

    public RobotModel(MovementStrategy strategy, double startX, double startY) {
        this.strategy = strategy;
        this.robotX = startX;
        this.robotY = startY;
    }

    public synchronized void update(double deltaTime) {
        if (strategy != null) {
            strategy.move(this, deltaTime);
        }
    }

    public synchronized void setTarget(int x, int y) {
        this.targetX = x;
        this.targetY = y;
        this.hasTarget = true;
        notifyObserversUpdate();
    }

    public void notifyObserversUpdate() {
        setChanged();
        notifyObservers();
    }

    public double getX() { return robotX; }
    public double getY() { return robotY; }
    public double getDirection() { return direction; }
    public int getTargetX() { return targetX; }
    public int getTargetY() { return targetY; }
    public boolean hasTarget() { return hasTarget; }

    public double calculateAngularVelocity(double angleToTarget) {
        double diff = angleToTarget - direction;
        diff = normalizeAngle(diff);
        return diff * 0.1;
    }

    public synchronized void move(double velocity, double angularVelocity, double duration) {
        direction = normalizeAngle(direction + angularVelocity * duration);
        robotX += velocity * Math.cos(direction) * duration;
        robotY += velocity * Math.sin(direction) * duration;
        notifyObserversUpdate();
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double angleTo(double fromX, double fromY, double toX, double toY) {
        return normalizeAngle(Math.atan2(toY - fromY, toX - fromX));
    }

    public static double normalizeAngle(double angle) {
        while (angle < 0) angle += 2 * Math.PI;
        while (angle >= 2 * Math.PI) angle -= 2 * Math.PI;
        return angle;
    }
}
