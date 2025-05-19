package tests;

import gui.RobotModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.AcceleratingStrategy;
import strategy.StraightToTargetStrategy;
import strategy.ZigZagStrategy;
import static org.junit.jupiter.api.Assertions.*;

public class RobotModelTest {
    RobotModel robot;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testStraightToTargetStrategyMovesCloser() {
        robot = new RobotModel(new StraightToTargetStrategy(), 0, 0);
        robot.setTarget(100, 0);
        double oldX = robot.getX();
        robot.update(1.0);
        double newX = robot.getX();
        assertTrue(newX > oldX && newX <= 100, "Робот должен двигаться к цели по X");
    }

    @Test
    void testAcceleratingStrategySpeedsUp() {
        robot = new RobotModel(new AcceleratingStrategy(), 0, 0);
        robot.setTarget(100, 0);

        double x0 = robot.getX();

        robot.update(1.0);
        double x1 = robot.getX();
        double firstStep = x1 - x0;

        robot.update(1.0);
        double x2 = robot.getX();
        double secondStep = x2 - x1;

        assertTrue(secondStep > firstStep, "Робот должен ускоряться");
    }


    @Test
    void testZigZagStrategyAltersPath() {
        robot = new RobotModel(new ZigZagStrategy(), 0, 0);
        robot.setTarget(100, 100);

        double firstY, secondY;

        robot.update(1.0);
        firstY = robot.getY();

        robot.update(1.0);
        secondY = robot.getY();

        assertNotEquals(firstY, secondY, "Y-координата должна меняться зигзагом");
    }
}
