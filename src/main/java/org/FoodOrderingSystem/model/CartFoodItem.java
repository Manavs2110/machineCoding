package org.FoodOrderingSystem.model;

public class CartFoodItem {
    FoodItem foodItem;
    Integer quantity;

    public CartFoodItem(FoodItem foodItem, Integer quantity) {
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
