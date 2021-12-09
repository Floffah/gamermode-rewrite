package dev.floffah.gamermode.util.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

public class ServerThreadFactory implements ThreadFactory {

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public ServerThreadFactory(String type) {
        @SuppressWarnings("removal")
        SecurityManager s = System.getSecurityManager();
        group =
            (s != null)
                ? s.getThreadGroup()
                : Thread.currentThread().getThreadGroup();
        namePrefix = type + "-thread-";
    }

    public Thread newThread(@NotNull Runnable r) {
        Thread t = new Thread(
            group,
            r,
            namePrefix + threadNumber.getAndIncrement(),
            0
        );
        if (t.isDaemon()) t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(
            Thread.NORM_PRIORITY
        );
        return t;
    }
}
