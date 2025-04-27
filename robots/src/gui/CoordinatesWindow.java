package gui;

import gui.state.StatefulInternalFrame;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public class CoordinatesWindow extends StatefulInternalFrame implements Observer {
    private final JLabel coordinatesLabel = new JLabel();

    public CoordinatesWindow(RobotModel model) {
        super("Координаты робота", true, true, true, true); // теперь вызываем конструктор StatefulInternalFrame
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
