package io.dungeons.dungeonsapi.databases;

public class SqlKeepAliveTask implements Runnable {

    private SqlClient sqlClient;
    public SqlKeepAliveTask(SqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    @Override
    public void run() {
        sqlClient.keepAlive();
    }
}
