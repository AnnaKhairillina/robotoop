package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogWindowSource
{
    private final int m_iQueueLength;

    private final List<LogEntry> m_messages;
    private final List<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength)
    {
        this.m_iQueueLength = iQueueLength;
        this.m_messages = new ArrayList<>(iQueueLength);
        this.m_listeners = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            if (!m_listeners.contains(listener)) {
                m_listeners.add(listener);
                m_activeListeners = null;
            }
        }
    }

    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            if (m_listeners.remove(listener)) {
                m_activeListeners = null;
            }
        }
    }

    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);

        synchronized (m_messages) {
            m_messages.add(entry);
            while (m_messages.size() > m_iQueueLength) {
                m_messages.remove(0);
            }
        }

        LogChangeListener[] activeListeners = m_activeListeners;
        if (activeListeners == null)
        {
            synchronized (m_listeners)
            {
                if (m_activeListeners == null)
                {
                    activeListeners = m_listeners.toArray(new LogChangeListener[0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged();
        }
    }

    public int size()
    {
        synchronized (m_messages) {
            return m_messages.size();
        }
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        synchronized (m_messages) {
            if (startFrom < 0 || startFrom >= m_messages.size())
            {
                return Collections.emptyList();
            }
            int indexTo = Math.min(startFrom + count, m_messages.size());
            return new ArrayList<>(m_messages.subList(startFrom, indexTo));
        }
    }

    public Iterable<LogEntry> all()
    {
        synchronized (m_messages) {
            return new ArrayList<>(m_messages);
        }
    }
}
