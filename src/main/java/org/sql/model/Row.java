package org.sql.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class Row {
    long id;
    Map<String, Object> values;

    public Row(long id, Map<String, Object> values) {
        this.id = id;
        this.values = new HashMap<>(values);
    }

    public long getId() {
        return id;
    }

    public Map<String, Object> getValues() {
        return values;
    }
}
