package org.FoodOrderingSystem;

import org.FoodOrderingSystem.model.*;
import org.FoodOrderingSystem.service.OrderService;

public class main {
    public static void main(String args[]){
        Customer manav = new Customer("1","Manav", new Location("Bangalore", 0,0));
        Restaurant meghna = new Restaurant("1", new Location("Bangalore", 4,4),"Meghna");
        FoodItem biryani = new FoodItem("1","Biryani",100, meghna.getRestaurantId());
        FoodItem soda = new FoodItem("2","soda",200, meghna.getRestaurantId());
        meghna.addFoodItem(new RestaurantFoodItem(biryani,10));
        meghna.addFoodItem(new RestaurantFoodItem(soda,10));
        OrderService orderService = new OrderService();
        orderService.addCustomer(manav);
        orderService.addRestaurant(meghna);

        orderService.addItemsInCart(manav, new CartFoodItem(biryani,2));
        orderService.addItemsInCart(manav, new CartFoodItem(soda,2));
//        orderService.printCart(manav.getCart());
        orderService.generateBill(manav, meghna.getRestaurantId());

        orderService.orderHistory(manav);
        orderService.addItemsInCart(manav, new CartFoodItem(biryani,3));
        orderService.addItemsInCart(manav, new CartFoodItem(soda,3));
        orderService.generateBill(manav, meghna.getRestaurantId());
        orderService.orderHistory(manav);
    }
}
