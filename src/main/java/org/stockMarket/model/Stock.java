package org.stockMarket.model;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Queue;
import java.util.TreeMap;

public class Stock {
    String stockSymbol;
    String stockName;
    long lastTradePrice;
    // price -> resting orders at that price, oldest first.
    // Buy map is reversed so on both sides the best price is firstEntry().
    TreeMap<Long, Queue<Order>> buyOrdersByPrice = new TreeMap<>(Comparator.reverseOrder()); // highest first
    TreeMap<Long, Queue<Order>> sellOrdersByPrice = new TreeMap<>();                         // lowest first

    public Stock(String stockSymbol, String stockName, long lastTradePrice) {
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.lastTradePrice = lastTradePrice;
    }

    // Puts an unfilled order at the back of its price level, on its own side of the book.
    public void addRestingOrder(Order order){
        getOwnSideOrders(order.getSide())
                .computeIfAbsent(order.getPricePerShare(), price -> new ArrayDeque<>())
                .add(order);
    }

    // The side an incoming order trades against.
    public TreeMap<Long, Queue<Order>> getOppositeOrders(OrderSide incomingSide){
        return incomingSide == OrderSide.BUY ? sellOrdersByPrice : buyOrdersByPrice;
    }

    public boolean removeOrder(Order order){
        TreeMap<Long, Queue<Order>> ordersByPrice = getOwnSideOrders(order.getSide());
        Queue<Order> ordersAtPrice = ordersByPrice.get(order.getPricePerShare());
        if(ordersAtPrice == null || !ordersAtPrice.remove(order)) return false;
        if(ordersAtPrice.isEmpty()) ordersByPrice.remove(order.getPricePerShare());
        return true;
    }

    private TreeMap<Long, Queue<Order>> getOwnSideOrders(OrderSide side){
        return side == OrderSide.BUY ? buyOrdersByPrice : sellOrdersByPrice;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public long getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(long lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }

    public TreeMap<Long, Queue<Order>> getBuyOrdersByPrice() {
        return buyOrdersByPrice;
    }

    public TreeMap<Long, Queue<Order>> getSellOrdersByPrice() {
        return sellOrdersByPrice;
    }
}
