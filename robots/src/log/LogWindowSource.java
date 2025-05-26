package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LogWindowSource {
    private final int queueLength;
    private final LogEntry[] buffer;
    private final AtomicInteger startIndex = new AtomicInteger(0);
    private final AtomicInteger count = new AtomicInteger(0);

    private final List<LogChangeListener> listeners = new ArrayList<>();
    private volatile LogChangeListener[] activeListeners;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public LogWindowSource(int queueLength) {
        this.queueLength = queueLength;
        this.buffer = new LogEntry[queueLength];
    }

    public void registerListener(LogChangeListener listener) {
        lock.writeLock().lock();
        try {
            listeners.add(listener);
            activeListeners = null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        lock.writeLock().lock();
        try {
            listeners.remove(listener);
            activeListeners = null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);

        lock.writeLock().lock();
        try {
            int index = (startIndex.get() + count.get()) % queueLength;
            buffer[index] = entry;

            if (count.get() < queueLength) {
                count.incrementAndGet();
            } else {
                startIndex.incrementAndGet();
                startIndex.compareAndSet(queueLength, 0);
            }
        } finally {
            lock.writeLock().unlock();
        }

        notifyListeners();
    }

    private void notifyListeners() {
        LogChangeListener[] activeListeners = this.activeListeners;
        if (activeListeners == null) {
            synchronized (this) {
                activeListeners = this.activeListeners;
                if (activeListeners == null) {
                    activeListeners = listeners.toArray(new LogChangeListener[0]);
                    this.activeListeners = activeListeners;
                }
            }
        }

        for (LogChangeListener listener : activeListeners) {
            listener.onLogChanged();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return count.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= size()) {
            return Collections.emptyList();
        }

        return new Iterable<LogEntry>() {
            @Override
            public Iterator<LogEntry> iterator() {
                return new LogEntryIterator(startFrom, Math.min(startFrom + count, size()));
            }
        };
    }

    public Iterable<LogEntry> all() {
        return range(0, size());
    }

    private class LogEntryIterator implements Iterator<LogEntry> {
        private final int endIndex;
        private int currentIndex;

        public LogEntryIterator(int startFrom, int endIndex) {
            this.currentIndex = startFrom;
            this.endIndex = endIndex;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < endIndex;
        }
        @Override
        public LogEntry next() {
            lock.readLock().lock();
            try {
                if (!hasNext()) {
                    throw new IllegalStateException("No more elements");
                }
                int index = (startIndex.get() + currentIndex) % queueLength;
                currentIndex++;
                return buffer[index];
            } finally {
                lock.readLock().unlock();
            }
        }
    }
}