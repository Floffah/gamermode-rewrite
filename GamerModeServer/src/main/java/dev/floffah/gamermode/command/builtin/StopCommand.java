package dev.floffah.gamermode.command.builtin;

import dev.floffah.gamermode.command.Command;
import dev.floffah.gamermode.command.CommandInfo;
import dev.floffah.gamermode.command.CommandSender;
import dev.floffah.gamermode.console.ConsoleCommandSender;
import java.io.IOException;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@CommandInfo(name = "stop", description = "Stop the server")
public class StopCommand extends Command {

    @Override
    public boolean onExecute(CommandSender sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            try {
                this.getServer().getConsole().getServer().shutdown();
            } catch (IOException e) {
                this.getServer()
                    .getConsole()
                    .getRenderer()
                    .getConsole()
                    .getServer()
                    .getLogger()
                    .fatal("Could not stop server", e);
                System.exit(1);
            }
        } else {
            sender.sendMessage(
                Component
                    .text("Not permitted to stop the server")
                    .color(NamedTextColor.RED)
            );
        }
        return true;
    }
}
