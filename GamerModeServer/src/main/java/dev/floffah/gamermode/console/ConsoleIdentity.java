package dev.floffah.gamermode.console;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class ConsoleIdentity implements Identity {
    ConsoleCommandSender executor;

    /**
     * Console's identifiable/examinable name
     * -- GETTER --
     * Get the name of the console
     *
     * @return The name of the console
     * -- SETTER --
     * Set the name of the console
     *
     * @param name The name of the console
     */
    @Getter
    @Setter
    private String name = "CONSOLE";

    public ConsoleIdentity(ConsoleCommandSender executor) {
        this.executor = executor;
    }

    @Override
    public java.util.@NotNull UUID uuid() {
        return this.executor.getConsole().getServer().getUniqueId();
    }

    @Override
    public @NotNull String examinableName() {
        return this.name;
    }
}
