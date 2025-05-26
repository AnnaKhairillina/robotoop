package gui.log;

import log.LogChangeListener;
import log.LogEntry;
import log.LogLevel;
import log.LogWindowSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LogWindowSourceTest {
    private LogWindowSource logSource;
    private static final int QUEUE_LENGTH = 5;

    @BeforeEach
    void setUp() {
        logSource = new LogWindowSource(QUEUE_LENGTH);
    }

    @Test
    void testAppendWithinCapacity() {
        logSource.append(LogLevel.Debug, "Message 1");
        logSource.append(LogLevel.Error, "Message 2");

        assertEquals(2, logSource.size());
        List<LogEntry> entries = getEntriesList(logSource.all());
        assertEquals("Message 1", entries.get(0).getMessage());
        assertEquals("Message 2", entries.get(1).getMessage());
    }

    @Test
    void testAppendExceedsCapacity() {
        for (int i = 0; i < QUEUE_LENGTH + 2; i++) {
            logSource.append(LogLevel.Info, "Message " + (i + 1));
        }

        assertEquals(QUEUE_LENGTH, logSource.size());
        List<LogEntry> entries = getEntriesList(logSource.all());
        assertEquals("Message 3", entries.get(0).getMessage());
        assertEquals("Message 7", entries.get(QUEUE_LENGTH - 1).getMessage());

        /*
        logSouse = { 6 , 7 , 3 , 4  5}
        entries  = { 3 , 4 , 5 , 6 , 7 }
         */
    }

    @Test
    void testRange() {
        for (int i = 0; i < QUEUE_LENGTH; i++) {
            logSource.append(LogLevel.Warning, "Message " + (i + 1));
        }

        List<LogEntry> entries = getEntriesList(logSource.range(1, 3));
        assertEquals(3, entries.size());
        assertEquals("Message 2", entries.get(0).getMessage());
        assertEquals("Message 4", entries.get(2).getMessage());

        /*
        logSouse = { 1 , 2 , 3 , 4  5}
        entries  = { 2 , 3 , 4}
         */
    }

    @Test
    void testRangeOutOfBounds() {
        logSource.append(LogLevel.Debug, "Message 1");
        assertTrue(getEntriesList(logSource.range(5, 2)).isEmpty());
        assertTrue(getEntriesList(logSource.range(-1, 2)).isEmpty());
    }

    @Test
    void testIterator() {
        logSource.append(LogLevel.Debug, "Message 1");
        logSource.append(LogLevel.Error, "Message 2");

        var iterator = logSource.all().iterator();
        assertTrue(iterator.hasNext());
        assertEquals("Message 1", iterator.next().getMessage());
        assertEquals("Message 2", iterator.next().getMessage());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testConcurrentAppend() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.execute(() -> {
                logSource.append(LogLevel.Info, "Message " + index);
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(QUEUE_LENGTH, logSource.size());
    }

    @Test
    void testListenerNotification() {
        LogChangeListener listener = mock(LogChangeListener.class);
        logSource.registerListener(listener);

        logSource.append(LogLevel.Debug, "Test message");
        verify(listener, timeout(100)).onLogChanged();

        logSource.unregisterListener(listener);
        logSource.append(LogLevel.Error, "Another message");
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testConcurrentListenerAccess() throws InterruptedException {
        LogChangeListener listener = mock(LogChangeListener.class);
        logSource.registerListener(listener);

        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                logSource.append(LogLevel.Info, "Concurrent message");
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        verify(listener, times(threadCount)).onLogChanged();
    }

    private List<LogEntry> getEntriesList(Iterable<LogEntry> iterable) {
        List<LogEntry> entries = new ArrayList<>();
        for (LogEntry entry : iterable) {
            entries.add(entry);
        }
        return entries;
    }
}