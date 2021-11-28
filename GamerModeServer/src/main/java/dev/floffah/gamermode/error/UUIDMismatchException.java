package dev.floffah.gamermode.error;

public class UUIDMismatchException extends Exception {

    public UUIDMismatchException() {
        super(
            "Player's saved UUID does not match the one claimed by the client and or Mojang api."
        );
    }
}
