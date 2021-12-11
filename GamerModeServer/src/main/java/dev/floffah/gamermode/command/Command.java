package dev.floffah.gamermode.command;

import dev.floffah.gamermode.datatype.Identifier;
import java.util.List;

import dev.floffah.gamermode.server.Server;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Command {

    @Getter
    protected Server server;

    @Getter
    protected String name;

    @Getter
    protected String description;

    @Getter
    protected List<String> aliases;

    @Getter
    protected Identifier identifier;

    @Getter
    protected boolean system;

    protected boolean canBeSystem = false;

    public Command() {}

    /**
     * The executor for a command.
     *
     * @param sender The sender of the command.
     * @param args   The arguments of the command.
     * @return Whether the command was executed successfully. An error thrown assumes false here.
     */
    public boolean onExecute(CommandSender sender, String[] args) {
        sender.sendMessage(
            Component.text("Not implemented").color(NamedTextColor.RED)
        );
        return false;
    }
}
