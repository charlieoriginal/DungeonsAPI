package io.dungeons.dungeonsapi.databases;

import io.dungeons.dungeonsapi.Module;
import io.dungeons.dungeonsapi.databases.exception.SQLClientException;
import io.dungeons.dungeonsapi.databases.playerdata.PlayerDataHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class DatabaseModule implements Module {

    private JavaPlugin plugin;
    private PlayerDataHandler playerDataHandler = null;
    private SqlClient sqlClient = null;

    @Override
    public void instantiate(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /*
    A method for instantiating a global sqlclient for use in multiple plugins.
     */
    public SqlClient getSqlClient(String ip, int port, String dbName, String user, String pass) {
        if (this.sqlClient == null) {
            this.sqlClient = new SqlClient(this.plugin, ip, port, dbName, user, pass);
        }

        return this.sqlClient;
    }

    /*
    You must instantiate the global sqlclient before accessing this method.
     */
    public PlayerDataHandler getPlayerDataHandler(String table) throws SQLClientException {
        if (this.sqlClient == null) {
            //Throws exception when client does no exist.
            throw new SQLClientException("You must instantiate the global sqlclient before accessing this method.");
        }

        if (this.playerDataHandler == null) {
            this.playerDataHandler = new PlayerDataHandler(this.sqlClient, table);
        }

        return this.playerDataHandler;
    }

    public PlayerDataHandler getPlayerDataHandler(String table, SqlClient sqlClient) {
        if (this.playerDataHandler == null) {
            this.playerDataHandler = new PlayerDataHandler(sqlClient, table);
        }

        return this.playerDataHandler;
    }

    /*
    A method of instantiating playerdata without having to get the sqlclient with a different method.
     */
    public PlayerDataHandler getPlayerDataHandler(String table, String ip, int port, String dbName, String user, String pass) {
        if (this.sqlClient == null) {
            //Throws exception when client does no exist.
            this.sqlClient = new SqlClient(this.plugin, ip, port, dbName, user, pass);
        }

        if (this.playerDataHandler == null) {
            this.playerDataHandler = new PlayerDataHandler(this.sqlClient, table);
        }

        return this.playerDataHandler;
    }


}
