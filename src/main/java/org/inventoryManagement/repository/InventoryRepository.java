package org.inventoryManagement.repository;

import org.inventoryManagement.model.CartItem;
import org.inventoryManagement.model.InventoryItem;
import org.inventoryManagement.model.Product;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryRepository {
    Map<Integer, InventoryItem> inventory;

    public InventoryRepository() {
        inventory = new ConcurrentHashMap<>();
    }

    public void addProduct(Product product){
        inventory.put(product.getProductId(), new InventoryItem(0, product));
    }

    public void addInventory(int productId, int quantity){
        InventoryItem item = inventory.get(productId);
        item.block();
        try {
            item.addQuantity(quantity);
        } finally {
            item.unblock();
        }
    }

    public void removeInventory(int productId, int quantity){
        InventoryItem item = inventory.get(productId);
        item.block();
        try {
            item.removeQuantity(quantity);
        } finally {
            item.unblock();
        }
    }

    public int getQuantity(int productId){
        return inventory.get(productId).getQuantity();
    }

    public InventoryItem getItem(int productId){
        return inventory.get(productId);
    }

}
