package dev.floffah.gamermode.server.cache;

import dev.floffah.gamermode.server.Server;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

/**
 * Server cache<br/>
 * Currently isn't used
 */
public class CacheProvider {

    /**
     * The directory containing cache
     */
    public File cachedir;
    /**
     * The server
     */
    Server server;
    /**
     * Cache info
     */
    JSONObject inf;

    /**
     * Construct a cacheprovider
     *
     * @param server The server
     */
    public CacheProvider(Server server) {
        this.server = server;

        cachedir = Path.of(server.getDataDir().getPath(), "cache").toFile();
        if (!cachedir.exists()) cachedir.mkdirs();

        Path cacheinfo = Path.of(cachedir.getPath(), "cacheinfo.dat");

        try {
            if (cacheinfo.toFile().exists()) {
                String sinf = Files.readString(cacheinfo);
                inf = new JSONObject(sinf);
            } else {
                inf = new JSONObject();
            }
            validate();
        } catch (IOException e) {
            server.getLogger().error("Error occurred while trying to read cacheinfo file", e);
        }
    }

    /**
     * Validate the cache and purge old cached data
     *
     * @throws IOException Any exceptions thrown along the way
     */
    public void validate() throws IOException {
        if (
            !inf.has("lastPlayerPurge") ||
            inf.getLong("lastPlayerPurge") <
            (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
        ) {
            File entriesdir = Path.of(cachedir.getPath(), "ids").toFile();
            if (!entriesdir.exists()) entriesdir.mkdirs();
            File[] entries = entriesdir.listFiles();
            if (entries == null || entries.length <= 0) return;
            for (File entry : entries) {
                if (!entry.isDirectory()) {
                    JSONObject idcache = new JSONObject(
                        Files.readString(entry.toPath())
                    );
                    if (
                        idcache.getLong("lastCheck") <
                        (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
                    ) {
                        if (!entry.delete()) {
                            idcache.put("lastCheck", (long) 0);
                            Files.writeString(
                                entry.toPath(),
                                idcache.toString()
                            );
                        }
                    }
                }
            }
            inf.put("lastPlayerPurge", System.currentTimeMillis());
        }
    }
}
