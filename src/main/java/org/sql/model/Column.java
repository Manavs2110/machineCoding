package org.sql.model;

import java.util.Objects;

public final class Column {
    String name;
    ColumnType columnType;
    boolean primaryKey;
    boolean notNull;
    boolean unique;

    public Column(String name, ColumnType columnType, boolean primaryKey, boolean notNull, boolean unique) {
        this.name = Objects.requireNonNull(name, "name");
        this.columnType = Objects.requireNonNull(columnType, "columnType");
        this.primaryKey = primaryKey;
        this.notNull = notNull || primaryKey;
        this.unique = unique || primaryKey;
    }
    public Column(String name, ColumnType columnType) {
        this(name, columnType, false, false, false);
    }

    public String getName() {
        return name;
    }

    public boolean isUnique() {
        return unique;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public ColumnType getColumnType() {
        return columnType;
    }
}
