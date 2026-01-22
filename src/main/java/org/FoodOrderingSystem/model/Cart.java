package org.FoodOrderingSystem.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    List<CartFoodItem> cartFoodItems;


    public Cart() {
        this.cartFoodItems = new ArrayList<>();
    }

    public List<CartFoodItem> getCartFoodItems() {
        return cartFoodItems;
    }

    public void setCartFoodItems(List<CartFoodItem> cartFoodItems) {
        this.cartFoodItems = cartFoodItems;
    }
    public void addCartItem(CartFoodItem cartFoodItem){
        cartFoodItems.add(cartFoodItem);
    }
}
