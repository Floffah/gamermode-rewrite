package dev.floffah.gamermode.datatype;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

public class Identifier {

    /**
     * The identifier's namespace. Usually `minecraft`
     * -- GETTER --
     * Get the namespace of the identifier
     *
     * @return The namespace of the identifier
     * -- SETTER --
     * Set the namespace of the identifier
     * @param namespace The namespace of the identifier
     */
    @Getter
    @Setter
    @Nullable
    private String namespace;

    /**
     * The identifier's name
     * -- GETTER --
     * Get the name of the identifier
     *
     * @return The name of the identifier
     * -- SETTER --
     * Set the name of the identifier
     * @param name The name of the identifier
     */
    @Getter
    @Setter
    private String name;

    /**
     * Create an identifier from a namespace and a name.
     *
     * @param namespace The namespace of the identifier
     * @param name      The name of the identifier
     * @return The identifier
     */
    public static Identifier from(@Nullable String namespace, String name) {
        Identifier identifier = new Identifier();
        identifier.setNamespace(namespace);
        identifier.setName(name);
        return identifier;
    }

    /**
     * Create an identifier from a name.
     *
     * @param name The name of the identifier
     * @return The identifier
     */
    public static Identifier from(String name) {
        return Identifier.from(null, name);
    }

    /**
     * Parses a string into an identifier. Supports namespace:name or just name.
     *
     * @param parseable The string to parse
     * @return The parsed identifier
     */
    public static Identifier fromString(String parseable) {
        String[] parsed = parseable.split(":");

        if (parsed.length == 1) {
            return Identifier.from(null, parsed[0]);
        }
        return Identifier.from(parsed[0], parsed[1]);
    }

    /**
     * Get the string representation of the identifier. This will return namespace:name or name if the namespace is null.
     *
     * @return The string representation of the identifier
     */
    @Override
    public String toString() {
        String value = "";
        if (this.getNamespace() != null) value += (this.getNamespace() + ":");
        value += this.getName();
        return value;
    }
}
