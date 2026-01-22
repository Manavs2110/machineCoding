package org.FoodOrderingSystem.model;

public class RestaurantFoodItem {
    FoodItem foodItem;
    Integer quantityLeft;

    public RestaurantFoodItem(FoodItem foodItem, Integer quantityLeft) {
        this.foodItem = foodItem;
        this.quantityLeft = quantityLeft;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public Integer getQuantityLeft() {
        return quantityLeft;
    }

    public void setQuantityLeft(Integer quantityLeft) {
        this.quantityLeft = quantityLeft;
    }
}
