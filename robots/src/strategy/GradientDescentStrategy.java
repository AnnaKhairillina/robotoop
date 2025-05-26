package strategy;

import gui.RobotModel;

import java.util.*;

public class GradientDescentStrategy implements MovementStrategy {
    private static final int NUM_PATHS = 100; // Количество генерируемых путей (n)
    private static final int PATH_STEPS = 25; // Количество шагов в каждом пути (k)
    private static final double STEP_DURATION = 0.02; // Длительность одного шага
    private static final double MAX_VELOCITY = RobotModel.MAX_VELOCITY; // Максимальная линейная скорость
    private static final double MAX_ANGULAR_VELOCITY = 10; // Максимальная угловая скорость
    private static final Random random = new Random();

    @Override
    public void move(RobotModel model, double deltaTime) {
        if (!model.hasTarget()) return;

        // Проверка, достиг ли робот цели (расстояние < 5)
        double distanceToTarget = RobotModel.distance(model.getX(), model.getY(), model.getTargetX(), model.getTargetY());

        if (distanceToTarget < 5) {
            model.setTarget(model.getTargetX(), model.getTargetY());
            model.notifyObserversUpdate();
            return; }

        // Генерация и оценка случайных путей

        Path bestPath = generateBestPath(model);

        // Выполнение первого шага лучшего пути
        if (bestPath != null && bestPath.steps.length > 0) {
            Step firstStep = bestPath.steps[0];
            model.move(firstStep.velocity, firstStep.angularVelocity, deltaTime);
        }
    }

    private Path generateBestPath(RobotModel model) {
        Path bestPath = null;
        double bestCost = Double.MAX_VALUE;

        // Генерация NUM_PATHS путей и выбор лучшего
        for (int i = 0; i < NUM_PATHS; i++) {
            Path path = generateRandomPath(model);
            double cost = evaluatePath(path, model);
            if (cost < bestCost) {
                bestCost = cost;
                bestPath = path;
            }
        }

        return bestPath;
    }

    private Path generateRandomPath(RobotModel model) {
        Path path = new Path();
        double currentX = model.getX();
        double currentY = model.getY();
        double currentDirection = model.getDirection();

        for (int i = 0; i < PATH_STEPS; i++) {
            // Случайная линейная скорость (от 0.5 до MAX_VELOCITY)
            double velocity = 0.5 + random.nextDouble() * (MAX_VELOCITY - 0.5);

            // Случайная угловая скорость с учетом направления к цели
            double angleToTarget = RobotModel.angleTo(currentX, currentY, model.getTargetX(), model.getTargetY());
            double angleDiff = RobotModel.normalizeAngle(angleToTarget - currentDirection);
            double angularVelocity = angleDiff * 0.1 + (random.nextDouble() - 0.5) * MAX_ANGULAR_VELOCITY * 0.3;

            // Ограничение угловой скорости
            angularVelocity = Math.max(-MAX_ANGULAR_VELOCITY, Math.min(MAX_ANGULAR_VELOCITY, angularVelocity));

            // Симуляция шага
            currentDirection = RobotModel.normalizeAngle(currentDirection + angularVelocity * STEP_DURATION);
            currentX += velocity * Math.cos(currentDirection) * STEP_DURATION;
            currentY += velocity * Math.sin(currentDirection) * STEP_DURATION;

            // Сохранение шага
            path.steps[i] = new Step(velocity, angularVelocity);
        }

        path.finalX = currentX;
        path.finalY = currentY;
        return path;
    }

    private double evaluatePath(Path path, RobotModel model) {
        // Функция оценки: расстояние от конечной точки пути до цели
        return RobotModel.distance(path.finalX, path.finalY, model.getTargetX(), model.getTargetY());
    }

    private static class Path {
        Step[] steps = new Step[PATH_STEPS];
        double finalX;
        double finalY;

        Path() {
            for (int i = 0; i < PATH_STEPS; i++) {
                steps[i] = new Step(0, 0);
            }
        }
    }

    private static class Step {
        double velocity;
        double angularVelocity;

        Step(double velocity, double angularVelocity) {
            this.velocity = velocity;
            this.angularVelocity = angularVelocity;
        }
    }
}
