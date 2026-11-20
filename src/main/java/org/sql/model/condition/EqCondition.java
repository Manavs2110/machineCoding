package org.sql.model.condition;

import org.sql.model.Row;

// WHERE column = value   (comparing with NULL never matches)
public class EqCondition implements Condition {
    String column;
    Object value;

    public EqCondition(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }

    public boolean matches(Row row) {
        return value != null && value.equals(row.getValues().get(column));
    }
}
