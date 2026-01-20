package org.inventoryManagement.model;

public class Bill {
    Cart cart;
    User user;
    double totalAmount;

    public Bill(Cart cart, User user, double totalAmount) {
        this.cart = cart;
        this.user = user;
        this.totalAmount = totalAmount;
    }
}
