package io.dungeons.dungeonsapi.databases.builders;

import io.dungeons.dungeonsapi.databases.PostgresRequirement;

public class DeleteBuilder {

    private String table;
    private String id;
    private String data;
    private PostgresRequirement[] requirements = null;

    public DeleteBuilder(String table, String id, Object data) {
        this.table = table;
        this.id = id;
        this.data = "'" + data + "'";
    }

    public DeleteBuilder(String table, PostgresRequirement... requirement) {
        this.table = table;
        this.requirements = requirement;
    }


    public String build() {
        if (requirements == null) {
            return "DELETE FROM " + table + " WHERE " + id + "=" + data;
        } else {
            StringBuilder s = new StringBuilder("DELETE FROM " + table + " WHERE ");
            int i = 1;
            for (PostgresRequirement postgresRequirement : requirements) {
                if (i != requirements.length) {
                    s.append(postgresRequirement.toWhere());
                } else {
                    s.append(postgresRequirement.toWhere()).append(",");
                }
            }
            return s.toString();
        }
    }

}
