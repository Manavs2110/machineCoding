package org.atmByMe.service;

import org.atmByMe.model.Account;
import org.atmByMe.model.Card;
import org.atmByMe.model.Transaction;
import org.atmByMe.model.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankService {
    Map<Long, Account> accounts = new HashMap<>();
    Map<String, Card> cards = new HashMap<>();
    List<Transaction> transactions = new ArrayList<>();

    public void addAccount(Account account) { accounts.put(account.getId(), account); }
    public void addCard(Card card) { cards.put(card.getCardNumber(), card); }
    public Account getAccount(long accountId) { return accounts.get(accountId); }
    public Card getCard(String cardNumber) { return cards.get(cardNumber); }
    public List<Transaction> getTransactions() { return transactions; }

    public Transaction createTransaction(long atmId, String cardNumber, Type type, long amount) {
        Transaction txn = new Transaction(transactions.size() + 1, type, amount, cardNumber, atmId);
        transactions.add(txn);
        return txn;
    }
}
