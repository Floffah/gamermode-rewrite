package dev.floffah.gamermode.events.message;

import dev.floffah.gamermode.datatype.Identifier;
import dev.floffah.gamermode.events.Event;
import dev.floffah.gamermode.server.packet.play.message.PluginMessage;

/**
 * Event fired when a plugin message is received
 */
public class PluginMessageReceivedEvent extends Event {

    PluginMessage packet;

    /**
     * Construct a {@link PluginMessageReceivedEvent}
     * @param packet The {@link PluginMessage} packet
     */
    public PluginMessageReceivedEvent(PluginMessage packet) {
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
