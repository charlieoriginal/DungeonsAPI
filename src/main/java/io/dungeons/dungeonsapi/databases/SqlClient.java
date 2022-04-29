package io.dungeons.dungeonsapi.databases;

import io.dungeons.dungeonsapi.databases.builders.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlClient {

    @Getter private Plugin plugin;
    @Getter private String ip;
    @Getter private String dbName;
    @Getter private String user;
    @Getter private String pass;
    @Getter private int port;
    private Connection connection;
    public SqlClient(Plugin plugin, String ip, int port, String dbName, String user, String pass) {
        this.plugin = plugin;
        this.ip = ip;
        this.port = port;
        this.dbName = dbName;
        this.user = user;
        this.pass = pass;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://"+ip+":"+port+"/"+dbName+"?user="+user+"&password="+pass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new SqlKeepAliveTask(this), 0L, 20L);
    }

    public void keepAlive() {
        try {
            if (connection != null && connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:postgresql://"+ip+":"+port+"/"+dbName+"?user="+user+"&password="+pass);
            } else if (connection == null) {
                connection = DriverManager.getConnection("jdbc:postgresql://"+ip+":"+port+"/"+dbName+"?user="+user+"&password="+pass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    The first column always acts as the tables primary key.
     */
    public void createTable(String id, PostgresColumn... columns) {
        CreateTableBuilder builder = new CreateTableBuilder(id);
        for (PostgresColumn column : columns) {
            builder.addColumn(column);
        }
        String query = builder.build();
        sendUpdateSync(query);
    }

    /*
    Adds a column to a pre-existing table.
     */
    public void addColumnToTable(String table, String colId, PostgresType type) {
        AddColumnBuilder builder = new AddColumnBuilder(table);
        builder.addColumn(colId, type);
        sendUpdateSync(builder.build());
    }

    /*
    Adds a single data point into a table.
     */
    public void insertData(String table, PostgresData data) {
        InsertBuilder builder = new InsertBuilder(table);
        builder.addData(data);
        String query = builder.build();
        sendUpdateSync(query);
    }

    /*
    Adds multiple data points into a table.
     */
    public void insertData(String table, PostgresData... data) {
        InsertBuilder builder = new InsertBuilder(table);
        for (PostgresData d : data) {
            builder.addData(d);
        }
        String query = builder.build();
        sendUpdateSync(query);
    }

    /*
    Updates a data value.
     */
    public void updateData(String id, String key, String table, PostgresData data) {
        UpdateBuilder builder = new UpdateBuilder(id, key, table);
        builder.addData(data);
        String query = builder.build();
        sendUpdateSync(query);
    }

    public boolean keyExists(String table, String key, String value) {
        try {
            String sql = "SELECT "+key+" FROM "+table+" WHERE "+key+" = '"+value+"'";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException e) {}
        return false;
    }

    public void updateData(String id, String key, String table, PostgresData... data) {
        UpdateBuilder builder = new UpdateBuilder(id, key, table);
        for (PostgresData d : data) {
            builder.addData(d);
        }
        String query = builder.build();
        sendUpdateSync(query);
    }

    /*
    Deletes a specific primary key from a table.
     */
    public void deleteData(String table, String id, String data) {
        DeleteBuilder builder = new DeleteBuilder(table, id, data);
        String query = builder.build();
        sendUpdateSync(query);
    }

    /*
    Deletes a specific primary key from a table (w/ requirements).
     */
    public void deleteData(String table, PostgresRequirement... requirement) {
        DeleteBuilder builder = new DeleteBuilder(table, requirement);
        String query = builder.build();
        System.out.println(query);
        sendUpdateSync(query);
    }

    /*
    Fetch a single datapoint from a table.
     */
    public List<String> fetchData(String table, String id, PostgresRequirement... requirements) {
        QueryBuilder queryBuilder = new QueryBuilder(table, id);
        queryBuilder.setRequirements(requirements);
        String query = queryBuilder.build();
        ResultSet set = sendQuerySync(query);
        List<String> data = new ArrayList<>();
        try {
            ResultSetMetaData rsmd = set.getMetaData();
            int columns = rsmd.getColumnCount();
            while (set.next()) {
                for (int i = 1; i <= columns; i++) {
                    String dat = set.getString(i);
                    data.add(dat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    /*
    Fetch Multiple Data Points From A Table
     */
    public List<PostgresRow> fetchDataMultiple(String table, String[] ids, PostgresRequirement... requirements) {
        QueryBuilder queryBuilder = new QueryBuilder(table, ids);
        queryBuilder.setRequirements(requirements);
        String query = queryBuilder.build();
        ResultSet set = sendQuerySync(query);
        List<PostgresRow> data = new ArrayList<>();
        try {
            ResultSetMetaData rsmd = set.getMetaData();
            int columns = rsmd.getColumnCount();
            while (set.next()) {
                PostgresRow postgresRow = new PostgresRow(set.getRow());
                for (int i = 1; i <= columns; i++) {
                    String dat = set.getString(i);
                    postgresRow.addData(rsmd.getColumnName(i), dat);
                }
                data.add(postgresRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void sendUpdateSync(String update) {
        try {
            PreparedStatement statement = connection.prepareStatement(update);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet sendQuerySync(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
