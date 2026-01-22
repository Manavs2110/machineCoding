package org.FoodOrderingSystem.model;

public class Bill {
    Cart cart;
    String customerId;
    String restaurantId;
    double total;
    public Bill(Cart cart, String customer, String restaurant, double total) {
        this.cart = cart;
        this.customerId = customer;
        this.restaurantId = restaurant;
        this.total = total;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
