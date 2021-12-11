package dev.floffah.gamermode.console;

import dev.floffah.gamermode.server.Server;
import dev.floffah.gamermode.visual.console.ConsoleRenderer;
import java.io.IOException;
import lombok.Getter;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

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

    @Getter
    private final ConsoleCommandSender commandExecutor;

    public static ComponentFlattener flattener = ComponentFlattener.basic();
    public static LegacyComponentSerializer ComponentSerializer = LegacyComponentSerializer
        .builder()
        .flattener(flattener)
        .hexColors()
        .useUnusualXRepeatedCharacterHexFormat()
        .build();

    public Console(Server server) {
        this.server = server;
        this.renderer = new ConsoleRenderer(this);
        this.commandExecutor = new ConsoleCommandSender(this);
    }

    public void close() throws IOException {
        this.renderer.stop();
    }
}
