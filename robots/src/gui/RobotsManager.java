package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RobotsManager {
    private final List<RobotModel> robots = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final Random random = new Random();
    private int fieldWidth = 800;
    private int fieldHeight = 600;

    public void addRobot(RobotModel robot) {
        robots.add(robot);
    }

    public void setTargetForAll(int x, int y) {
        robots.forEach(robot -> robot.setTarget(x, y));
    }

    public void updateAll(double duration) {
        robots.forEach(robot ->
                executor.submit(() -> {
                    synchronized(robot) {
                        robot.update(duration);
                    }
                })
        );
        checkAndRespawnTargetIfReached();
    }

    public List<RobotModel> getRobots() {
        return robots;
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void checkAndRespawnTargetIfReached() {
        for (RobotModel robot : robots) {
            if (robot.hasTarget()) {
                double dist = RobotModel.distance(robot.getX(), robot.getY(), robot.getTargetX(), robot.getTargetY());
                if (dist < 10) {
                    int newX = random.nextInt(fieldWidth - 20) + 10;
                    int newY = random.nextInt(fieldHeight - 40) + 30;
                    setTargetForAll(newX, newY);
                    break;
                }
            }
        }
    }

    // Позволим передать размеры поля (например из GameVisualizer)
    public void setFieldSize(int width, int height) {
        this.fieldWidth = width;
        this.fieldHeight = height;
    }
}