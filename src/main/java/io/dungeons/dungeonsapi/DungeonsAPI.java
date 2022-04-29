package io.dungeons.dungeonsapi;

import io.dungeons.dungeonsapi.databases.DatabaseModule;
import io.dungeons.dungeonsapi.databases.SqlClient;
import org.bukkit.plugin.java.JavaPlugin;

public final class DungeonsAPI extends JavaPlugin {
    public static DungeonsAPI instance;
    private SqlClient sqlClient;
    private DatabaseModule databaseModule;

    @Override
    public void onEnable() {
        instance = this;

        this.databaseModule = new DatabaseModule();
        this.databaseModule.instantiate(this);
        this.sqlClient = this.databaseModule.getSqlClient("localhost", 5432, "dungeons", "postgres", "lewish09");
    }

    @Override
    public void onDisable() {

    }
}
