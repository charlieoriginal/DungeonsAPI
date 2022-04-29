package io.dungeons.dungeonsapi.databases.builders;

import io.dungeons.dungeonsapi.databases.PostgresColumn;

import java.util.ArrayList;
import java.util.List;

public class CreateTableBuilder {

    private StringBuilder query = new StringBuilder();
    private List<PostgresColumn> columns = new ArrayList<>();
    private PostgresColumn primaryKey;

    public CreateTableBuilder(String id) {
        query.append("CREATE TABLE IF NOT EXISTS ").append(id).append(" (");
    }

    public CreateTableBuilder setPrimaryKey(PostgresColumn column) {
        primaryKey = column;
        return this;
    }

    public CreateTableBuilder addColumn(PostgresColumn column) {
        if (primaryKey == null) {
            setPrimaryKey(column);
        }
        columns.add(column);
        return this;
    }

    public String build() {
        int i = 1;
        for (PostgresColumn column : columns) {
            query.append(column.getId());
            query.append(" ");
            query.append(column.getType().getType());
            if (i != columns.size()) {
                query.append(", ");
            }
            i++;
        }

        query.append(", PRIMARY KEY (").append(primaryKey.getId()).append(")");
        query.append(");");
        return query.toString();
    }

}
