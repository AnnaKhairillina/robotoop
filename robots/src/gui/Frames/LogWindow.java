package gui.Frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.util.Map;
import javax.swing.JPanel;

import gui.state.StatefulInternalFrame;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends StatefulInternalFrame implements LogChangeListener
{
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;

    public LogWindow(LogWindowSource logSource)
    {
        super("Протокол работы", true, true, true, true);
        this.m_logSource = logSource;
        this.m_logSource.registerListener(this);

        this.m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);

        pack();
        updateLogContent();

        setSize(300, 800);
        setLocation(10, 10);
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public Map<String, String> getWindowState() {
        return super.getWindowState();
    }

    @Override
    public void restoreState(Map<String, String> state) {
        super.restoreState(state);
    }

    @Override
    public void dispose() {
        // отписка от логов при закрытии окна
        m_logSource.unregisterListener(this);
        super.dispose();
    }
}
