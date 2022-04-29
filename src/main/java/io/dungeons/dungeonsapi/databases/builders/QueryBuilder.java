package io.dungeons.dungeonsapi.databases.builders;

import io.dungeons.dungeonsapi.databases.PostgresRequirement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryBuilder {

    private String table;
    private String[] id;
    private List<PostgresRequirement> requirements = new ArrayList<>();
    public QueryBuilder(String table, String id) {
        this.table = table;
        this.id = new String[]{id};
    }

    public QueryBuilder(String table, String[] ids) {
        this.table = table;
        this.id = ids;
    }

    public void setRequirements(PostgresRequirement[] requirements) {
        this.requirements = Arrays.asList(requirements);
    }

    public QueryBuilder addRequirement(PostgresRequirement requirement) {
        requirements.add(requirement);
        return this;
    }

    public String build() {
        String query = "SELECT ";
        int n = 1;
        for (String ids : id) {
            query = query+ids;

            if (n != id.length) {
                query = query + ",";
            }
            n++;
        }
        query = query + " FROM " + table;

        if (requirements.size() != 0) {
            query += " WHERE ";
        }

        int i = 1;
        for (PostgresRequirement requirement : requirements) {
            query = query + requirement.toWhere();

            if (i != requirements.size()) {
                query = query+", ";
            }
            i++;
        }
        return query;
    }

}
