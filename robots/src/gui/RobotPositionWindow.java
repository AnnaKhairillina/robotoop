package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class RobotPositionWindow extends JInternalFrame implements Observer {
    private final JLabel positionLabel;

    public RobotPositionWindow(RobotModel model) {
        super("Координаты робота", true, true, true, true);
        positionLabel = new JLabel();
        positionLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        add(positionLabel);
        setSize(200, 100);
        model.addObserver(this);
        updatePosition(model);
    }

    private void updatePosition(RobotModel model) {
        positionLabel.setText(String.format("X: %.2f, Y: %.2f", model.getX(), model.getY()));
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof RobotModel) {
            SwingUtilities.invokeLater(() -> updatePosition((RobotModel) o));
        }
    }
}
