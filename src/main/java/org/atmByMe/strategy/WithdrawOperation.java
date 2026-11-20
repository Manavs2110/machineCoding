package org.atmByMe.strategy;

import org.atmByMe.model.Account;
import org.atmByMe.model.Atm;
import org.atmByMe.model.Card;
import org.atmByMe.model.Transaction;
import org.atmByMe.model.TransactionStatus;
import org.atmByMe.model.Type;
import org.atmByMe.service.AtmService;
import org.atmByMe.service.BankService;

public class WithdrawOperation implements AtmOperations {
    long amount;

    public WithdrawOperation(long amount) {
        this.amount = amount;
    }

    @Override
    public void perform(AtmService atmService) {
        Atm atm = atmService.getAtm();
        Card card = atmService.getCurrentCard();
        BankService bank = atmService.getBank();
        Account account = bank.getAccount(card.getAccountId());
        Transaction txn = bank.createTransaction(atm.getId(), card.getCardNumber(), Type.WITHDRAWAL, amount);

        if(amount <= 0 || amount % 100 != 0){
            fail(txn, "Amount must be a multiple of 100");
            return;
        }
        if(account.getBalance() < amount){
            fail(txn, "Insufficient balance");
            return;
        }
        try{
            atm.withdrawCash(amount);   // 1. take notes out of the machine (chain throws if it can't)
        } catch (RuntimeException e){
            fail(txn, "ATM does not have notes for " + amount);
            return;
        }
        account.debit(amount);          // 2. only then reduce the customer's balance
        txn.setStatus(TransactionStatus.SUCCESS);
        System.out.println("Withdrawn " + amount + ", balance " + account.getBalance());
    }

    private void fail(Transaction txn, String reason){
        txn.setStatus(TransactionStatus.FAILED);
        System.out.println(reason);
    }
}
