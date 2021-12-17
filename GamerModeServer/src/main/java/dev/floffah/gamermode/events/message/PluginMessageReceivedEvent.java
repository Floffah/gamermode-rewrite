package dev.floffah.gamermode.events.message;

import dev.floffah.gamermode.datatype.Identifier;
import dev.floffah.gamermode.events.Event;
import dev.floffah.gamermode.server.packet.play.message.PluginMessageClientBound;
import dev.floffah.gamermode.server.packet.play.message.PluginMessageServerBound;

/**
 * Event fired when a plugin message is received
 */
public class PluginMessageReceivedEvent extends Event {

    PluginMessageServerBound packet;

    /**
     * Construct a {@link PluginMessageReceivedEvent}
     * @param packet The {@link PluginMessageClientBound} packet
     */
    public PluginMessageReceivedEvent(PluginMessageServerBound packet) {
        this.packet = packet;
    }

    /**
     * Get the bytes read from the plugin message
     * @return The bytes read from the plugin message
     */
    public byte[] getBytes() {
        return packet.bytesread;
    }

    /**
     * Get the plugin message channel
     * @return The plugin message channel
     */
    public Identifier getChannel() {
        return packet.channel;
    }
}
