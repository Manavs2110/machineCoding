package org.stockMarket.strategy;

import org.stockMarket.model.Order;
import org.stockMarket.model.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

// Every order at the price gets a share proportional to its size.
// Shares are rounded down; the leftover is handed out one share at a time, oldest order first.
public class ProRataMatchingStrategy implements MatchingStrategy {

    @Override
    public List<Trade> fillAtPriceLevel(Order incomingOrder, Queue<Order> ordersAtPrice, long price) {
        List<Order> restingOrders = new ArrayList<>(ordersAtPrice); // oldest first
        long totalRestingQuantity = 0;
        for(Order restingOrder : restingOrders) totalRestingQuantity += restingOrder.getQuantity();

        long quantityToFill = Math.min(incomingOrder.getQuantity(), totalRestingQuantity);
        long[] allocations = new long[restingOrders.size()];
        long allocatedQuantity = 0;
        for(int i = 0; i < restingOrders.size(); i++){
            allocations[i] = quantityToFill * restingOrders.get(i).getQuantity() / totalRestingQuantity;
            allocatedQuantity += allocations[i];
        }

        // Rounding leaves fewer leftover shares than there are orders, so one pass is enough.
        long leftoverQuantity = quantityToFill - allocatedQuantity;
        for(int i = 0; i < restingOrders.size() && leftoverQuantity > 0; i++){
            if(allocations[i] < restingOrders.get(i).getQuantity()){
                allocations[i]++;
                leftoverQuantity--;
            }
        }

        List<Trade> trades = new ArrayList<>();
        for(int i = 0; i < restingOrders.size(); i++){
            if(allocations[i] == 0) continue;
            Order restingOrder = restingOrders.get(i);
            trades.add(Trade.between(incomingOrder, restingOrder, allocations[i], price));
            incomingOrder.reduceQuantity(allocations[i]);
            restingOrder.reduceQuantity(allocations[i]);
        }
        ordersAtPrice.removeIf(Order::isFilled);
        return trades;
    }
}
