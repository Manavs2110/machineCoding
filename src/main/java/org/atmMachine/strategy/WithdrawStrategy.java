package org.atmMachine.strategy;

import org.atmMachine.dispenser.NoteDispenser;
import org.atmMachine.model.Account;
import org.atmMachine.model.Atm;
import org.atmMachine.model.Card;
import org.atmMachine.model.Transaction;
import org.atmMachine.model.TransactionStatus;
import org.atmMachine.model.TransactionType;
import org.atmMachine.service.BankService;

import java.util.HashMap;
import java.util.Map;

public class WithdrawStrategy implements TransactionStrategy {
    long rupees;
    NoteDispenser dispenser = NoteDispenser.buildChain();

    public WithdrawStrategy(long rupees) {
        this.rupees = rupees;
    }

    @Override
    public Transaction execute(Atm atm, Card card, BankService bank) {
        Transaction txn = bank.createTransaction(atm.getId(), card.getCardNumber(), TransactionType.WITHDRAW, rupees * 100);
        Account account = bank.getAccount(card.getAccountId());
        Map<Integer, Integer> plan = new HashMap<>();

        if (rupees * 100 > account.getBalance() || dispenser.plan((int) rupees, atm.getCash(), plan) != 0) {
            txn.setStatus(TransactionStatus.FAILED);
            throw new RuntimeException("Cannot withdraw " + rupees);
        }

        account.debit(rupees * 100);
        for (int denomination : plan.keySet()) atm.removeNotes(denomination, plan.get(denomination));
        txn.setStatus(TransactionStatus.SUCCESS);
        return txn;
    }
}
