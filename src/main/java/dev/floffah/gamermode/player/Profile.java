package dev.floffah.gamermode.player;

import dev.floffah.gamermode.error.UUIDMismatchException;
import dev.floffah.gamermode.world.WorldManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import lombok.Getter;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.IntArrayTag;
import org.json.JSONObject;

public class Profile {

    public static String sessionhasJoinedURL =
        "https://sessionserver.mojang.com/session/minecraft/hasJoined";

    /**
     * The player
     * <p>
     * -- GETTER --
     * Get the player
     *
     * @return The player
     */
    @Getter
    Player player;

    public Profile(Player player) {
        this.player = player;
    }

    public void doHasJoined() throws IOException, UUIDMismatchException {
        URL joinUrl = new URL(
            sessionhasJoinedURL +
            "?username=" +
            this.player.username +
            "&serverId=" +
            this.player.conn.getSessionHash()
        );
        HttpURLConnection conn = (HttpURLConnection) joinUrl.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        StringBuilder content = new StringBuilder();
        String current;
        while ((current = in.readLine()) != null) {
            content.append(current);
        }
        in.close();
        conn.disconnect();

        JSONObject response = new JSONObject(content.toString());

        String unformattedUUID = response.getString("id");
        String formattedUUID =
            unformattedUUID.substring(0, 8) +
            "-" +
            unformattedUUID.substring(8, 12) +
            "-" +
            unformattedUUID.substring(12, 16) +
            "-" +
            unformattedUUID.substring(16, 20) +
            "-" +
            unformattedUUID.substring(20, 32);
        this.player.uniqueId = UUID.fromString(formattedUUID);

        if (
            !this.player.username.equals(response.getString("name"))
        ) this.player.username = response.getString("name");

        WorldManager wm =
            this.getPlayer()
                .getConn()
                .getSocketManager()
                .getServer()
                .getWorldManager();

        if (wm.hasRawPlayerData(this.getPlayer().getUniqueId())) {
            CompoundTag rawdata = wm.readRawPlayerData(
                this.getPlayer().getUniqueId()
            );

            int[] uuidInts = ((IntArrayTag) rawdata.get("UUID")).getValue();

            int uuidMostA = uuidInts[0];
            int uuidMostB = uuidInts[1];
            int uuidLeastA = uuidInts[2];
            int uuidLeastB = uuidInts[3];
            long uuidMost = (long) uuidMostA << 32 | uuidMostB & 0xFFFFFFFFL;
            long uuidLeast = (long) uuidLeastA << 32 | uuidLeastB & 0xFFFFFFFFL;

            UUID uuid = new UUID(uuidMost, uuidLeast);

            if (!this.getPlayer().getUniqueId().equals(uuid)) throw new UUIDMismatchException();
        }

        wm.writeRawPlayerData(this.getPlayer());
    }

    /**
     * Sets information from the LoginStart packet;
     *
     * @param username The client's claimed username
     */
    public void startLogin(String username) {
        this.player.username = username;
    }
}
