package gui.state;

import java.util.Map;

public interface Stateful {
    Map<String, String> getWindowState();
    void restoreState(Map<String, String> state);
}
