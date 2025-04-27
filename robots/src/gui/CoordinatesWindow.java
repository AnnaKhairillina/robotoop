package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CoordinatesWindow extends JInternalFrame implements Observer {
    private final JLabel coordinatesLabel = new JLabel();

    public CoordinatesWindow(RobotModel model) {
        super("Координаты робота", true, true, true, true);
        model.addObserver(this);

        coordinatesLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(coordinatesLabel);
        setSize(200, 100);
        update(model, null);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof RobotModel model) {
            coordinatesLabel.setText(String.format("X: %.2f, Y: %.2f", model.getX(), model.getY()));
        }
    }
}