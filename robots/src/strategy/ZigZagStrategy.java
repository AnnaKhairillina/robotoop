package strategy;

import gui.RobotModel;
import strategy.MovementStrategy;

public class ZigZagStrategy implements MovementStrategy {
    private boolean zig = true;
    private double lastSwitchTime = 0;

    @Override
    public void move(RobotModel model, double deltaTime) {
        if (!model.hasTarget()) return;

        double currentTime = System.currentTimeMillis() / 1000.0;
        if (currentTime - lastSwitchTime > 0.5) {
            zig = !zig;
            lastSwitchTime = currentTime;
        }

        double distance = RobotModel.distance(model.getX(), model.getY(), model.getTargetX(), model.getTargetY());
        if (distance < 5) {
            model.setTarget(model.getTargetX(), model.getTargetY());
            return;
        }
        double velocity = Math.min(RobotModel.MAX_VELOCITY, distance * 0.1);

        double angleToTarget = RobotModel.angleTo(model.getX(), model.getY(), model.getTargetX(), model.getTargetY());
        double offset = zig ? Math.PI / 8 : -Math.PI / 8;
        double angularVelocity = model.calculateAngularVelocity(angleToTarget + offset);

        model.move(velocity, angularVelocity, deltaTime);
        model.notifyObserversUpdate();
    }
}