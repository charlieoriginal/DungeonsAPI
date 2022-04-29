package io.dungeons.dungeonsapi.databases.playerdata;

import io.dungeons.dungeonsapi.databases.PostgresColumn;
import io.dungeons.dungeonsapi.databases.PostgresType;
import io.dungeons.dungeonsapi.databases.SqlClient;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataHandler {

    private Map<UUID, PlayerDataCache> cacheMap = new HashMap<>();
    private SqlClient sqlClient;
    private String table;

    public PlayerDataHandler(SqlClient sqlClient, String table) {
        this.sqlClient = sqlClient;
        this.table = table;

        this.sqlClient.createTable(table, new PostgresColumn("uuid", PostgresType.UUID));
        this.sqlClient.addColumnToTable(table, "uuid", PostgresType.UUID);
    }

    public void refreshAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerDataCache cache = new PlayerDataCache(player, sqlClient, table);
            cache.refreshFromDatabase(sqlClient, table);
            cacheMap.put(player.getUniqueId(), cache);
        }
    }

    public PlayerDataCache getCachedData(UUID uuid) {
        PlayerDataCache cache = cacheMap.get(uuid);
        if (cache == null) {
            cache = new PlayerDataCache(uuid, sqlClient, table);
            cache.refreshFromDatabase(sqlClient, table);
            cacheMap.put(uuid, cache);
        }
        return cache;
    }

    public PlayerDataCache getCachedData(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerDataCache cache = cacheMap.get(uuid);
        if (cache == null) {
            cache = new PlayerDataCache(uuid ,sqlClient, table);
            cache.refreshFromDatabase(sqlClient, table);
            cacheMap.put(uuid, cache);
        }
        return cache;
    }

    public boolean addValueToPlayer(UUID uuid, String key, Object value) {
        try {
            PlayerDataCache cache = cacheMap.get(uuid);
            if (cache == null) {
                cache = new PlayerDataCache(uuid, sqlClient, table);
                cache.refreshFromDatabase(sqlClient, table);
                cacheMap.put(uuid, cache);
            }
            this.sqlClient.addColumnToTable(table, key, PostgresType.TEXT);
            cache.updateValue(sqlClient, table, key, value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean addValueToPlayer(Player player, String key, Object value) {
        UUID uuid = player.getUniqueId();
        try {
            PlayerDataCache cache = cacheMap.get(uuid);
            if (cache == null) {
                cache = new PlayerDataCache(uuid, sqlClient, table);
                cache.refreshFromDatabase(sqlClient, table);
                cacheMap.put(uuid, cache);
            }
            this.sqlClient.addColumnToTable(table, key, PostgresType.TEXT);
            cache.updateValue(sqlClient, table, key, value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean deletePlayerData(Player player) {
        try {
            if (cacheMap.containsKey(player.getUniqueId())) {
                cacheMap.remove(player.getUniqueId());
            }
            sqlClient.deleteData(table, "uuid", player.getUniqueId().toString());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean deletePlayerData(UUID uuid) {
        try {
            if (cacheMap.containsKey(uuid)) {
                cacheMap.remove(uuid);
            }
            sqlClient.deleteData(table, "uuid", uuid.toString());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /*
    Use this if you want to force the cache to reload all data points.
     */
    public void invalidateCache() {
        cacheMap.clear();
    }

}
