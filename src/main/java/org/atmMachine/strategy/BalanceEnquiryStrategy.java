package org.atmMachine.strategy;

import org.atmMachine.model.Atm;
import org.atmMachine.model.Card;
import org.atmMachine.model.Transaction;
import org.atmMachine.model.TransactionStatus;
import org.atmMachine.model.TransactionType;
import org.atmMachine.service.BankService;

public class BalanceEnquiryStrategy implements TransactionStrategy {
    @Override
    public Transaction execute(Atm atm, Card card, BankService bank) {
        long balance = bank.getAccount(card.getAccountId()).getBalance();
        Transaction txn = bank.createTransaction(atm.getId(), card.getCardNumber(), TransactionType.BALANCE_ENQUIRY, balance);
        txn.setStatus(TransactionStatus.SUCCESS);
        return txn;
    }
}
