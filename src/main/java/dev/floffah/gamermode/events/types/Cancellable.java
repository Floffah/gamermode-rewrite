package dev.floffah.gamermode.events.types;

import dev.floffah.gamermode.events.Event;
import lombok.Getter;
import lombok.Setter;

/**
 * An event class that is supposed to be extended upon by cancellable events
 */
public class Cancellable extends Event implements ICancellable {

    @Getter
    @Setter
    boolean cancelled = false;
}
