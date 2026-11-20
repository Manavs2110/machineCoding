package org.atmByMe.model;

public class Transaction {
    int id;
    long atm_id;
    String card_number;
    long amount;
    Type type;
    TransactionStatus status = TransactionStatus.INITIATED;
    long created_at = System.currentTimeMillis();

    public Transaction(int id, Type type, long amount, String card_number, long atm_id) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.card_number = card_number;
        this.atm_id = atm_id;
    }

    public void setStatus(TransactionStatus status) { this.status = status; }

    @Override
    public String toString() {
        return id + " " + type + " " + amount + " " + status;
    }
}
