# Design an In-Memory SQL Database

> **Language:** Java 17+ · **Package:** `org.sql` · **Time:** ~75 min (Part 1 ≈ 30, Part 2 ≈ 25, Part 3 ≈ 20)
> No external libraries. Everything lives in memory. No persistence to disk is required.

## The problem (as the interviewer says it)

> "Build a small relational database that lives entirely in memory. I should be able to create tables
> with typed columns and constraints, insert rows, query them with filters, update and delete them.
> Once that works, make it fast with indexes and safe to use from many threads. If you have time left,
> let me send it plain SQL strings."

You are judged on a working core first. Do **Part 1 completely** before touching Part 2, and Part 2 before Part 3.
A clean Part 1 beats a half-working Part 3.

---

## Part 1 — Must have (~30 min)

### 1.1 Tables and columns

A `Database` holds named tables. Each table has an ordered list of columns.

Supported column types (`ColumnType`):

| Type      | Java value accepted | Notes                                               |
|-----------|---------------------|-----------------------------------------------------|
| `INT`     | `Integer`           | Also accept `Long` if it fits in an `int`.          |
| `DOUBLE`  | `Double`            | An `Integer` value is widened to `Double` on store. |
| `STRING`  | `String`            |                                                     |
| `BOOLEAN` | `Boolean`           |                                                     |

Any other Java type, or a value of the wrong type (e.g. `"42"` into an `INT` column), is a `TypeMismatchException`.

Column constraints:

- **PRIMARY KEY** — at most one per table. Implies `NOT NULL` and `UNIQUE`.
- **NOT NULL** — value can never be `null`.
- **UNIQUE** — no two rows may share the same non-null value.

### 1.2 Operations

- **insert** — a map of column → value. Columns not mentioned are `null`. Unknown column names are an error.
- **select** — choose columns (or all), optionally filter with a `Condition`.
- **update** — set one or more columns on every row matching a condition. Returns the number of rows changed.
- **delete** — remove every row matching a condition. Returns the number of rows removed.

A `null` condition means "match every row".

### 1.3 Filters (`Condition`)

Support these comparisons: `eq`, `neq`, `gt`, `gte`, `lt`, `lte`, `isNull`, `isNotNull`,
and combining them with `and(...)` / `or(...)` (any number of children, nestable).

Comparisons follow the column type: numbers compare numerically (an `INT` can be compared with a `DOUBLE`),
strings lexicographically, booleans only with `eq` / `neq`. Comparing against an incompatible type
(e.g. `gt("name", 5)`) is a `TypeMismatchException`.

### 1.4 NULL rules (these are tested)

1. Any comparison (`eq`, `neq`, `gt`, …) where the stored value **or** the literal is `null` is **false**.
   `eq("email", null)` matches nothing. Use `isNull("email")` instead.
2. `isNull` / `isNotNull` are the only conditions that can match a `null`.
3. `and` is false if any child is false; `or` is true if any child is true. (You do **not** need full
   three-valued logic — treat "unknown" as false.)
4. `UNIQUE` allows **many** `null`s. Only non-null values must be distinct.
5. A `PRIMARY KEY` / `NOT NULL` column may never hold `null`, on insert **or** update.

### 1.5 Errors

All errors are unchecked and extend `DatabaseException`:

| Exception                     | When                                                            |
|-------------------------------|-----------------------------------------------------------------|
| `TableAlreadyExistsException` | `createTable` with an existing name                             |
| `TableNotFoundException`      | any operation on a missing table                                |
| `ColumnNotFoundException`     | unknown column in insert / select / update / where / index      |
| `TypeMismatchException`       | wrong Java type for a column, or incomparable comparison        |
| `NotNullViolationException`   | `null` written into a `NOT NULL` or `PRIMARY KEY` column        |
| `UniqueViolationException`    | duplicate value in a `UNIQUE` or `PRIMARY KEY` column           |
| `SchemaException`             | invalid schema (two primary keys, duplicate column names, no columns) |

A failed insert must leave the table **unchanged**.

---

## Part 2 — Should have (~25 min)

### 2.1 Indexes

`table.createIndex(indexName, columnName, IndexType)` where `IndexType` is `HASH` or `BTREE`.

