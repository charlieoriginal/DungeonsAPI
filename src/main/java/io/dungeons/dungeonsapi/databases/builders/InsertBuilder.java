package io.dungeons.dungeonsapi.databases.builders;

import io.dungeons.dungeonsapi.databases.PostgresData;

import java.util.ArrayList;
import java.util.List;

public class InsertBuilder {

    private StringBuilder builder = new StringBuilder();
    private List<PostgresData> data = new ArrayList<>();
    private String table;

    public InsertBuilder(String table) {
        this.table = table;
    }

    public InsertBuilder addData(PostgresData data) {
        this.data.add(data);
        return this;
    }

    public String build() {
        builder.append("INSERT INTO " + table + " (");
        int i = 1;
        for (PostgresData point : data) {
            builder.append(point.getId());
            if (i < data.size()) {
                builder.append(", ");
            }
            i++;
        }
        builder.append(") VALUES (");
        i = 1;
        for (PostgresData point : data) {
            builder.append("'"+point.getData()+"'");
            if (i < data.size()) {
                builder.append(", ");
            }
            i++;
        }
        builder.append(");");
        return builder.toString();
    }

}
