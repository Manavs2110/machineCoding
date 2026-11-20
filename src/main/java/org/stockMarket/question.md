# Stock Order Matching

> **Asked at:** SDE-2, Feb 2024, "DSA and problem solving" round
> **Language:** Java 17+ · **Package:** `org.stockMarket` · **Time:** ~60 min (Part 1 ≈ 30, Part 2 ≈ 20, Part 3 ≈ 10)
> No external libraries. Everything lives in memory.

## The problem (as the interviewer says it)

> "Write functions to add a buy bid and a sell bid for a stock. Whenever a buy and a sell can trade,
> match them. Then let me switch the rule that decides *which* bid gets matched first."

**Why the real candidate was rejected:** the logic worked, but the function and variable names were confusing
(`process()`, `list1`, `o`, `q`, `flag`, `temp`). In this round, **naming counts as much as correctness.**
See [Naming rules](#naming-rules-this-is-what-failed-the-real-candidate).

Finish **Part 1 completely** before you touch Part 2.

---

## Part 1: Must have (~30 min)

### 1.1 Orders

An order has:

| Field           | Type     | Notes                                                   |
|-----------------|----------|---------------------------------------------------------|
| `orderId`       | `String` | Given by the caller. Unique.                            |
| `userId`        | `String` |                                                         |
| `stockSymbol`   | `String` | e.g. `"INFY"`. Each stock has its own separate order book. |
| `side`          | `OrderSide` | `BUY` or `SELL`                                      |
| `pricePerShare` | `long`   | In paise or cents. **Don't use `double` for money.**    |
| `quantity`      | `int`    | Shares still waiting to be matched. Goes down on partial fills. |
| `createdAt`     | `long`   | Arrival order. A simple counter is fine (no clock needed). |

Invalid input (quantity ≤ 0, price ≤ 0, duplicate `orderId`) → `IllegalArgumentException`.

### 1.2 When can two orders trade?

A buy and a sell **for the same stock** can trade when `buy.pricePerShare >= sell.pricePerShare`.

### 1.3 Matching rules

Matching happens **immediately** when an order is added:

1. A new **buy** order is matched against resting **sell** orders, **lowest sell price first**.
2. A new **sell** order is matched against resting **buy** orders, **highest buy price first**.
3. Among orders at the **same price**, the earlier order goes first (**price-time priority**, also called FIFO).
4. **The trade price is the resting order's price**, i.e. the order that was already in the book.
5. **Partial fills are allowed.** Trade `min(incomingQty, restingQty)`, reduce both, and keep going until the
   incoming order is filled or nothing else can trade.
6. A resting order that reaches quantity 0 is removed from the book.
7. Whatever is left of the incoming order **rests in the book** at its own price.

Each match produces a `Trade`:

```text
Trade(buyOrderId, sellOrderId, stockSymbol, pricePerShare, quantity)
```

### 1.4 Walkthrough (price-time priority)

```text
addSellOrder  S1  alice  INFY  10 @ 100     -> []                     (rests)
addSellOrder  S2  bob    INFY   5 @  99     -> []                     (rests)
addSellOrder  S3  carol  INFY  20 @ 100     -> []                     (rests)

addBuyOrder   B1  dave   INFY  12 @ 100     -> [B1/S2 5 @ 99, B1/S1 7 @ 100]
    S2 is the cheapest (99), so it fills first: 5 shares.
    7 shares left. At 100, S1 arrived before S3, so S1 gets them. S1 now has 3 left.

addBuyOrder   B2  eve    INFY   4 @  98     -> []                     (cheapest sell is 100, can't trade)

addSellOrder  S4  frank  INFY   6 @  97     -> [B2/S4 4 @ 98]
    The best buy is B2 @ 98 (>= 97), so 4 shares trade at 98, the resting order's price.
    2 shares of S4 are left and rest as a sell @ 97.

Book now:
    BUY : (empty)
    SELL: S4 2 @ 97, S1 3 @ 100, S3 20 @ 100
```

---

## Part 2: Should have (~20 min)

### 2.1 Pluggable matching strategies

The **price rule never changes**: the best price always goes first. The strategy only decides the
**tie-break between orders at the same price**. The caller picks the strategy when creating the exchange
and can switch it later.

| Strategy              | Tie-break at the same price                                  |
|-----------------------|--------------------------------------------------------------|
| `PriceTimeStrategy`   | Earlier order first. This is Part 1 and the default.         |
| `PriceSizeStrategy`   | **Larger remaining quantity first**. If the quantities are also equal, earlier first. |

Adding a new strategy must not require editing the matching loop.

Same input as 1.4 but with `PriceSizeStrategy`:

```text
addBuyOrder   B1  dave   INFY  12 @ 100     -> [B1/S2 5 @ 99, B1/S3 7 @ 100]
    S2 still goes first (better price). At 100, S3 (20 shares) beats S1 (10 shares).
```

### 2.2 Cancel

`cancelOrder(orderId)` removes a resting order and returns `true`. It returns `false` if the order
doesn't exist or is already fully filled.

### 2.3 Read the book

`getOpenBuyOrders(symbol)` / `getOpenSellOrders(symbol)` return the resting orders in the order they
would be matched (best first, using the current strategy).

---

## Part 3: Bonus (~10 min, pick one)

1. **Pro-rata strategy.** At the best price level, split the incoming quantity across all resting orders
   in proportion to their size. Round each share down, then hand out the leftover shares one at a time
   in time order.
   ```text
   resting sells @ 100: S1 30 shares (earlier), S2 10 shares
   buy B1 10 @ 100  -> S1 gets floor(10*30/40)=7, S2 gets floor(10*10/40)=2, the leftover 1 goes to S1
                    -> [B1/S1 8 @ 100, B1/S2 2 @ 100]
   ```
   This one does **not** fit a simple comparator. Notice how your design has to change.
2. **Market orders.** No price; trade at whatever is available. Any unfilled remainder is cancelled, not rested.
3. **Thread safety.** Many threads place orders at once. Orders for different stocks shouldn't block each other.

---

## Required API

```java
package org.stockMarket;

public enum OrderSide { BUY, SELL }

public class StockExchange {
    public StockExchange(MatchingStrategy matchingStrategy);
    public void setMatchingStrategy(MatchingStrategy matchingStrategy);

    public List<Trade> addBuyOrder (String orderId, String userId, String stockSymbol, int quantity, long pricePerShare);
    public List<Trade> addSellOrder(String orderId, String userId, String stockSymbol, int quantity, long pricePerShare);
    public boolean cancelOrder(String orderId);

    public List<Order> getOpenBuyOrders(String stockSymbol);
    public List<Order> getOpenSellOrders(String stockSymbol);
}

public interface MatchingStrategy { /* you design this */ }
public class PriceTimeStrategy implements MatchingStrategy { }
public class PriceSizeStrategy implements MatchingStrategy { }

public class Trade {
    public String getBuyOrderId();
    public String getSellOrderId();
    public String getStockSymbol();
    public long getPricePerShare();
    public int getQuantity();
}
```

Everything else (`Order`, `OrderBook`, how you store orders) is up to you.

---

## Example driver program

```java
package org.stockMarket;

public class Main {
    public static void main(String[] args) {
        StockExchange exchange = new StockExchange(new PriceTimeStrategy());

        exchange.addSellOrder("S1", "alice", "INFY", 10, 100);
        exchange.addSellOrder("S2", "bob",   "INFY",  5,  99);
        exchange.addSellOrder("S3", "carol", "INFY", 20, 100);

        System.out.println(exchange.addBuyOrder("B1", "dave", "INFY", 12, 100));
        // -> [B1/S2 5 @ 99, B1/S1 7 @ 100]

        System.out.println(exchange.addBuyOrder("B2", "eve", "INFY", 4, 98));
        // -> []

        System.out.println(exchange.addSellOrder("S4", "frank", "INFY", 6, 97));
        // -> [B2/S4 4 @ 98]

        System.out.println(exchange.getOpenSellOrders("INFY"));
        // -> S4 2 @ 97, S1 3 @ 100, S3 20 @ 100

        System.out.println(exchange.cancelOrder("S3"));   // -> true
        System.out.println(exchange.cancelOrder("S3"));   // -> false

        // A different stock has its own book
        System.out.println(exchange.addBuyOrder("B3", "dave", "TCS", 5, 100));
        // -> []   (no INFY sells are used)
    }
}
```

---

## Naming rules (this is what failed the real candidate)

| Bad                          | Good                                         | Why                                  |
|------------------------------|----------------------------------------------|--------------------------------------|
| `add(o)`, `process()`        | `addBuyOrder(...)`, `matchIncomingOrder(...)` | Say what it does                     |
| `list1`, `list2`, `pq`       | `buyOrders`, `sellOrders`                     | Say what's inside                    |
| `o1`, `o2`                   | `incomingOrder`, `restingOrder`               | The two sides of a match have different roles |
| `q`, `p`, `amt`              | `quantity`, `pricePerShare`                   | No abbreviations                     |
| `flag`, `done`               | `canTrade`, `isFilled()`                      | Booleans read as a yes/no question   |
| `temp`, `x`                  | `tradedQuantity`                              | Name the meaning, not the role in the code |
| `Strategy1`, `Strategy2`     | `PriceTimeStrategy`, `PriceSizeStrategy`      | Name the rule                        |
| `price` (is it total or per share?) | `pricePerShare`                       | Remove ambiguity, especially with money |

Also: keep methods short (under ~20 lines), use one word per concept (don't mix `bid`, `order` and `request`
for the same thing), and say your names out loud as you type them.

---

## How you're judged

| # | Area                       | What a strong answer shows                                                       | Weight |
|---|----------------------------|-----------------------------------------------------------------------------------|--------|
| 1 | **Correctness**            | Price priority, the tie-break, partial fills, trade at the resting price, the remainder rests. | High |
| 2 | **Naming & readability**   | Any reader understands each method from its name; no `temp`/`flag`/`list1`.       | High   |
| 3 | **Data structures**        | A heap or sorted structure per side per stock; can state the Big-O of add, match and cancel. | High |
| 4 | **Extensibility**          | A new strategy is a new class; the matching loop is untouched.                   | Medium |
| 5 | **Edge cases**             | Exact price equality, an order filling many resting orders, cancelling a filled order, separate stocks. | Medium |
| 6 | **Communication**          | Asks clarifying questions first (trade price? partial fills?) and states assumptions. | Medium |

---

## Clarifying questions to ask before coding

1. At what price does a trade happen: the buyer's, the seller's, or the resting order's? *(Resting.)*
2. Are partial fills allowed? *(Yes.)*
3. Does an unmatched order stay in the book or get dropped? *(It stays.)*
4. Is there one order book per stock? *(Yes.)*
5. Can a user's buy match their own sell? *(Allow it in the base version; see the follow-ups.)*

---

## Follow-up questions

1. What is the time complexity of `addBuyOrder`, `cancelOrder` and `getOpenBuyOrders` in your design? Can cancel be faster than O(n)?
2. Your strategy is probably a `Comparator`. Why doesn't pro-rata fit that shape, and how would you change the interface?
3. How do you stop **self-trading** (a user's buy matching their own sell)?
4. How would you support **modify order** (change quantity or price)? Should a change keep the order's place in the time queue?
5. How would you make it thread-safe? A lock per stock, or a single-threaded matching engine per stock with a queue in front?
6. How would you send trades to users in real time (think back to the Kafka/pub-sub design)?
7. How would you recover the order book after a crash?

---

## Hints

<details>
<summary>Hint 1: one order book per stock</summary>

`Map<String, OrderBook> orderBooksBySymbol`. Each `OrderBook` has a `buyOrders` and a `sellOrders` collection.
Keep a `Map<String, Order> ordersById` for cancel.
</details>

<details>
<summary>Hint 2: a strategy is just a comparator for each side</summary>

```java
public interface MatchingStrategy {
    Comparator<Order> buyOrderPriority();   // highest price first, then the tie-break
    Comparator<Order> sellOrderPriority();  // lowest price first, then the tie-break
}
```
Each `OrderBook` builds `PriorityQueue<Order> buyOrders = new PriorityQueue<>(strategy.buyOrderPriority())`.
The matching loop only ever calls `peek()` and `poll()`, so it never knows which strategy it's using.
</details>

<details>
<summary>Hint 3: the matching loop</summary>

```java
while (incomingOrder.getQuantity() > 0 && !oppositeOrders.isEmpty()
        && canTrade(incomingOrder, oppositeOrders.peek())) {
    Order restingOrder = oppositeOrders.peek();
    int tradedQuantity = Math.min(incomingOrder.getQuantity(), restingOrder.getQuantity());
    trades.add(createTrade(incomingOrder, restingOrder, tradedQuantity));   // at restingOrder's price
    incomingOrder.reduceQuantity(tradedQuantity);
    restingOrder.reduceQuantity(tradedQuantity);
    if (restingOrder.isFilled()) oppositeOrders.poll();
}
if (incomingOrder.getQuantity() > 0) sameSideOrders.add(incomingOrder);
```
Write it **once** and use it for both buy and sell, passing in which side is "opposite".
</details>

<details>
<summary>Hint 4: careful with PriceSizeStrategy and a PriorityQueue</summary>

With size priority, a resting order's quantity drops after a partial fill, which changes its priority.
A `PriorityQueue` doesn't re-sort an element whose fields change, so you must `poll()` it, update it, and `add()` it back.
(With price-time this doesn't matter, because price and `createdAt` never change.)
</details>

<details>
<summary>Hint 5: cancel</summary>

The easy version is `PriorityQueue.remove(order)`, which is O(n). That's fine to start with. The faster version is
**lazy deletion**: mark the order cancelled in O(1) and skip cancelled orders whenever you `peek()`.
</details>

<details>
<summary>Hint 6: switching strategies at runtime</summary>

The comparator is baked into each `PriorityQueue`, so `setMatchingStrategy` has to rebuild the queues:
make new queues with the new comparators and `addAll` the old contents.
</details>
