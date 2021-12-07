package dev.floffah.gamermode.console;

import dev.floffah.gamermode.server.Server;
import dev.floffah.gamermode.visual.console.ConsoleRenderer;
import lombok.Getter;

import java.io.IOException;

public class Console {

    @Getter
    private final Server server;

    /**
     * Rendering manager for the console
     * -- GETTER --
     * Get the console renderer
     *
     * @return The console renderer
     */
    @Getter
    private final ConsoleRenderer renderer;

    public Console(Server server) {
        this.server = server;
        this.renderer = new ConsoleRenderer(this);
    }

    public void close() throws IOException {
        this.renderer.stop();
    }
}