- **HASH** — speeds up `eq` lookups.
- **BTREE** — speeds up `eq` **and** range lookups (`gt`, `gte`, `lt`, `lte`) and can feed `ORDER BY` on that column.
- Creating an index on existing data must index all current rows.
- Every `insert` / `update` / `delete` must keep every index in sync.
- A `UNIQUE` / `PRIMARY KEY` column should get an index automatically (your choice of type) so uniqueness checks are not O(n).
- Duplicate index name → `SchemaException`.

`table.explain(Query)` returns a short string describing the plan, so tests can see whether an index was used:

- `"INDEX_SCAN <indexName>"` when an index is used
- `"FULL_SCAN"` otherwise

You only need to use an index when the top-level condition is (or is an `and` containing) a single
comparison the index can answer. `or` may always fall back to `FULL_SCAN`.
Query results must be identical with or without an index.

### 2.2 ORDER BY and LIMIT

Selections are expressed with a `Query`:

```java
Query q = Query.select("id", "name")        // or Query.selectAll()
               .where(Condition.gt("age", 25))
               .orderBy("age", SortOrder.DESC)  // optional, may be called more than once for tie-breaks
               .limit(10);                       // optional
```

- `null`s sort **first** in `ASC` and **last** in `DESC`.
- Without `orderBy`, row order is unspecified (tests will not depend on it).
- `limit(n)` with `n < 0` → `IllegalArgumentException`. `limit(0)` returns no rows.
- You may order by a column that is not in the projection.

### 2.3 Thread safety

The database will be hit by many threads at once (tests use ~16 threads and thousands of operations).

- Concurrent reads must not block each other.
- No reader may ever see a half-applied write (e.g. a row updated but its index not yet updated).
- Two concurrent inserts with the same `UNIQUE` value: **exactly one** succeeds.
- Operations on different tables should not block each other.
- `createTable` / `dropTable` must be safe to call concurrently with other operations.

### 2.4 All-or-nothing UPDATE

If an `update` fails on **any** row (type error, `NOT NULL`, `UNIQUE` collision — including collisions
*between* rows changed in the same update), then **no row and no index** may change.

```text
users: (id=1, email='a'), (id=2, email='b')
UPDATE users SET email = 'x'   -> UniqueViolationException, both rows still 'a' and 'b'
```

`SET` only takes literal values in this problem (no `SET age = age + 1`).

The same rule applies to `delete` (trivially) and to multi-row `INSERT` in Part 3.

---

## Part 3 — Bonus (~20 min)

### 3.1 `db.execute(String sql)`

Parse and run one SQL statement, returning a `QueryResult`.

- `QueryResult.rows()` — `List<Map<String, Object>>` for `SELECT` (empty for others). Map keys are in projection order.
- `QueryResult.affectedRows()` — rows inserted / updated / deleted (0 for `SELECT` and DDL).
- Syntax errors → `SqlParseException` (extends `DatabaseException`), message should mention the position or token.
- All Part 1 / Part 2 errors still apply (e.g. `TableNotFoundException`).

### 3.2 Exact grammar

Keywords are **case-insensitive**. Identifiers are **case-sensitive** and match `[A-Za-z_][A-Za-z0-9_]*`.
Whitespace is free-form. One optional trailing `;`.

