package dev.floffah.gamermode.events.types;

import dev.floffah.gamermode.events.Event;
import lombok.Getter;
import lombok.Setter;

/**
 * An event class that is supposed to be extended upon by cancellable events
 */
public class Cancellable extends Event {
    /**
     * If the event was cancelled
     * -- GETTER --
     * Get whether or not the event has been cancelled
     *
     * @return Whether or not the event was cancelled
     * -- SETTER --
     * Set whether or not the event has been cancelled
     * @param cancelled New cancelled value
     */
    @Getter
    @Setter
    boolean cancelled = false;
}
