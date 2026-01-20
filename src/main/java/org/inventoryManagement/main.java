package org.inventoryManagement;

import org.inventoryManagement.model.Cart;
import org.inventoryManagement.model.Product;
import org.inventoryManagement.model.User;
import org.inventoryManagement.repository.InventoryRepository;
import org.inventoryManagement.service.InventoryService;

public class main {
    static void main(String[] args){
        Product product1 = new Product(1,"table", 1800.00);
        Product product2 = new Product(2,"chair", 1600.00);
        Product product3 = new Product(3,"mobile", 18008.00);
        Product product4 = new Product(4,"chocolate", 100.00);
        InventoryService inventoryService = new InventoryService(new InventoryRepository());
        inventoryService.addProduct(product1);
        inventoryService.addProduct(product2);
        inventoryService.addProduct(product3);
        inventoryService.addProduct(product4);

        inventoryService.addInventory(product1.getProductId(), 10);
        inventoryService.addInventory(product2.getProductId(), 20);
        inventoryService.addInventory(product3.getProductId(), 30);

        inventoryService.removeInventory(product1.getProductId(), 4);
        User user = new User(1, "manav");
        Cart cart = new Cart();
        cart.addItem(1, 2);
        cart.addItem(2,5);

    }
}
