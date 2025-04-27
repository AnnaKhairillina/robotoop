package gui.state;

import java.util.Map;
import java.util.HashMap;
import javax.swing.JFrame;

public abstract class StatefulWindow extends JFrame implements Stateful {
    @Override
    public Map<String, String> getWindowState() {
        Map<String, String> state = new HashMap<>();
        state.put("x", String.valueOf(getX()));
        state.put("y", String.valueOf(getY()));
        state.put("width", String.valueOf(getWidth()));
        state.put("height", String.valueOf(getHeight()));
        state.put("isMaximized", String.valueOf((getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH));
        return state;
    }

    @Override
    public void restoreState(Map<String, String> state) {
        if (state == null || state.isEmpty()) return;

        setBounds(
                Integer.parseInt(state.getOrDefault("x", "50")),
                Integer.parseInt(state.getOrDefault("y", "50")),
                Integer.parseInt(state.getOrDefault("width", "800")),
                Integer.parseInt(state.getOrDefault("height", "600"))
        );

        if (Boolean.parseBoolean(state.getOrDefault("isMaximized", "false"))) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }
}