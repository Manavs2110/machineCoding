package org.atmByMe.model;

public class Card {
    String cardNumber;
    long accountId;
    long pin; // real system stores a hash, never the plain PIN
    Status status;
    int failedAttempts = 0;

    public Card(String cardNumber, long accountId, long pin, Status status) {
        this.cardNumber = cardNumber;
        this.accountId = accountId;
        this.pin = pin;
        this.status = status;
    }

    public boolean checkPin(long enteredPin) {
        if (pin == enteredPin) {
            failedAttempts = 0;
            return true;
        }
        failedAttempts++;
        if (failedAttempts >= 3) status = Status.BLOCKED;
        return false;
    }

    public String getCardNumber() { return cardNumber; }
    public long getAccountId() { return accountId; }
    public Status getStatus() { return status; }
}
