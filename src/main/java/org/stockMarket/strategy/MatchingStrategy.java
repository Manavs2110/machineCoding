package org.stockMarket.strategy;

import org.stockMarket.model.Order;
import org.stockMarket.model.Trade;

import java.util.List;
import java.util.Queue;

// Decides how an incoming order's quantity is shared among the resting orders at ONE price level.
// Choosing the price level (best price first) is not the strategy's job; OrderService does that.
public interface MatchingStrategy {
    List<Trade> fillAtPriceLevel(Order incomingOrder, Queue<Order> ordersAtPrice, long price);
}
