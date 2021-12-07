package dev.floffah.gamermode.visual.console;

import dev.floffah.gamermode.console.Console;
import java.io.IOException;
import lombok.Getter;

public class ConsoleRenderer {

    @Getter
    private final Console console;

    private GamerModeConsole terminal;

    public ConsoleRenderer(Console console) {
        this.console = console;
    }

    public void startOutput() throws IOException {
        this.terminal = new GamerModeConsole(this);
        this.console.getServer()
            .getDaemonPool()
            .execute(() -> {
                this.terminal.start();
                Thread.currentThread().interrupt();
            });
    }

    public void stop() throws IOException {}
}
