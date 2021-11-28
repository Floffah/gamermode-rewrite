package dev.floffah.gamermode.utils.fonts;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import lombok.Getter;

public class FontProvider {
    public static FontProvider INSTANCE;

    @Getter
    private Font monoFont;

    public FontProvider() {
        FontProvider.INSTANCE = this;

        try {
            InputStream monoFont = getClass()
                .getClassLoader()
                .getResourceAsStream("JetBrainsMono-Regular.ttf");

            if (monoFont == null) {
                this.monoFont = Font.getFont(Font.MONOSPACED);
            } else {
                this.monoFont = Font.createFont(Font.TRUETYPE_FONT, monoFont);
            }
        } catch (IOException | FontFormatException e) {
            this.monoFont = Font.getFont(Font.MONOSPACED);
        }
    }
}
