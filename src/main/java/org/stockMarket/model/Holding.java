package org.stockMarket.model;

public class Holding {
    long quantity;
    long averagePrice;   // average buy price, used for profit

    public void addShares(long boughtQuantity, long pricePerShare){
        long totalCost = averagePrice * quantity + pricePerShare * boughtQuantity;
        quantity += boughtQuantity;
        averagePrice = totalCost / quantity;
    }

    // Selling doesn't change the average buy price.
    public void removeShares(long soldQuantity){
        if(soldQuantity > quantity) throw new IllegalArgumentException("Not enough shares to sell");
        quantity -= soldQuantity;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getAveragePrice() {
        return averagePrice;
    }
}
