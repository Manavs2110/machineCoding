package org.sql.model;

import org.sql.model.condition.Condition;
import org.sql.model.condition.EqCondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Table {
    String name;
    Schema schema;
    Map<Long, Row> rowMap;
    long rowId;
    // column name -> (column value -> ids of rows with that value)
    Map<String, Map<Object, Set<Long>>> indexes;
    // many readers (select) at once, or one writer (insert/update/delete/createIndex) alone
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Table(String name, Schema schema) {
        this.name = name;
        this.schema = schema;
        rowMap = new LinkedHashMap<>();
        rowId=1;
        indexes = new HashMap<>();
    }

    public String getName() {
        return name;
    }
    public void insert(Map<String, Object> row){
        lock.writeLock().lock();
        try {
            schema.validateRow(row);
            checkUnique(List.of(row), Set.of());
            // all checks passed -> store it
            Row newRow = new Row(rowId, row);
            rowMap.put(rowId, newRow);
            addToIndexes(newRow);
            rowId++;
        } finally {
            lock.writeLock().unlock();
        }
    }
    // newRows: values about to be written. skipIds: existing rows they replace (so a row doesn't clash with itself).
    private void checkUnique(List<Map<String, Object>> newRows, Set<Long> skipIds){
        for(Column column: schema.getColumns()){
            if(!column.isUnique()) continue;
            Set<Object> seen = new HashSet<>();
            for(Row existing: rowMap.values()){
                Object value = existing.getValues().get(column.getName());
                if(value != null && !skipIds.contains(existing.getId())) seen.add(value);
            }
            for(Map<String, Object> row: newRows){
                Object value = row.get(column.getName());
                if(value == null) continue; // many NULLs are allowed
                if(!seen.add(value)){
                    throw new RuntimeException("Duplicate value " + value + " for column " + column.getName());
                }
            }
        }
    }
    public List<Map<String, Object>> select(List<String> cols, Condition where){
        return select(cols, where, null, true, -1);
    }

    // orderBy: column to sort by (null = no sorting). ascending: ASC or DESC. limit: max rows (-1 = no limit).
    public List<Map<String, Object>> select(List<String> cols, Condition where, String orderBy, boolean ascending, int limit){
        lock.readLock().lock();
        try {
            // which columns to return: null means SELECT *
            List<String> columnsToReturn = new ArrayList<>();
            if(cols == null){
                for(Column column: schema.getColumns()) columnsToReturn.add(column.getName());
            } else {
                for(String col: cols){
                    if(schema.findColumn(col) == null) throw new RuntimeException("Select Failed: Column " + col + " doesn't exist");
                    columnsToReturn.add(col);
                }
            }

            // 1. WHERE: keep matching rows
            List<Row> rows = new ArrayList<>();
            for(Row row: candidateRows(where)){
                if(where == null || where.matches(row)) rows.add(row); // null where means every row
            }

            // 2. ORDER BY
            if(orderBy != null){
                if(schema.findColumn(orderBy) == null) throw new RuntimeException("Select Failed: Column " + orderBy + " doesn't exist");
                rows.sort((a, b) -> {
                    int cmp = compareValues(a.getValues().get(orderBy), b.getValues().get(orderBy));
                    return ascending ? cmp : -cmp;
                });
            }

            // 3. LIMIT
            if(limit >= 0 && limit < rows.size()){
                rows = rows.subList(0, limit);
            }

            // 4. pick the requested columns
            List<Map<String, Object>> result = new ArrayList<>();
            for(Row row: rows){
                Map<String, Object> out = new LinkedHashMap<>();   // keeps column order
                for(String col: columnsToReturn){
                    out.put(col, row.getValues().get(col));
                }
                result.add(out);
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    // NULL is the smallest value, so it comes first in ASC and last in DESC.
    private int compareValues(Object a, Object b){
        if(a == null && b == null) return 0;
        if(a == null) return -1;
        if(b == null) return 1;
        return ((Comparable) a).compareTo(b);
    }

    public int update(Map<String, Object> set, Condition where){
        lock.writeLock().lock();
        try {
            // 1. build the new version of every matching row and check it -- nothing is written yet
            List<Row> oldRows = new ArrayList<>();
            List<Row> updatedRows = new ArrayList<>();
            Set<Long> updatedIds = new HashSet<>();
            for(Row row: candidateRows(where)){
                if(where != null && !where.matches(row)) continue;
                Map<String, Object> merged = new HashMap<>(row.getValues());
                merged.putAll(set);
                schema.validateRow(merged);
                oldRows.add(row);
                updatedRows.add(new Row(row.getId(), merged));
                updatedIds.add(row.getId());
            }
            List<Map<String, Object>> newValues = new ArrayList<>();
            for(Row row: updatedRows) newValues.add(row.getValues());
            checkUnique(newValues, updatedIds);

            // 2. every check passed -> replace the rows and move them in the indexes
            for(Row row: oldRows) removeFromIndexes(row);
            for(Row row: updatedRows){
                rowMap.put(row.getId(), row);
                addToIndexes(row);
            }
            return updatedRows.size();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int delete(Condition where){
        lock.writeLock().lock();
        try {
            List<Row> toDelete = new ArrayList<>();
            for(Row row: candidateRows(where)){
                if(where == null || where.matches(row)) toDelete.add(row);
            }
            for(Row row: toDelete){
                rowMap.remove(row.getId());
                removeFromIndexes(row);
            }
            return toDelete.size();
        } finally {
            lock.writeLock().unlock();
        }
    }

    // ---------------- indexes ----------------

    public void createIndex(String column, IndexType type){
        lock.writeLock().lock();
        try {
            if(schema.findColumn(column) == null) throw new RuntimeException("Index Failed: Column " + column + " doesn't exist");
            if(indexes.containsKey(column)) throw new RuntimeException("Index Failed: Column " + column + " is already indexed");

            Map<Object, Set<Long>> index = (type == IndexType.HASH) ? new HashMap<>() : new TreeMap<>();
            // fill it with the rows that already exist
            for(Row row: rowMap.values()){
                Object value = row.getValues().get(column);
                if(value != null) index.computeIfAbsent(value, k -> new HashSet<>()).add(row.getId());
            }
            indexes.put(column, index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Rows that might match: for "column = value" on an indexed column, only the rows in the index.
    // Otherwise every row. Callers still run where.matches() on each.
    private Collection<Row> candidateRows(Condition where){
        if(where instanceof EqCondition eq && indexes.containsKey(eq.getColumn())){
            Column column = schema.findColumn(eq.getColumn());
            // skip NULL / wrong-type values: they never match, and a TreeMap would throw on them
            if(eq.getValue() == null || !schema.matchesType(column.getColumnType(), eq.getValue())) return List.of();

            List<Row> rows = new ArrayList<>();
            for(Long id: indexes.get(eq.getColumn()).getOrDefault(eq.getValue(), Set.of())){
                rows.add(rowMap.get(id));
            }
            return rows;
        }
        return rowMap.values();
    }

    private void addToIndexes(Row row){
        for(Map.Entry<String, Map<Object, Set<Long>>> entry: indexes.entrySet()){
            Object value = row.getValues().get(entry.getKey());
            if(value == null) continue; // NULLs are not indexed
            entry.getValue().computeIfAbsent(value, k -> new HashSet<>()).add(row.getId());
        }
    }

    private void removeFromIndexes(Row row){
        for(Map.Entry<String, Map<Object, Set<Long>>> entry: indexes.entrySet()){
            Object value = row.getValues().get(entry.getKey());
            if(value == null) continue;
            Set<Long> ids = entry.getValue().get(value);
            if(ids == null) continue;
            ids.remove(row.getId());
            if(ids.isEmpty()) entry.getValue().remove(value); // drop empty entries
        }
    }
}
