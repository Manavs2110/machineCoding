package org.atmMachine;

import org.atmMachine.model.Account;
import org.atmMachine.model.Atm;
import org.atmMachine.model.Card;
import org.atmMachine.service.AtmMachine;
import org.atmMachine.service.BankService;
import org.atmMachine.strategy.BalanceEnquiryStrategy;
import org.atmMachine.strategy.DepositStrategy;
import org.atmMachine.strategy.WithdrawStrategy;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        BankService bank = new BankService();
        bank.addAccount(new Account(1, "Manav", 50_000_00)); // 50,000 rupees in paise
        bank.addCard(new Card("1111", 1, "1234"));

        Atm atm = new Atm(1, "MG Road");
        atm.addNotes(2000, 5);
        atm.addNotes(500, 10);
        atm.addNotes(200, 10);
        atm.addNotes(100, 2);
        AtmMachine machine = new AtmMachine(atm, bank);

        machine.insertCard("1111");
        machine.enterPin("1234");
        System.out.println(machine.perform(new BalanceEnquiryStrategy())); // BALANCE_ENQUIRY 50000 SUCCESS
        System.out.println(machine.perform(new WithdrawStrategy(4700)));   // WITHDRAW 4700 SUCCESS
        System.out.println(atm.getCash());                                 // {100=2, 200=9, 500=9, 2000=3}

        Map<Integer, Integer> notes = new HashMap<>();
        notes.put(2000, 1);
        notes.put(500, 2);
        System.out.println(machine.perform(new DepositStrategy(notes)));   // DEPOSIT 3000 SUCCESS
        System.out.println(atm.getCash());                                 // {100=2, 200=9, 500=11, 2000=4}
        System.out.println(machine.perform(new BalanceEnquiryStrategy())); // BALANCE_ENQUIRY 48300 SUCCESS

        try {
            machine.perform(new WithdrawStrategy(150)); // 50 can't be made from notes
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());                            // Cannot withdraw 150
        }

        machine.ejectCard();
        try {
            machine.perform(new WithdrawStrategy(100)); // no card -> state machine rejects
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());                            // Action not allowed in IdleState
        }
    }
}
