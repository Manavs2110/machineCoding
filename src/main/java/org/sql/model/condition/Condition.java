package org.sql.model.condition;

import org.sql.model.Row;

// Strategy: every WHERE operator is its own small class with one job, matches(row).
public interface Condition {
    boolean matches(Row row);
}
