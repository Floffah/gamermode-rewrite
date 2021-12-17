package dev.floffah.gamermode.command;

import dev.floffah.gamermode.datatype.Identifier;
import net.kyori.adventure.identity.Identified;
import org.jetbrains.annotations.Nullable;

public interface CanRegister extends Identified {
    /**
     * Namespace to give to the identifier of commands that can be registered.
     *
     * @return The namespace.
     */
    default @Nullable String getNamespace() {
        return null;
    }

    /**
     * Build an identifier for a command registered by this object.
     *
     * @param command The command.
     * @return The identifier.
     */
    default Identifier buildIdentifier(Command command) {
        return Identifier.from(this.getNamespace(), command.getName());
    }
}