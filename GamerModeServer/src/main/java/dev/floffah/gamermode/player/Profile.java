package dev.floffah.gamermode.player;

import dev.floffah.gamermode.error.UUIDMismatchException;
import dev.floffah.gamermode.util.UUIDUtil;
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
        // session server stuff
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

        // read the player's data

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
            UUID uuid = UUIDUtil.intArrayToUUID(uuidInts);

            if (
                !this.getPlayer().getUniqueId().equals(uuid)
            ) throw new UUIDMismatchException();

            if (rawdata.containsKey("previousPlayerGameType")) {
                this.getPlayer().previousGameMode =
                    rawdata.getByte("previousPlayerGameType");
            }
            if (
                this.getPlayer()
                    .getConn()
                    .getSocketManager()
                    .getServer()
                    .getConfig()
                    .worlds.enforceDefaultGamemode ||
                !rawdata.containsKey("playerGameType")
            ) {
                this.getPlayer().gameMode =
                    this.getPlayer()
                        .getConn()
                        .getSocketManager()
                        .getServer()
                        .getConfig()
                        .worlds.defaultGamemode;
            } else {
                this.getPlayer().gameMode = rawdata.getByte("playerGameType");
            }

            long worldUUIDMost = rawdata.getLong("WorldUUIDMost");
            long worldUUIDLeast = rawdata.getLong("WorldUUIDLeast");
            UUID worldUUID = new UUID(worldUUIDMost, worldUUIDLeast);

            if (wm.getWorlds().containsKey(worldUUID)) this.player.world =
                wm.getWorlds().get(worldUUID);
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