```ebnf
statement     = create_table | drop_table | create_index
              | insert | select | update | delete ;

create_table  = "CREATE" "TABLE" ident "(" column_def { "," column_def } ")" ;
column_def    = ident type { constraint } ;
type          = "INT" | "DOUBLE" | "STRING" | "BOOLEAN" ;
constraint    = "PRIMARY" "KEY" | "NOT" "NULL" | "UNIQUE" ;

drop_table    = "DROP" "TABLE" ident ;

create_index  = "CREATE" ( "HASH" | "BTREE" ) "INDEX" ident "ON" ident "(" ident ")" ;

insert        = "INSERT" "INTO" ident "(" ident { "," ident } ")"
                "VALUES" value_list { "," value_list } ;
value_list    = "(" literal { "," literal } ")" ;

select        = "SELECT" ( "*" | ident { "," ident } ) "FROM" ident
                [ "WHERE" expr ]
                [ "ORDER" "BY" order_item { "," order_item } ]
                [ "LIMIT" integer ] ;
order_item    = ident [ "ASC" | "DESC" ] ;

update        = "UPDATE" ident "SET" assignment { "," assignment } [ "WHERE" expr ] ;
assignment    = ident "=" literal ;

delete        = "DELETE" "FROM" ident [ "WHERE" expr ] ;

expr          = and_expr { "OR" and_expr } ;
and_expr      = primary { "AND" primary } ;
primary       = "(" expr ")"
              | ident comp_op literal
              | ident "IS" [ "NOT" ] "NULL" ;
comp_op       = "=" | "!=" | "<>" | "<" | "<=" | ">" | ">=" ;

literal       = integer | decimal | string | "TRUE" | "FALSE" | "NULL" ;
integer       = [ "-" ] digit { digit } ;
decimal       = [ "-" ] digit { digit } "." digit { digit } ;
string        = "'" { any char except "'" | "''" } "'" ;   (* '' is an escaped quote *)
```

Notes:

- `AND` binds tighter than `OR`: `a = 1 OR b = 2 AND c = 3` means `a = 1 OR (b = 2 AND c = 3)`.
- `col = NULL` is legal syntax but, per the NULL rules, matches nothing.
- An `INSERT` with several value lists is all-or-nothing.
- The number of literals in each value list must equal the number of columns, else `SqlParseException`.

---

## Required API (the tests call exactly these)

Everything not listed here — internal classes, storage layout, how you lock, how you parse — is yours to design.

```java
package org.sql;

// ---- Database ----
public class Database {
    public Database();
    public Table createTable(String name, List<Column> columns);  // TableAlreadyExistsException, SchemaException
    public Table getTable(String name);                            // TableNotFoundException
    public void dropTable(String name);                            // TableNotFoundException
    public QueryResult execute(String sql);                        // Part 3
}

// ---- Schema ----
public enum ColumnType { INT, DOUBLE, STRING, BOOLEAN }

public final class Column {
    public static Column of(String name, ColumnType type);
    public Column primaryKey();   // fluent, returns a Column with the flag set
    public Column notNull();
    public Column unique();
    public String name();
    public ColumnType type();
}

// ---- Table ----
public class Table {
    public String name();
    public List<Column> columns();

    public void insert(Map<String, Object> values);
    public List<Map<String, Object>> select(Query query);
    public int update(Map<String, Object> set, Condition where);   // where may be null
    public int delete(Condition where);                            // where may be null
    public int size();

    // Part 2
    public void createIndex(String indexName, String column, IndexType type);
    public String explain(Query query);
}

// ---- Query ----
public final class Query {
    public static Query selectAll();
    public static Query select(String... columns);
    public Query where(Condition condition);
    public Query orderBy(String column, SortOrder order);   // Part 2
    public Query limit(int n);                              // Part 2
}

public enum SortOrder { ASC, DESC }
public enum IndexType { HASH, BTREE }

// ---- Conditions ----
public interface Condition {
    static Condition eq(String column, Object value);
    static Condition neq(String column, Object value);
    static Condition gt(String column, Object value);
    static Condition gte(String column, Object value);
    static Condition lt(String column, Object value);
    static Condition lte(String column, Object value);
    static Condition isNull(String column);
    static Condition isNotNull(String column);
    static Condition and(Condition... children);
    static Condition or(Condition... children);
}

// ---- Results (Part 3) ----
public final class QueryResult {
    public List<Map<String, Object>> rows();
    public int affectedRows();
}

// ---- Exceptions (all extend DatabaseException extends RuntimeException) ----
DatabaseException, TableAlreadyExistsException, TableNotFoundException, ColumnNotFoundException,
TypeMismatchException, NotNullViolationException, UniqueViolationException, SchemaException,
SqlParseException
```

---

## Example driver program

