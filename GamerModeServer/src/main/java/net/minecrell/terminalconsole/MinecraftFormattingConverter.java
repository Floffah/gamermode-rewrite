package net.minecrell.terminalconsole;

import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.*;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.jetbrains.annotations.Nullable;

/**
 * A fork of Minecrell's <a href="https://github.com/Minecrell/TerminalConsoleAppender/blob/master/src/main/java/net/minecrell/terminalconsole/MinecraftFormattingConverter.java">MinecraftFormattingConverter</a>.
 */
@Plugin(
    name = "gamermodeMinecraftFormatting",
    category = PatternConverter.CATEGORY
)
@ConverterKeys({ "gamermodeMinecraftFormatting" })
@PerformanceSensitive("allocation")
public final class MinecraftFormattingConverter
    extends LogEventPatternConverter {

    /**
     * System property that allows disabling the replacement of Minecraft
     * formatting codes entirely, keeping them in the console output. For
     * some applications they might be easier and more accurate for parsing
     * in applications like certain control panels.
     *
     * <p>If this system property is not set, or set to any value except
     * {@code true}, all Minecraft formatting codes will be replaced
     * or stripped from the console output.</p>
     */
    public static final String KEEP_FORMATTING_PROPERTY =
        TerminalConsoleAppender.PROPERTY_PREFIX + ".keepMinecraftFormatting";

    private static final boolean KEEP_FORMATTING = PropertiesUtil
        .getProperties()
        .getBooleanProperty(KEEP_FORMATTING_PROPERTY);

    static final String ANSI_RESET = "\u001B[m";

    private static final char COLOR_CHAR = '§';
    private static final String LOOKUP = "0123456789abcdefklmnor";

    private static final String[] ansiCodes = new String[] {
        "\u001B[0;30m", // Black §0
        "\u001B[0;34m", // Dark Blue §1
        "\u001B[0;32m", // Dark Green §2
        "\u001B[0;36m", // Dark Aqua §3
        "\u001B[0;31m", // Dark Red §4
        "\u001B[0;35m", // Dark Purple §5
        "\u001B[0;33m", // Gold §6
        "\u001B[0;37m", // Gray §7
        "\u001B[0;30;1m", // Dark Gray §8
        "\u001B[0;34;1m", // Blue §9
        "\u001B[0;32;1m", // Green §a
        "\u001B[0;36;1m", // Aqua §b
        "\u001B[0;31;1m", // Red §c
        "\u001B[0;35;1m", // Light Purple §d
        "\u001B[0;33;1m", // Yellow §e
        "\u001B[0;37;1m", // White §f
        "\u001B[5m", // Obfuscated §k
        "\u001B[21m", // Bold §l
        "\u001B[9m", // Strikethrough §m
        "\u001B[4m", // Underline §n
        "\u001B[3m", // Italic §o
        ANSI_RESET, // Reset §r
    };

    private final boolean ansi;
    private final List<PatternFormatter> formatters;

    /**
     * Construct the converter.
     *
     * @param formatters The pattern formatters to generate the text to manipulate
     * @param strip If true, the converter will strip all formatting codes
     */
    protected MinecraftFormattingConverter(
        List<PatternFormatter> formatters,
        boolean strip
    ) {
        super("minecraftFormatting", null);
        this.formatters = formatters;
        this.ansi = !strip;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        int start = toAppendTo.length();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0, size = formatters.size(); i < size; i++) {
            formatters.get(i).format(event, toAppendTo);
        }

        if (KEEP_FORMATTING || toAppendTo.length() == start) {
            // Skip replacement if disabled or if the content is empty
            return;
        }

        String content = toAppendTo.substring(start);
        format(
            content,
            toAppendTo,
            start,
            ansi && TerminalConsoleAppender.isAnsiSupported()
        );
    }

    static void format(
        String s,
        StringBuilder result,
        int start,
        boolean ansi
    ) {
        int next = s.indexOf(COLOR_CHAR);
        int last = s.length() - 1;
        if (next == -1 || next == last) {
            return;
        }

        result.setLength(start + next);

        int pos = next;
        do {
            int format = LOOKUP.indexOf(
                Character.toLowerCase(s.charAt(next + 1))
            );
            if (format != -1) {
                if (pos != next) {
                    result.append(s, pos, next);
                }
                if (ansi) {
                    result.append(ansiCodes[format]);
                }
                pos = next += 2;
            } else {
                next++;
            }

            next = s.indexOf(COLOR_CHAR, next);
        } while (next != -1 && next < last);

        result.append(s, pos, s.length());
        if (ansi) {
            result.append(ANSI_RESET);
        }
    }

    /**
     * Gets a new instance of the {@link MinecraftFormattingConverter} with the
     * specified options.
     *
     * @param config The current configuration
     * @param options The pattern options
     * @return The new instance
     *
     * @see MinecraftFormattingConverter
     */
    public static @Nullable MinecraftFormattingConverter newInstance(
        Configuration config,
        String[] options
    ) {
        if (options.length < 1 || options.length > 2) {
            LOGGER.error(
                "Incorrect number of options on minecraftFormatting. Expected at least 1, max 2 received " +
                options.length
            );
            return null;
        }
        if (options[0] == null) {
            LOGGER.error("No pattern supplied on minecraftFormatting");
            return null;
        }

        PatternParser parser = PatternLayout.createPatternParser(config);
        List<PatternFormatter> formatters = parser.parse(options[0]);
        boolean strip = options.length > 1 && "strip".equals(options[1]);
        return new MinecraftFormattingConverter(formatters, strip);
    }
}
