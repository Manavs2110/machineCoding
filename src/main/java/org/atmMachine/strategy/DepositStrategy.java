package org.atmMachine.strategy;

import org.atmMachine.model.Atm;
import org.atmMachine.model.Card;
import org.atmMachine.model.Transaction;
import org.atmMachine.model.TransactionStatus;
import org.atmMachine.model.TransactionType;
import org.atmMachine.service.BankService;

import java.util.Map;

public class DepositStrategy implements TransactionStrategy {
    Map<Integer, Integer> notes; // denomination -> count put in by the user

    public DepositStrategy(Map<Integer, Integer> notes) {
        this.notes = notes;
    }

    @Override
    public Transaction execute(Atm atm, Card card, BankService bank) {
        long rupees = 0;
        for (int denomination : notes.keySet()) {
            atm.addNotes(denomination, notes.get(denomination));
            rupees += (long) denomination * notes.get(denomination);
        }
        Transaction txn = bank.createTransaction(atm.getId(), card.getCardNumber(), TransactionType.DEPOSIT, rupees * 100);
        bank.getAccount(card.getAccountId()).credit(rupees * 100);
        txn.setStatus(TransactionStatus.SUCCESS);
        return txn;
    }
}
