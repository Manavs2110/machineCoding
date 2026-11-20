package org.sql.model.condition;

import org.sql.model.Row;

// WHERE column < value   (numbers only)
public class LtCondition implements Condition {
    String column;
    Number value;

    public LtCondition(String column, Number value) {
        this.column = column;
        this.value = value;
    }

    public boolean matches(Row row) {
        Object stored = row.getValues().get(column);
        return stored instanceof Number && ((Number) stored).doubleValue() < value.doubleValue();
    }
}
