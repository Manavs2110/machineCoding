package org.sql.model;

public enum IndexType {
    HASH,   // HashMap: fast "="
    BTREE   // TreeMap: sorted, fast "=" and ready for ranges
}
