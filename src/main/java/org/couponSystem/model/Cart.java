package org.couponSystem.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    String userId;
    String restaurantId;
    List<CartItem> items = new ArrayList<>();

    public Cart(String userId, String restaurantId) {
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public void addItem(CartItem item) {
        items.add(item);
    }

    public long getTotal() {
        long total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public String getUserId() {
        return userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }
}
