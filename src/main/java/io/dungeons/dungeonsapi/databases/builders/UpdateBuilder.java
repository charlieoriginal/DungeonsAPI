package io.dungeons.dungeonsapi.databases.builders;

import io.dungeons.dungeonsapi.databases.PostgresData;

import java.util.ArrayList;
import java.util.List;

public class UpdateBuilder {

    private StringBuilder builder = new StringBuilder();
    private List<PostgresData> data = new ArrayList<>();
    private String table;
    private String id;
    private String key;

    public UpdateBuilder(String id, String key, String table) {
        this.table = table;
        this.id = id;
        this.key = key;
    }

    public UpdateBuilder addData(PostgresData data) {
        this.data.add(data);
        return this;
    }

    public String build() {
        builder.append("UPDATE " + table + " SET ");
        int i = 1;
        for (PostgresData point : data) {
            builder.append(point.getId() + "='" + point.getData() + "'");
            if (i != data.size()) {
                builder.append(", ");
            }
            i++;
        }
        builder.append(" WHERE " + id + "='" + key + "';");
        return builder.toString();
    }

}
