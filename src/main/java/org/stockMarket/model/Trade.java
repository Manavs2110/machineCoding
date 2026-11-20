package org.stockMarket.model;

public class Trade {
    String buyOrderId;
    String sellOrderId;
    long pricePerShare;
    long quantity;
    String stockSymbol;

    public Trade(String buyOrderId, String sellOrderId, long pricePerShare, long quantity, String stockSymbol) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.pricePerShare = pricePerShare;
        this.quantity = quantity;
        this.stockSymbol = stockSymbol;
    }

    // Works out which order is the buyer and which is the seller.
    public static Trade between(Order incomingOrder, Order restingOrder, long quantity, long pricePerShare){
        Order buyOrder = incomingOrder.getSide() == OrderSide.BUY ? incomingOrder : restingOrder;
        Order sellOrder = incomingOrder.getSide() == OrderSide.BUY ? restingOrder : incomingOrder;
        return new Trade(buyOrder.getOrderId(), sellOrder.getOrderId(), pricePerShare, quantity, incomingOrder.getStockSymbol());
    }

    public String getBuyOrderId() {
        return buyOrderId;
    }

    public String getSellOrderId() {
        return sellOrderId;
    }

    public long getPricePerShare() {
        return pricePerShare;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    @Override
    public String toString() {
        return buyOrderId + "/" + sellOrderId + " " + quantity + " @ " + pricePerShare;
    }
}
