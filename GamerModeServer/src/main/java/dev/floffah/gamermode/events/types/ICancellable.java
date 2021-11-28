package dev.floffah.gamermode.events.types;

public interface ICancellable {
    /**
     * Get whether or not the event has been cancelled
     *
     * @return Whether or not the event was cancelled
     */
    public boolean isCancelled();

    /**
     * Set whether or not the event has been cancelled
     *
     * @param cancelled New cancelled value
     */
    public void setCancelled(boolean cancelled);
}
