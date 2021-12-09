package dev.floffah.gamermode.visual.gui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.jetbrains.annotations.Nullable;

// inspired by baeldung & paper
@Plugin(
    name = "GuiAppender",
    category = Core.CATEGORY_NAME,
    elementType = Appender.ELEMENT_TYPE,
    printObject = true
)
public class GuiAppender extends AbstractAppender {

    public static String DefaultQueue = "GuiAppender";
    public static Map<String, BlockingQueue<String>> Queues = new HashMap<>();
    public static ReadWriteLock Lock = new ReentrantReadWriteLock();

    private final BlockingQueue<String> appendQueue;

    protected GuiAppender(
        String name,
        Filter filter,
        Layout<? extends Serializable> layout,
        BlockingQueue<String> appendQueue
    ) {
        super(name, filter, layout, false, Property.EMPTY_ARRAY);
        this.appendQueue = appendQueue;
    }

    @PluginFactory
    public static GuiAppender createAppender(
        @PluginAttribute("name") String name,
        @PluginAttribute("queue") String queue,
        @PluginElement("Filters") Filter filter,
        @PluginElement("Layout") Layout<? extends Serializable> layout
    ) {
        if (queue == null) queue = name;

        GuiAppender.Lock.writeLock().lock();
        BlockingQueue<String> logQueue = GuiAppender.Queues.get(queue);
        if (logQueue == null) {
            logQueue = new LinkedBlockingDeque<>();
            GuiAppender.Queues.put(queue, logQueue);
        }
        GuiAppender.Lock.writeLock().unlock();

        if (layout == null) layout = PatternLayout.createDefaultLayout();

        return new GuiAppender(name, filter, layout, logQueue);
    }

    public static @Nullable String next(@Nullable String target) {
        GuiAppender.Lock.readLock().lock();
        BlockingQueue<String> queue = GuiAppender.Queues.get(
            target == null ? "GuiAppender" : target
        );
        GuiAppender.Lock.readLock().unlock();

        try {
            return queue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override
    public void append(LogEvent event) {
        appendQueue.add(this.getLayout().toSerializable(event).toString());
    }
}
