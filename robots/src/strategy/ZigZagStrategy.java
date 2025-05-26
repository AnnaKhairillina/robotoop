package strategy;

import gui.RobotModel;

public class ZigZagStrategy implements MovementStrategy {
    private boolean zig = true;
    private double timeAccumulator = 0.0;

    @Override
    public void move(RobotModel model, double deltaTime) {
        if (!model.hasTarget()) return;

        timeAccumulator += deltaTime;
        if (timeAccumulator >= 0.5) {
            zig = !zig;
            timeAccumulator = 0;
        }

        double distance = RobotModel.distance(model.getX(), model.getY(), model.getTargetX(), model.getTargetY());
        if (distance < 2) return;

        double velocity = Math.min(RobotModel.MAX_VELOCITY, distance * 0.1);
        double angleToTarget = RobotModel.angleTo(model.getX(), model.getY(), model.getTargetX(), model.getTargetY());
        double offset = zig ? Math.PI / 12 : -Math.PI / 12;

        double angularVelocity = model.calculateAngularVelocity(angleToTarget + offset);
        model.move(velocity, angularVelocity, deltaTime);
        model.notifyObserversUpdate();
    }
}
