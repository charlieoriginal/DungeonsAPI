package io.dungeons.dungeonsapi.databases;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class PostgresRow {

    @Getter private int rowId;
    @Getter private Map<String, Object> data;

    public PostgresRow(int row) {
        this.rowId = row;
        this.data = new HashMap<>();
    }

    public void addData(String colId, Object o) {
        this.data.put(colId, o);
    }

}
