package dev.floffah.gamermode.utils;

import dev.floffah.gamermode.utils.window.UtilWindow;

import java.io.IOException;
import java.util.List;

public class GamerModeUtils {
    public static void main(String[] args) throws IOException {
        UtilWindow window = new UtilWindow(List.of(args));
        window.start();
    }
}