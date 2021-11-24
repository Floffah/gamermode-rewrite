package dev.floffah.gamermode.chat;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ColorUtils {
    public static TextComponent translate(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }
}
