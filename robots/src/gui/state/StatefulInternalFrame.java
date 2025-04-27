package gui.state;

import log.Logger;
import javax.swing.JInternalFrame;
import java.util.HashMap;
import java.util.Map;

public abstract class StatefulInternalFrame extends JInternalFrame implements Stateful {
    public StatefulInternalFrame(String title, boolean resizable, boolean closable,
                                 boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    @Override
    public Map<String, String> getWindowState() {
        Map<String, String> state = new HashMap<>();
        state.put("x", String.valueOf(getX()));
        state.put("y", String.valueOf(getY()));
        state.put("width", String.valueOf(getWidth()));
        state.put("height", String.valueOf(getHeight()));
        state.put("isIcon", String.valueOf(isIcon()));
        state.put("isMaximum", String.valueOf(isMaximum()));
        return state;
    }

    @Override
    public void restoreState(Map<String, String> state) {
        if (state == null || state.isEmpty()) return;

        setBounds(
                Integer.parseInt(state.getOrDefault("x", "10")),
                Integer.parseInt(state.getOrDefault("y", "10")),
                Integer.parseInt(state.getOrDefault("width", "300")),
                Integer.parseInt(state.getOrDefault("height", "300"))
        );

        try {
            if (Boolean.parseBoolean(state.get("isIcon"))) setIcon(true);
            if (Boolean.parseBoolean(state.get("isMaximum"))) setMaximum(true);
        } catch (Exception e) {
            Logger.error("Ошибка восстановления состояния окна: " + e.getMessage());
        }
    }
}