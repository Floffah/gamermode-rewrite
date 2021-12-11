package dev.floffah.gamermode.console;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class ConsoleAudience implements Audience {

    @Getter
    private final Console console;

    public ConsoleAudience(Console console) {
        this.console = console;
    }

    @Override
    public void sendMessage(
        final @NotNull Identity source,
        final @NotNull Component message,
        final @NotNull MessageType type
    ) {
        this.console.getServer()
            .getLogger()
            .info(Console.ComponentSerializer.serialize(message));
    }
}
