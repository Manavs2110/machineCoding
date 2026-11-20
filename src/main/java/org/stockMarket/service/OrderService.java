package org.stockMarket.service;

import org.stockMarket.model.Order;
import org.stockMarket.model.OrderSide;
import org.stockMarket.model.OrderStatus;
import org.stockMarket.model.Stock;
import org.stockMarket.model.Trade;
import org.stockMarket.strategy.MatchingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

public class OrderService {
    Map<String, Stock> stocksBySymbol = new HashMap<>();
    Map<String, Order> ordersById = new HashMap<>();
    MatchingStrategy matchingStrategy;

    public OrderService(MatchingStrategy matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    public void setMatchingStrategy(MatchingStrategy matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    public void addStock(String stockSymbol, String stockName, long listingPrice){
        if(stocksBySymbol.containsKey(stockSymbol)) throw new IllegalArgumentException("Stock " + stockSymbol + " already exists");
        stocksBySymbol.put(stockSymbol, new Stock(stockSymbol, stockName, listingPrice));
    }

    public List<Trade> addBuyOrder(String orderId, long userId, String stockSymbol, long quantity, long pricePerShare){
        return placeOrder(new Order(orderId, userId, stockSymbol, OrderSide.BUY, pricePerShare, quantity));
    }

    public List<Trade> addSellOrder(String orderId, long userId, String stockSymbol, long quantity, long pricePerShare){
        return placeOrder(new Order(orderId, userId, stockSymbol, OrderSide.SELL, pricePerShare, quantity));
    }

    public boolean cancelOrder(String orderId){
        Order order = ordersById.get(orderId);
        if(order == null || order.getOrderStatus() != OrderStatus.OPEN) return false;
        getStock(order.getStockSymbol()).removeOrder(order);
        order.cancel();
        return true;
    }

    // Resting orders in the order they would be matched: best price first, oldest first within a price.
    public List<Order> getOpenBuyOrders(String stockSymbol){
        return flatten(getStock(stockSymbol).getBuyOrdersByPrice());
    }

    public List<Order> getOpenSellOrders(String stockSymbol){
        return flatten(getStock(stockSymbol).getSellOrdersByPrice());
    }

    public long getLastTradePrice(String stockSymbol){
        return getStock(stockSymbol).getLastTradePrice();
    }

    private List<Trade> placeOrder(Order incomingOrder){
        if(incomingOrder.getQuantity() <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if(incomingOrder.getPricePerShare() <= 0) throw new IllegalArgumentException("Price must be positive");
        if(ordersById.containsKey(incomingOrder.getOrderId())) throw new IllegalArgumentException("Order " + incomingOrder.getOrderId() + " already exists");
        Stock stock = getStock(incomingOrder.getStockSymbol());
        ordersById.put(incomingOrder.getOrderId(), incomingOrder);

        List<Trade> trades = matchIncomingOrder(stock, incomingOrder);
        if(incomingOrder.getQuantity() > 0) stock.addRestingOrder(incomingOrder);
        return trades;
    }

    // Walks the opposite side from the best price inward until the order is filled or prices stop crossing.
    private List<Trade> matchIncomingOrder(Stock stock, Order incomingOrder){
        List<Trade> trades = new ArrayList<>();
        TreeMap<Long, Queue<Order>> oppositeOrdersByPrice = stock.getOppositeOrders(incomingOrder.getSide());

        while(incomingOrder.getQuantity() > 0 && !oppositeOrdersByPrice.isEmpty()){
            Map.Entry<Long, Queue<Order>> bestPriceLevel = oppositeOrdersByPrice.firstEntry();
            long bestPrice = bestPriceLevel.getKey();
            if(!canTrade(incomingOrder, bestPrice)) break;

            trades.addAll(matchingStrategy.fillAtPriceLevel(incomingOrder, bestPriceLevel.getValue(), bestPrice));
            stock.setLastTradePrice(bestPrice);
            if(bestPriceLevel.getValue().isEmpty()) oppositeOrdersByPrice.pollFirstEntry();
        }
        return trades;
    }

    private boolean canTrade(Order incomingOrder, long restingPrice){
        return incomingOrder.getSide() == OrderSide.BUY
                ? incomingOrder.getPricePerShare() >= restingPrice
                : incomingOrder.getPricePerShare() <= restingPrice;
    }

    private Stock getStock(String stockSymbol){
        Stock stock = stocksBySymbol.get(stockSymbol);
        if(stock == null) throw new IllegalArgumentException("Stock " + stockSymbol + " does not exist");
        return stock;
    }

    private List<Order> flatten(TreeMap<Long, Queue<Order>> ordersByPrice){
        List<Order> orders = new ArrayList<>();
        for(Queue<Order> ordersAtPrice : ordersByPrice.values()) orders.addAll(ordersAtPrice);
        return orders;
    }
}
