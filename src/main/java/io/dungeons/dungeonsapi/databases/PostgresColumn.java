package io.dungeons.dungeonsapi.databases;

import lombok.Getter;

public class PostgresColumn {

    @Getter private String id;
    @Getter private PostgresType type;
    public PostgresColumn(String id, PostgresType type) {
        this.id = id;
        this.type = type;
    }

}
