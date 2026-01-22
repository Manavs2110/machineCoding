package org.FoodOrderingSystem.model;

public class Location {
    String city;
    Integer latitude;
    Integer longitude;

    public Location(String city, Integer longitude, Integer latitude) {
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
