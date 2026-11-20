package org.atmMachine.service;

import org.atmMachine.model.Account;
import org.atmMachine.model.Card;
import org.atmMachine.model.Transaction;
import org.atmMachine.model.TransactionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// In-memory stand-in for the Account, Card and Transaction tables.
public class BankService {
    Map<Long, Account> accounts = new HashMap<>();
    Map<String, Card> cards = new HashMap<>();
    List<Transaction> transactions = new ArrayList<>();

    public void addAccount(Account account) { accounts.put(account.getId(), account); }
    public void addCard(Card card) { cards.put(card.getCardNumber(), card); }
    public Account getAccount(long accountId) { return accounts.get(accountId); }
    public Card getCard(String cardNumber) { return cards.get(cardNumber); }

    public Transaction createTransaction(long atmId, String cardNumber, TransactionType type, long amount) {
        Transaction txn = new Transaction(atmId, cardNumber, type, amount);
        transactions.add(txn);
        return txn;
    }
}
