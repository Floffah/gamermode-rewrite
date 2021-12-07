package dev.floffah.gamermode.visual.console;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import lombok.Getter;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

public class GamerModeConsole extends SimpleTerminalConsole {

    @Getter
    private final ConsoleRenderer renderer;

    public GamerModeConsole(ConsoleRenderer renderer) {
        super();
        this.renderer = renderer;
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder builder) {
        builder
            .appName("GamerMode")
            .variable(
                LineReader.HISTORY_FILE,
                Paths.get(
                    this.renderer.getConsole()
                        .getServer()
                        .getCache()
                        .cachedir.getPath(),
                    "console.history"
                )
            )
            .option(LineReader.Option.COMPLETE_IN_WORD, true);
        return super.buildReader(builder);
    }

    @Override
    protected boolean isRunning() {
        return !this.renderer.getConsole().getServer().isStopping();
    }

    @Override
    protected void runCommand(String command) {
        // temporary
        if (Objects.equals(command, "stop")) try {
            this.renderer.getConsole().getServer().shutdown();
        } catch (IOException e) {
            this.renderer.getConsole()
                .getRenderer()
                .getConsole()
                .getServer()
                .getLogger()
                .fatal("Could not stop server", e);
            System.exit(1);
        }
    }

    @Override
    protected void shutdown() {
        try {
            this.renderer.getConsole().getServer().shutdown();
        } catch (IOException e) {
            this.renderer.getConsole()
                .getRenderer()
                .getConsole()
                .getServer()
                .getLogger()
                .fatal("Could not stop server", e);
            System.exit(1);
        }
    }
}
