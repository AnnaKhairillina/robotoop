package strategy;

import gui.RobotModel;

public class AcceleratingStrategy implements MovementStrategy {
    private double currentVelocity = 0.0;
    private final double acceleration = 1.0;

    @Override
    public void move(RobotModel model, double deltaTime) {
        if (!model.hasTarget()) return;

        currentVelocity += acceleration * deltaTime;

        currentVelocity = Math.min(currentVelocity, RobotModel.MAX_VELOCITY * 1.5);

        double angleToTarget = RobotModel.angleTo(model.getX(), model.getY(), model.getTargetX(), model.getTargetY());
        double angularVelocity = model.calculateAngularVelocity(angleToTarget);

        angularVelocity *= 1.0 + (currentVelocity / RobotModel.MAX_VELOCITY);


        model.move(currentVelocity, angularVelocity, deltaTime);
    }
}
