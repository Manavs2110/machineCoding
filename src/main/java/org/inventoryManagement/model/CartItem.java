package org.inventoryManagement.model;

public class CartItem {
    int quantity=0;
    int productId;

    public CartItem(int quantity, int productId) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void addQuantity(int qty){
        this.quantity+=qty;
    }
}
