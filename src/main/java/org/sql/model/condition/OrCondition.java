package org.sql.model.condition;

import org.sql.model.Row;

// WHERE left OR right
public class OrCondition implements Condition {
    Condition left;
    Condition right;

    public OrCondition(Condition left, Condition right) {
        this.left = left;
        this.right = right;
    }

    public boolean matches(Row row) {
        return left.matches(row) || right.matches(row);
    }
}
