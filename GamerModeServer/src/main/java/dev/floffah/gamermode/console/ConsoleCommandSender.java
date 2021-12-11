package dev.floffah.gamermode.console;

import dev.floffah.gamermode.command.CanRegister;
import dev.floffah.gamermode.command.Command;
import dev.floffah.gamermode.command.CommandSender;
import dev.floffah.gamermode.command.builtin.StopCommand;
import dev.floffah.gamermode.datatype.Identifier;
import dev.floffah.gamermode.events.EventListener;
import dev.floffah.gamermode.events.Listener;
import dev.floffah.gamermode.events.state.ServerLoadEvent;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConsoleCommandSender extends ConsoleAudience
    implements CommandSender, CanRegister, Listener {


    private final Identity identity = Identity.nil();

    /**
     * Console's identifiable/examinable name
     * -- GETTER --
     * Get the name of the console
     *
     * @return The name of the console
     * -- SETTER --
     * Set the name of the console
     * @param name The name of the console
     */
    @Getter
    @Setter
    private String name = "CONSOLE";

    public ConsoleCommandSender(Console console) {
        super(console);
//        this.console = console;
        //        this.identity = new ConsoleIdentity(this);
        this.getConsole().getServer().getEvents().registerListeners(this);
    }

    public @NotNull Identity identity() {
        return this.identity;
    }

    @Override
    public String senderName() {
        return this.name;
    }

    @Override
    public @Nullable String getNamespace() {
        return "gamermode";
    }

    @Override
    public Identifier buildIdentifier(Command command) {
        if (command.isSystem()) return Identifier.from(
            "minecraft",
            command.getName()
        );
        return CanRegister.super.buildIdentifier(command);
    }

    @EventListener
    public void onServerLoad(ServerLoadEvent e) {
        this.getConsole()
            .getServer()
            .getCommands()
            .getStore()
            .registerCommands(this, new StopCommand());
    }
}
