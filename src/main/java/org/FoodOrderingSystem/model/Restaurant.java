package org.FoodOrderingSystem.model;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    String restaurantId;
    String RestaurantName;
    Location location;
    List<RestaurantFoodItem> restaurantFoodItemList;

    public Restaurant(String restaurantId, Location location, String restaurantName) {
        this.restaurantId = restaurantId;
        this.restaurantFoodItemList = restaurantFoodItemList;
        this.location = location;
        RestaurantName = restaurantName;
        this.restaurantFoodItemList = new ArrayList<>();
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<RestaurantFoodItem> getRestaurantFoodItemList() {
        return restaurantFoodItemList;
    }
    public void addFoodItem(RestaurantFoodItem restaurantFoodItem){
        restaurantFoodItemList.add(restaurantFoodItem);
    }
}
