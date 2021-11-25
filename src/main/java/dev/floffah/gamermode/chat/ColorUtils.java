package dev.floffah.gamermode.chat;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ColorUtils {
    /**
     * Translates a string into an Adventure TextComponent
     * @param message The message to translate
     * @return The translated message
     */
    public static TextComponent translate(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }
}
