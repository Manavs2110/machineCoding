package org.inventoryManagement.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Cart {
    Map<Integer, CartItem> cartItems;

    public Map<Integer, CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Map<Integer, CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Cart() {
        this.cartItems = new HashMap<>();
    }


    public void addItem(int productId, int quantity){
        cartItems.putIfAbsent(productId, new CartItem(0, productId));
        cartItems.get(productId).addQuantity(quantity);
    }
    public void removeItem(int productId, int quantity){
        if(!cartItems.containsKey(productId)) return;
        CartItem cartItem = cartItems.get(productId);
        if(cartItem.getQuantity() <= quantity) cartItems.remove(productId);
        else cartItem.addQuantity(-1*quantity);
    }
    public void clear() {
        cartItems.clear();
    }
}
