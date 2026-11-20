package org.sql.model.condition;

import org.sql.model.Row;

// WHERE left AND right
public class AndCondition implements Condition {
    Condition left;
    Condition right;

    public AndCondition(Condition left, Condition right) {
        this.left = left;
        this.right = right;
    }

    public boolean matches(Row row) {
        return left.matches(row) && right.matches(row);
    }
}
