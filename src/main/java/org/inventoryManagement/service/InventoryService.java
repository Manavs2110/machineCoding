package org.inventoryManagement.service;

import org.inventoryManagement.model.CartItem;
import org.inventoryManagement.model.InventoryItem;
import org.inventoryManagement.model.Product;
import org.inventoryManagement.repository.InventoryRepository;

import java.util.List;

public class InventoryService {
    InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    public void addProduct(Product product){
        inventoryRepository.addProduct(product);
    }
    public void addInventory(int productId, int quantity){
        inventoryRepository.addInventory(productId,quantity);
    }

    public void removeInventory(int productId, int quantity) {
        inventoryRepository.removeInventory(productId,quantity);
    }

    public int getQuantity(int productId){
        return inventoryRepository.getQuantity( productId);
    }
    public InventoryItem getItem(int productId){
        return inventoryRepository.getItem(productId);
    }
}
