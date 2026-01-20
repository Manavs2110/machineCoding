package org.inventoryManagement.service;

import org.inventoryManagement.model.*;
import org.inventoryManagement.repository.InventoryRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CheckoutService {

    InventoryService inventoryService;
    Cart cart;
    User user;

    public CheckoutService(InventoryService inventoryService, Cart cart, User user) {
        this.inventoryService = inventoryService;
        this.cart = cart;
        this.user = user;
    }

    public Bill checkout(){
        List<CartItem> cartItems= cart.getCartItems().values().stream().toList();
        List<InventoryItem> inventoryItems = cartItems.stream().map(cartItem -> inventoryService.getItem(cartItem.getProductId())).distinct().sorted(Comparator.comparingInt(inv -> inv.getProduct().getProductId())).toList();
        inventoryItems.forEach(InventoryItem::block);
        Bill bill = null;
        try{

            for(CartItem cartItem:cartItems){
                if(!inventoryService.getItem(cartItem.getProductId()).hasStock(cartItem.getQuantity())){
                    throw new RuntimeException("Out of Stock");
                }
            }
            double total = 0;
            for (CartItem item : cartItems) {

                InventoryItem invItem =
                        inventoryService.getItem(
                                item.getProductId());
                total+=item.getQuantity()*invItem.getProduct().getPrice();
                invItem.removeQuantity(item.getQuantity());
            }
            bill = generateBill(cart, user, total);
            cart.clear();
        }finally {
            inventoryItems.forEach(InventoryItem::unblock);
        }
        return bill;
    }

    private Bill generateBill(Cart cart, User user, double total){
        return new Bill(cart,user,total);
    }

}
