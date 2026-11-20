package org.sql.service;

import org.sql.model.Column;
import org.sql.model.condition.Condition;
import org.sql.model.Database;
import org.sql.model.IndexType;
import org.sql.model.Table;

import java.util.List;
import java.util.Map;

// Single entry point for callers: works by table name, finds the table, and delegates to it.
public class DatabaseService {
    private final Database database;

    public DatabaseService(Database database) {
        this.database = database;
    }

    public void createTable(String tableName, List<Column> columns) {
        database.createTable(tableName, columns); // throws if the table already exists
    }

    public void dropTable(String tableName) {
        getTable(tableName);
        database.dropTable(tableName);
    }

    public void insert(String tableName, Map<String, Object> row) {
        getTable(tableName).insert(row);
    }

    public List<Map<String, Object>> select(String tableName, List<String> cols, Condition where) {
        return getTable(tableName).select(cols, where);
    }

    public List<Map<String, Object>> select(String tableName, List<String> cols, Condition where,
                                            String orderBy, boolean ascending, int limit) {
        return getTable(tableName).select(cols, where, orderBy, ascending, limit);
    }

    public int update(String tableName, Map<String, Object> set, Condition where) {
        return getTable(tableName).update(set, where);
    }

    public int delete(String tableName, Condition where) {
        return getTable(tableName).delete(where);
    }

    public void createIndex(String tableName, String column, IndexType type) {
        getTable(tableName).createIndex(column, type);
    }

    private Table getTable(String tableName) {
        Table table = database.getTable(tableName);
        if (table == null) {
            throw new RuntimeException("Table " + tableName + " doesn't exist");
        }
        return table;
    }
}
