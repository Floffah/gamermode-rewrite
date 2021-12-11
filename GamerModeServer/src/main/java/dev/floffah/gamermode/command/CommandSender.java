package dev.floffah.gamermode.command;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;

/**
 * Interface that represents an executor of commands. E.g., player, console.
 */
public interface CommandSender extends Identified, Audience {
    public String senderName();
}
