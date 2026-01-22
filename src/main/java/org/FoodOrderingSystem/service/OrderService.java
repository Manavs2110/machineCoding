package org.FoodOrderingSystem.service;

import org.FoodOrderingSystem.model.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    Map<String, List<Restaurant>> restaurants;
    Map<String, List<Customer>> customers;
    Map<String, Restaurant> restaurantMap;

    public OrderService() {
        customers = new HashMap<>();
        restaurants = new HashMap<>();
        restaurantMap = new HashMap<>();
    }

    public void addRestaurant(Restaurant restaurant){
        if(!restaurants.containsKey(restaurant.getLocation().getCity())){
            restaurants.put(restaurant.getLocation().getCity(),new ArrayList<>());
        }
        List<Restaurant> restaurants1 = restaurants.get(restaurant.getLocation().getCity());
        restaurants1.add(restaurant);
        restaurants.put(restaurant.getLocation().getCity(), restaurants1);
        restaurantMap.put(restaurant.getRestaurantId(),restaurant);
    }
    public void addCustomer(Customer customer){
        if(!customers.containsKey(customer.getLocation().getCity())){
            customers.put(customer.getLocation().getCity(),new ArrayList<>());
        }
        List<Customer> customers1 = customers.get(customer.getLocation().getCity());
        customers1.add(customer);
        customers.put(customer.getLocation().getCity(), customers1);
    }


    public List<Restaurant> getRestaurants(Location location){
        return restaurants.get(location.getCity());
    }

    public List<RestaurantFoodItem> getMenu(String restaurantId){
        return restaurantMap.get(restaurantId).getRestaurantFoodItemList();
    }

    public void addItemsInCart(Customer customer, CartFoodItem cartFoodItem){
        if(customer.getCart() == null) customer.setCart(new Cart());
        customer.getCart().addCartItem(cartFoodItem);
    }
    public void printCart(Cart cart){
        for(CartFoodItem cartFoodItem: cart.getCartFoodItems()){
            System.out.println(cartFoodItem.getFoodItem().getName()+" "+cartFoodItem.getQuantity());
        }
    }
    public void ModifyCartItem(Customer customer, String cartFoodItemId, int quantity){
        List<CartFoodItem> cartFoodItems = customer.getCart().getCartFoodItems();
        for(CartFoodItem cartFoodItem:cartFoodItems){
            if(cartFoodItem.getFoodItem().getId().equals(cartFoodItemId)){
                cartFoodItem.setQuantity(quantity);
            }
        }
    }
    public Bill generateBill(Customer customer, String restaurantId){
        Cart cart = customer.getCart();
        List<CartFoodItem> cartFoodItems = cart.getCartFoodItems();
        double total = 0;
        for(CartFoodItem cartFoodItem: cartFoodItems){
            total+=cartFoodItem.getQuantity()*cartFoodItem.getFoodItem().getPrice();
        }
        Bill bill = (new Bill(cart,customer.getId(),restaurantId, total));
        customer.addBill(bill);
        customer.clearCart();
        return bill;
    }

    public void orderHistory(Customer customer){
        for(Bill bill: customer.getBills()){
            printCart(bill.getCart());
            System.out.println(bill.getTotal());
        }
    }

}
