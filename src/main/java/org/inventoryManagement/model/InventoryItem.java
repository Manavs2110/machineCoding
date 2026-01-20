package org.inventoryManagement.model;

import java.util.concurrent.locks.ReentrantLock;

public class InventoryItem {
    int quantity;
    Product product;
    ReentrantLock lock = new ReentrantLock();
    public InventoryItem(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void block(){
        lock.lock();
    }
    public void unblock(){
        lock.unlock();
    }
    public boolean hasStock(int qty){
        return this.getQuantity()>qty;
    }
    public void addQuantity(int quantity){
        this.quantity += quantity;
    }

    public void removeQuantity(int quantity){
        this.quantity = Math.max(this.quantity-quantity, 0);
    }
}
