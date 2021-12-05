package dev.floffah.gamermode.entity.player;

import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;

public class PlayerIdentity implements Identity {
    private final Player player;

    public PlayerIdentity(Player player) {
        this.player = player;
    }

    @Override
    public java.util.@NotNull UUID uuid() {
        return this.player.getUniqueId();
    }

    @Override
    public @NotNull String examinableName() {
        return this.player.getUsername();
    }
}
