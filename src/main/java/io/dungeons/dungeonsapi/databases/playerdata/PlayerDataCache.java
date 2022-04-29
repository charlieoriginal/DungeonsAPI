package io.dungeons.dungeonsapi.databases.playerdata;

import io.dungeons.dungeonsapi.databases.PostgresData;
import io.dungeons.dungeonsapi.databases.PostgresRequirement;
import io.dungeons.dungeonsapi.databases.PostgresRow;
import io.dungeons.dungeonsapi.databases.SqlClient;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerDataCache {

    private UUID uuid;
    private Map<String, Object> dataMap = new HashMap<>();
    public PlayerDataCache(UUID uuid, SqlClient sqlClient, String table) {
        if (!sqlClient.keyExists(table, "uuid", uuid.toString())) {
            sqlClient.insertData(table, new PostgresData("uuid", uuid.toString()));
        }
        this.uuid = uuid;
    }

    public PlayerDataCache(Player player, SqlClient sqlClient, String table) {
        if (!sqlClient.keyExists(table, "uuid", uuid.toString())) {
            sqlClient.insertData(table, new PostgresData("uuid", uuid.toString()));
        }
        this.uuid = player.getUniqueId();
    }

    public void refreshFromDatabase(SqlClient sqlClient, String table) {
        List<PostgresRow> rows = sqlClient.fetchDataMultiple(table, new String[]{"*"}, new PostgresRequirement("uuid", uuid.toString()));

        for (PostgresRow row : rows) {
            for (String s : row.getData().keySet()) {
                Object data = row.getData().get(s);
                dataMap.put(s, data);
            }
        }
    }

    public Object getData(String key) {
        return dataMap.get(key);
    }

    public void updateValue(SqlClient client, String table, String key, Object data) {
        dataMap.put(key, data);
        client.updateData("uuid", uuid.toString(), table, new PostgresData("uuid", uuid.toString()), new PostgresData(key, data));
    }

    public void setUpdating(Plugin plugin, SqlClient client, String table, int period) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            refreshFromDatabase(client, table);
        }, 0L, period*20L);
    }

}