```java
package org.sql;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();

        // ---------- Part 1 ----------
        Table users = db.createTable("users", List.of(
                Column.of("id", ColumnType.INT).primaryKey(),
                Column.of("name", ColumnType.STRING).notNull(),
                Column.of("email", ColumnType.STRING).unique(),
                Column.of("age", ColumnType.INT),
                Column.of("active", ColumnType.BOOLEAN)
        ));

        users.insert(Map.of("id", 1, "name", "Asha",  "email", "asha@x.com",  "age", 31, "active", true));
        users.insert(Map.of("id", 2, "name", "Ravi",  "email", "ravi@x.com",  "age", 24, "active", false));
        users.insert(Map.of("id", 3, "name", "Meera", "age", 29, "active", true));   // email = NULL
        users.insert(Map.of("id", 4, "name", "Kabir", "age", 40));                   // email, active = NULL

        System.out.println(users.select(Query.select("id", "name")
                .where(Condition.and(Condition.gt("age", 25), Condition.eq("active", true)))));
        // -> [{id=1, name=Asha}, {id=3, name=Meera}]   (any order)

        System.out.println(users.select(Query.select("id").where(Condition.eq("email", null))));
        // -> []   (comparison with NULL is false)

        System.out.println(users.select(Query.select("id").where(Condition.isNull("email"))));
        // -> [{id=3}, {id=4}]

        try {
            users.insert(Map.of("id", 1, "name", "Dup"));
        } catch (UniqueViolationException e) {
            System.out.println("rejected: " + e.getMessage());
        }

        try {
            users.insert(Map.of("id", 5, "name", "Bad", "age", "forty"));
        } catch (TypeMismatchException e) {
            System.out.println("rejected: " + e.getMessage());
        }

        System.out.println(users.update(Map.of("active", false), Condition.lt("age", 30)));  // -> 2
        System.out.println(users.delete(Condition.isNull("active")));                         // -> 1 (Kabir)

        // ---------- Part 2 ----------
        users.createIndex("idx_age", "age", IndexType.BTREE);
        Query older = Query.selectAll()
                .where(Condition.gte("age", 25))
                .orderBy("age", SortOrder.DESC)
                .limit(2);
        System.out.println(users.explain(older));  // -> INDEX_SCAN idx_age
        System.out.println(users.select(older));   // -> Asha (31), Meera (29)

        try {
            users.update(Map.of("email", "same@x.com"), null);   // would make 3 rows share an email
        } catch (UniqueViolationException e) {
            System.out.println("update rolled back, size=" + users.size()
                    + ", asha email=" + users.select(Query.select("email").where(Condition.eq("id", 1))));
            // -> asha email=[{email=asha@x.com}]
        }

        // ---------- Part 3 ----------
        db.execute("CREATE TABLE orders (id INT PRIMARY KEY, user_id INT NOT NULL, total DOUBLE)");
        db.execute("CREATE HASH INDEX idx_user ON orders (user_id)");
        QueryResult ins = db.execute(
                "INSERT INTO orders (id, user_id, total) VALUES (1, 1, 250.0), (2, 1, 99.5), (3, 2, NULL)");
        System.out.println(ins.affectedRows());   // -> 3

        QueryResult r = db.execute(
                "select id, total from orders where user_id = 1 and (total > 100 or total is null) order by id limit 5;");
        System.out.println(r.rows());             // -> [{id=1, total=250.0}]

        try {
            db.execute("SELEC * FROM orders");
        } catch (SqlParseException e) {
            System.out.println("parse error: " + e.getMessage());
        }
    }
}
```

---

## How you're judged

| # | Area                          | What a strong answer shows                                                                                   | Weight |
|---|-------------------------------|---------------------------------------------------------------------------------------------------------------|--------|
| 1 | **Correctness of the core**   | Part 1 fully works; NULL rules and constraint checks are exact; failed writes leave no trace.                 | High   |
| 2 | **Object-oriented design**    | Clear separation: schema, storage, conditions, indexes, query execution, parser. No god class.               | High   |
| 3 | **Extensibility**             | Adding a new type, operator or index kind touches one place (e.g. `Condition` as a polymorphic tree, `Index` interface). | Medium |
| 4 | **Data structures & complexity** | Right structure per job (`HashMap` vs `TreeMap`/`NavigableMap`); can state Big-O of lookups, inserts, range scans. | Medium |
| 5 | **Concurrency**               | Sensible lock granularity (per-table `ReadWriteLock` or better); no deadlocks; unique check + write is atomic. | Medium |
| 6 | **Error handling & atomicity**| Specific exception types with useful messages; validate-then-apply or undo log for all-or-nothing updates.   | Medium |
| 7 | **Code quality & communication** | Readable names, small methods, a few meaningful tests, and thinking out loud about trade-offs and scope.    | Medium |

