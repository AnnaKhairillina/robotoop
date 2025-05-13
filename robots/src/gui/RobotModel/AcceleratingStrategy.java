package gui.RobotModel;

public class AcceleratingStrategy implements MovementStrategy {
    @Override
    public void move(RobotModel model, double deltaTime) {
        if (!model.hasTarget()) return;

        double distance = RobotModel.distance(model.getX(), model.getY(), model.getTargetX(), model.getTargetY());
        double velocity = Math.min(RobotModel.MAX_VELOCITY * 1.5, Math.max(0.1, distance * 0.15));
        double angleToTarget = RobotModel.angleTo(model.getX(), model.getY(), model.getTargetX(), model.getTargetY());
        double angularVelocity = model.calculateAngularVelocity(angleToTarget);

        model.move(velocity, angularVelocity, deltaTime);
        model.notifyObserversUpdate();
    }
}
