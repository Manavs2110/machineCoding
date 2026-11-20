package org.stockMarket.model;

public class Order {
    private static long nextSequence = 0; // arrival counter, used for time priority

    String orderId;
    long userId;
    String stockSymbol;
    OrderSide side;
    long pricePerShare;
    long quantity;      // shares still waiting to be matched
    long createdAt;
    OrderStatus orderStatus;

    public Order(String orderId, long userId, String stockSymbol, OrderSide side, long pricePerShare, long quantity) {
        this.orderId = orderId;
        this.userId = userId;
        this.stockSymbol = stockSymbol;
        this.side = side;
        this.pricePerShare = pricePerShare;
        this.quantity = quantity;
        this.createdAt = nextSequence++;
        this.orderStatus = OrderStatus.OPEN;
    }

    public void reduceQuantity(long tradedQuantity){
        quantity -= tradedQuantity;
        if(quantity == 0) orderStatus = OrderStatus.FILLED;
    }

    public boolean isFilled(){
        return quantity == 0;
    }

    public void cancel(){
        orderStatus = OrderStatus.CANCELLED;
    }

    public String getOrderId() {
        return orderId;
    }

    public long getUserId() {
        return userId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public OrderSide getSide() {
        return side;
    }

    public long getPricePerShare() {
        return pricePerShare;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    @Override
    public String toString() {
        return orderId + " " + side + " " + quantity + " @ " + pricePerShare;
    }
}
