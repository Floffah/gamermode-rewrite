package dev.floffah.gamermode.util.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

public class DaemonThreadFactory implements ThreadFactory {

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public DaemonThreadFactory() {
        @SuppressWarnings("removal")
        SecurityManager s = System.getSecurityManager();
        group =
            (s != null)
                ? s.getThreadGroup()
                : Thread.currentThread().getThreadGroup();
        this.namePrefix = "daemon-thread-";
    }

    public Thread newThread(@NotNull Runnable r) {
        Thread t = new Thread(
            group,
            r,
            namePrefix + threadNumber.getAndIncrement(),
            0
        );
        if (!t.isDaemon()) t.setDaemon(true);
        if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(
            Thread.NORM_PRIORITY
        );
        return t;
    }
}
