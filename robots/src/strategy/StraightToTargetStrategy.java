
package strategy;

import gui.RobotModel;

public class StraightToTargetStrategy implements MovementStrategy {
    @Override
    public void move(RobotModel model, double deltaTime) {
        if (!model.hasTarget()) return;

        double velocity = RobotModel.MAX_VELOCITY;
        double angleToTarget = RobotModel.angleTo(model.getX(), model.getY(), model.getTargetX(), model.getTargetY());
        double angularVelocity = model.calculateAngularVelocity(angleToTarget);

        model.move(velocity, angularVelocity, deltaTime);
        model.notifyObserversUpdate();
    }
}