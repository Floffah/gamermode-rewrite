package dev.floffah.gamermode.events;

/**
 * An abstract class for storing listener executors
 */
public abstract class Executor {
    /**
     * Abstract method for executing an event
     * @param event The event to execute
     */
    public abstract void execute(Event event);
}
