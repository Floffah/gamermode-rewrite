package dev.floffah.gamermode.command;

import dev.floffah.gamermode.datatype.Identifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandStore {

    @Getter
    private final CommandManager manager;

    /**
     * Commands known to the server keyed by their identifiers (which may or may not have a namespace)
     */
    @Getter
    private final Map<Identifier, Object> knownCommands = new HashMap<>();

    /**
     * All command aliases known to the server where the value is the identifier of the aliased command.
     */
    @Getter
    private final Map<Identifier, Identifier> knownAliases = new HashMap<>();

    /**
     * All identifier namespaces of known commands that are known to the server.
     */
    @Getter
    private final List<String> knownNamespaces = new ArrayList<>();

    public CommandStore(CommandManager manager) {
        this.manager = manager;
    }

    public void registerCommands(
        CanRegister registeredBy,
        Command... commands
    ) {
        for (Command command : commands) {
            this.registerCommand(registeredBy, command);
        }
    }

    public void registerCommand(CanRegister registeredBy, Command command) {
        if (
            command.canBeSystem &&
            registeredBy ==
            this.manager.getServer().getConsole().getCommandExecutor()
        ) command.system = true;

        if (command.server == null) command.server =
            this.getManager().getServer();

        CommandInfo info = command.getClass().getAnnotation(CommandInfo.class);
        command.name = info.name();
        command.description = info.description();
        command.aliases = List.of(info.aliases());

        command.identifier = registeredBy.buildIdentifier(command);

        String namespace = command.identifier.getNamespace();

        for (String alias : command.aliases) {
            if (
                !this.knownAliases.containsKey(
                        Identifier.from(namespace, alias)
                    )
            ) {
                this.knownAliases.put(
                        Identifier.from(namespace, alias),
                        command.identifier
                    );
            }
        }
        this.knownAliases.put(
                Identifier.from(command.getName()),
                command.identifier
            );

        this.knownCommands.put(command.identifier, command);
        if (!this.knownNamespaces.contains(namespace)) this.knownNamespaces.add(
                namespace
            );
    }

    public void execute(CommandSender sender, String command) {
        String[] args = command.split(" ");
        Identifier identifier = Identifier.from(args[0]);
        args = List.of(args).subList(1, args.length).toArray(new String[0]);

        if (this.knownAliases.containsKey(identifier)) {
            identifier = this.knownAliases.get(identifier);
        }

        if (this.knownCommands.containsKey(identifier)) {
            Command cmd = (Command) this.knownCommands.get(identifier);
            this.execute(sender, cmd, args);
        } else {
            sender.sendMessage(
                Component
                    .text("No such command `" + identifier.toString() + "`")
                    .color(NamedTextColor.RED)
            );
        }
    }

    public void execute(CommandSender sender, Command command, String[] args) {
        if (args.length > 0 && args[0].equals(command.getName())) args =
            List.of(args).subList(1, args.length).toArray(new String[0]);
        try {
            command.onExecute(sender, args);
        } catch (Exception e) {
            sender.sendMessage(
                Component
                    .text("An error occurred while executing this command")
                    .color(NamedTextColor.RED)
            );
            this.getManager()
                .getServer()
                .getLogger()
                .error(
                    "An error occurred while executing command " +
                    command.getName(),
                    e
                );
        }
    }
}
