package org.atmMachine.model;

public class Card {
    String cardNumber;
    long accountId;
    String pinHash; // never store the plain PIN
    CardStatus status = CardStatus.ACTIVE;
    int failedAttempts = 0;

    public Card(String cardNumber, long accountId, String pin) {
        this.cardNumber = cardNumber;
        this.accountId = accountId;
        this.pinHash = hash(pin);
    }

    // Real system: bcrypt / SHA-256 with salt.
    private static String hash(String pin) {
        return Integer.toHexString(pin.hashCode());
    }

    public boolean checkPin(String pin) {
        if (pinHash.equals(hash(pin))) {
            failedAttempts = 0;
            return true;
        }
        failedAttempts++;
        if (failedAttempts >= 3) status = CardStatus.BLOCKED;
        return false;
    }

    public String getCardNumber() { return cardNumber; }
    public long getAccountId() { return accountId; }
    public CardStatus getStatus() { return status; }
}
