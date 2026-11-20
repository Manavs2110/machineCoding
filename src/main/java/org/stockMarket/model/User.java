package org.stockMarket.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    long userId;
    String name;
    long balance;
    Map<String, Holding> holdings = new HashMap<>();   // stock symbol -> holding

    public User(long userId, String name, long balance) {
        this.userId = userId;
        this.name = name;
        this.balance = balance;
    }

    public void addBalance(long amount){
        balance += amount;
    }

    public void deductBalance(long amount){
        if(amount > balance) throw new IllegalArgumentException("Insufficient balance");
        balance -= amount;
    }

    public Holding getHolding(String stockSymbol){
        return holdings.computeIfAbsent(stockSymbol, symbol -> new Holding());
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public long getBalance() {
        return balance;
    }

    public Map<String, Holding> getHoldings() {
        return holdings;
    }
}
