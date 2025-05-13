package gui;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RobotsManager {
    private final List<RobotModel> robots = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
    }

    public List<RobotModel> getRobots() {
        return robots;
    }

    public void shutdown() {
        executor.shutdown();
    }
}