---

## Follow-up questions

1. How would you add **transactions** (`BEGIN` / `COMMIT` / `ROLLBACK`) spanning multiple statements and tables? What isolation level would you pick and why?
2. Your `UNIQUE` check uses an index. What happens to correctness if two threads insert the same value at the same moment, and how does your locking prevent it?
3. How would you support **composite indexes** (e.g. `(last_name, first_name)`) and which queries could use them?
4. When there are two usable indexes for one `AND` condition, how would you choose between them? What statistics would a real planner keep?
5. How would you add **JOIN** (inner join on equality)? Compare nested-loop, hash join and sort-merge join.
6. How would you make the data **durable** across restarts? Explain a write-ahead log and how recovery works.
7. Per-table locks block all writers on a hot table. How would **MVCC** let readers never block writers?
8. Your B-tree index is probably a `TreeMap`. What would change if the data did not fit in memory, and why do real databases use B+ trees with large pages?
9. How would you add **aggregates** (`COUNT`, `SUM`, `GROUP BY`) and where would that live in your design?

---

## Hints

<details>
<summary>Hint 1 — Model the schema and validation in one place</summary>

Give `Table` (or a `Schema` class) a single `validateRow(Map<String,Object>)` that checks unknown columns,
types (with `Integer → Double` widening), and `NOT NULL`. Insert and update both call it, so the rules can't drift apart.
</details>

<details>
<summary>Hint 2 — Make Condition a small expression tree</summary>

`Condition` can be an interface with one method, `boolean matches(Row row)`. `Comparison`, `IsNull`, `And`, `Or`
are implementations. Put the "null means false" rule inside `Comparison` once, and every operator gets it for free.
The index planner can later `instanceof`-check (or pattern-match) the tree to find usable comparisons.
</details>

<details>
<summary>Hint 3 — Store rows by an internal id</summary>

Keep rows in a `Map<Long, Row>` keyed by an auto-incremented internal row id, not by the primary key.
Indexes then map `value → Set<Long rowId>`. Deletes and updates become simple, and a table without a primary key still works.
</details>

<details>
<summary>Hint 4 — HASH vs BTREE is HashMap vs TreeMap</summary>

`HashMap<Object, Set<Long>>` for HASH, `TreeMap<Comparable, Set<Long>>` for BTREE.
Range queries are `subMap` / `headMap` / `tailMap`. Remember a `TreeMap` can't hold a `null` key — keep null row ids in a separate set.
Normalise numbers (e.g. store all numerics as `Double` keys, or use one comparator) so `eq("age", 30)` and `eq("age", 30.0)` find the same entries.
</details>

<details>
<summary>Hint 5 — All-or-nothing update: validate first, then apply</summary>

Phase 1: compute the new version of every matching row, validate types / `NOT NULL`, and check uniqueness against
*(existing values − old values of the rows being changed) + new values*. Throw before touching anything.
Phase 2: only once everything passes, write rows and update indexes. Do both phases under the table's write lock.
</details>

<details>
<summary>Hint 6 — Start with one ReentrantReadWriteLock per table</summary>

`select` / `explain` take the read lock; `insert` / `update` / `delete` / `createIndex` take the write lock.
Use a `ConcurrentHashMap` for the table catalogue in `Database`. This gives correct, deadlock-free behaviour
(each operation locks one table) and is easy to explain; mention MVCC as the next step.
</details>

<details>
<summary>Hint 7 — Parser: tokenizer + recursive descent</summary>

First turn the string into tokens (keyword/identifier, number, string, symbol), handling `''` inside strings.
Then write one method per grammar rule: `parseStatement`, `parseSelect`, `parseExpr` (handles `OR`),
`parseAnd` (handles `AND`), `parsePrimary` (parentheses or a comparison). That function nesting gives
`AND` higher precedence automatically. Build the same `Condition` / `Query` objects Part 1 uses and call the Table API —
don't write a second execution engine.
</details>
