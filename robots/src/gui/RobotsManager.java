package gui;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RobotsManager {
    private final List<RobotModel> robots = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    public void addRobot(RobotModel robot) {
        robots.add(robot);
    }

    public void setTargetForAll(int x, int y) {
        for (RobotModel robot : robots) {
            robot.setTarget(x, y);
        }
    }

    public void updateAll(double duration) {
        for (RobotModel robot : robots) {
            executor.submit(() -> robot.update(duration));
        }
    }

    public List<RobotModel> getRobots() {
        return robots;
    }
}
