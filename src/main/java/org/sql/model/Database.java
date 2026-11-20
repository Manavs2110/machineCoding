package org.sql.model;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    Map< String, Table> tableMap;

    public Database() {
        this.tableMap = new ConcurrentHashMap<>();
    }
    public void createTable(String tableName, List<Column> columns){
        Table table = new Table(tableName, new Schema( columns));
        // putIfAbsent checks and inserts in one atomic step, so two threads can't both create it
        if(tableMap.putIfAbsent(tableName, table) != null){
            throw new RuntimeException("Table " + tableName + " already exists");
        }
    }
    public void dropTable(String tableName){
        tableMap.remove(tableName);
    }
    public Table getTable(String tableName){
        return tableMap.get(tableName);
    }
}
