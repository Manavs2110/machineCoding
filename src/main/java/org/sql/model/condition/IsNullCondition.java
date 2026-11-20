package org.sql.model.condition;

import org.sql.model.Row;

// WHERE column IS NULL
public class IsNullCondition implements Condition {
    String column;

    public IsNullCondition(String column) {
        this.column = column;
    }

    public boolean matches(Row row) {
        return row.getValues().get(column) == null;
    }
}
