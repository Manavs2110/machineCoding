package org.atmByMe.strategy;

import org.atmByMe.model.Account;
import org.atmByMe.model.Atm;
import org.atmByMe.model.Card;
import org.atmByMe.model.Transaction;
import org.atmByMe.model.TransactionStatus;
import org.atmByMe.model.Type;
import org.atmByMe.service.AtmService;
import org.atmByMe.service.BankService;

import java.util.Map;

public class DepositOperation implements AtmOperations {
    Map<Integer, Integer> notes; // denomination -> count

    public DepositOperation(Map<Integer, Integer> notes) {
        this.notes = notes;
    }

    @Override
    public void perform(AtmService atmService) {
        Atm atm = atmService.getAtm();
        Card card = atmService.getCurrentCard();
        BankService bank = atmService.getBank();
        Account account = bank.getAccount(card.getAccountId());

        long amount = 0;
        for(int denomination : notes.keySet()){
            atm.addCash(denomination, notes.get(denomination));
            amount += (long) denomination * notes.get(denomination);
        }
        account.credit(amount);
        Transaction txn = bank.createTransaction(atm.getId(), card.getCardNumber(), Type.DEPOSIT, amount);
        txn.setStatus(TransactionStatus.SUCCESS);
        System.out.println("Deposited " + amount + ", balance " + account.getBalance());
    }
}
