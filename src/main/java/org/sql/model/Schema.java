package org.sql.model;


import java.util.List;
import java.util.Map;

public final class Schema {
    List<Column> columns;

    public Schema(List<Column> columns) {
        this.columns = columns;
    }

    public List<Column> getColumns() {
        return columns;
    }
    public Column findColumn(String key){
        for(Column column: columns){
            if(column.getName().equals(key)) return column;
        }
        return null;
    }
    public void validateRow(Map<String, Object> row){
        // every key must be a real column
        for(String key: row.keySet()){
            if(findColumn(key) == null) throw new RuntimeException("Insert Failed: Column " + key + " doesn't exist");
        }
        // check every column, including ones the row left out (they come back as null)
        for(Column column: columns){
            validateValue(column, row.get(column.getName()));
        }
    }
    private void validateValue(Column column, Object value){
        if(value == null){
            if(column.isNotNull()) throw new RuntimeException("Insert Failed: Column " + column.getName() + " cannot be null");
            return;
        }
        if(!matchesType(column.getColumnType(), value)) throw new RuntimeException("Insert Failed: Column " + column.getName() + " expects "
                + column.getColumnType() + " but got " + value.getClass().getSimpleName());
    }
    public boolean matchesType(ColumnType type, Object value){
        return switch (type){
            case INT -> value instanceof Integer;
            case DOUBLE -> value instanceof Double;
            case STRING -> value instanceof String;
            case BOOLEAN -> value instanceof Boolean;
        };
    }
}
