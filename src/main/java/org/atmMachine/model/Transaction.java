package org.atmMachine.model;

public class Transaction {
    private static long nextId = 1;

    long id;
    long atmId;
    String cardNumber;
    TransactionType type;
    long amount; // in paise
    TransactionStatus status = TransactionStatus.INITIATED;
    long createdAt = System.currentTimeMillis();

    public Transaction(long atmId, String cardNumber, TransactionType type, long amount) {
        this.id = nextId++;
        this.atmId = atmId;
        this.cardNumber = cardNumber;
        this.type = type;
        this.amount = amount;
    }

    public void setStatus(TransactionStatus status) { this.status = status; }

    @Override
    public String toString() {
        return type + " " + amount / 100 + " " + status;
    }
}
