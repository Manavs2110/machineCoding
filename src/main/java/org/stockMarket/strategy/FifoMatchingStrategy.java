package org.stockMarket.strategy;

import org.stockMarket.model.Order;
import org.stockMarket.model.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

// The earliest order at the price is filled completely before the next one gets anything.
public class FifoMatchingStrategy implements MatchingStrategy {

    @Override
    public List<Trade> fillAtPriceLevel(Order incomingOrder, Queue<Order> ordersAtPrice, long price) {
        List<Trade> trades = new ArrayList<>();
        while(incomingOrder.getQuantity() > 0 && !ordersAtPrice.isEmpty()){
            Order restingOrder = ordersAtPrice.peek();
            long tradedQuantity = Math.min(incomingOrder.getQuantity(), restingOrder.getQuantity());
            trades.add(Trade.between(incomingOrder, restingOrder, tradedQuantity, price));
            incomingOrder.reduceQuantity(tradedQuantity);
            restingOrder.reduceQuantity(tradedQuantity);
            if(restingOrder.isFilled()) ordersAtPrice.poll();
        }
        return trades;
    }
}
