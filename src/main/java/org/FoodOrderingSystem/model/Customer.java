package org.FoodOrderingSystem.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    String id;
    String name;
    Location location;
    Cart cart;
    List<Bill> bills;
    public Customer(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        bills = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }
    public void addBill(Bill bill){
        bills.add(bill);
    }

    public List<Bill> getBills() {
        return bills;
    }
    public void clearCart(){
        cart = null;
    }
}
