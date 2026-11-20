package org.couponSystem.model;

public class CartItem {
    String name;
    long price;      // per unit, in rupees
    int quantity;

    public CartItem(String name, long price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public long getTotalPrice() {
        return price * quantity;
    }

    public String getName() {
        return name;
    }
}
