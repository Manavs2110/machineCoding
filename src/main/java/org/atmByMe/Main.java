package org.atmByMe;

import org.atmByMe.model.Account;
import org.atmByMe.model.Atm;
import org.atmByMe.model.AtmStatus;
import org.atmByMe.model.Card;
import org.atmByMe.model.Status;
import org.atmByMe.service.AtmService;
import org.atmByMe.service.BankService;
import org.atmByMe.strategy.BalanceEnquiryOperation;
import org.atmByMe.strategy.DepositOperation;
import org.atmByMe.strategy.WithdrawOperation;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String args[]){
        BankService bank = new BankService();
        bank.addAccount(new Account(1, "Manav", 50000));
        bank.addCard(new Card("1111", 1, 1234, Status.ACTIVE));
        bank.addCard(new Card("2222", 1, 4321, Status.ACTIVE));

        Atm atm = new Atm(1);
        atm.addCash(2000, 5);
        atm.addCash(500, 10);
        atm.addCash(200, 10);
        atm.addCash(100, 2);
        AtmService atmService = new AtmService(atm, bank);

        System.out.println("---- happy flow ----");
        atmService.perform(new WithdrawOperation(1000));          // Invalid action in IdleState
        atmService.insertCard("1111");
        atmService.perform(new WithdrawOperation(1000));          // Invalid action in CardInsertedState
        atmService.enterPin(1234);
        atmService.perform(new BalanceEnquiryOperation());          // Balance 50000.0
        atmService.perform(new WithdrawOperation(4700));          // 2x2000 + 1x500 + 1x200
        System.out.println(atm.getCash());

        Map<Integer, Integer> notes = new HashMap<>();
        notes.put(500, 2);
        atmService.perform(new DepositOperation(notes));          // Deposited 1000

        System.out.println("---- failures ----");
        atmService.perform(new WithdrawOperation(150));           // Amount must be a multiple of 100
        atmService.perform(new WithdrawOperation(100000));        // Insufficient balance
        atmService.perform(new WithdrawOperation(20000));         // ATM does not have notes for 20000
        atmService.ejectCard();

        System.out.println("---- wrong PIN 3 times ----");
        atmService.insertCard("2222");
        atmService.enterPin(1);
        atmService.enterPin(2);
        atmService.enterPin(3);             // Card blocked + ejected
        atmService.insertCard("2222");      // Invalid or blocked card

        System.out.println("---- out of service ----");
        atm.setAtmStatus(AtmStatus.OUT_OF_SERVICE);
        atmService.insertCard("1111");      // ATM out of service

        System.out.println("---- transactions ----");
        System.out.println(bank.getTransactions());
    }
}
