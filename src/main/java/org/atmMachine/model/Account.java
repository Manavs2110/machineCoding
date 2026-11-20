package org.atmMachine.model;

public class Account {
    long id;
    String holderName;
    long balance; // in paise

    public Account(long id, String holderName, long balance) {
        this.id = id;
        this.holderName = holderName;
        this.balance = balance;
    }

    public void debit(long amount) { balance -= amount; }
    public void credit(long amount) { balance += amount; }

    public long getId() { return id; }
    public long getBalance() { return balance; }
}
