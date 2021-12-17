package dev.floffah.gamermode.command;

import dev.floffah.gamermode.server.Server;
import lombok.Getter;

public class CommandManager {

    @Getter
    private final Server server;

    @Getter
    private CommandStore store;

    public CommandManager(Server server) {
        this.server = server;
    }

    public void initialise() {
        this.store = new CommandStore(this);
    }
}
