package gui;

import gui.state.StatefulInternalFrame;

import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.JPanel;

public class GameWindow extends StatefulInternalFrame
{
    private final GameVisualizer m_visualizer;

    public GameWindow()
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);

        // Устанавливаем начальные размеры
        setSize(400, 400);
        pack();
    }

    @Override
    public Map<String, String> getWindowState() {
        // Используем реализацию из StatefulFrame
        Map<String, String> state = super.getWindowState();

        // Можно добавить специфичные для GameWindow параметры
        // Например:
        // state.put("gameLevel", currentLevel);
        return state;
    }

    @Override
    public void restoreState(Map<String, String> state) {
        // Восстанавливаем базовое состояние (позиция, размеры)
        super.restoreState(state);

        // Можно восстановить специфичные для GameWindow параметры
        // Например:
        // this.currentLevel = state.getOrDefault("gameLevel", "1");
    }
}