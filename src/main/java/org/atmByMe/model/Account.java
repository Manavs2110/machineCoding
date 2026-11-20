package org.atmByMe.model;

public class Account {
    long id;
    String holder_name;
    double balance;

    public Account(long id, String holder_name, double balance) {
        this.id = id;
        this.holder_name = holder_name;
        this.balance = balance;
    }

    public long getId() { return id; }
    public double getBalance() { return balance; }
    public void debit(double amount) { balance -= amount; }
    public void credit(double amount) { balance += amount; }
}
