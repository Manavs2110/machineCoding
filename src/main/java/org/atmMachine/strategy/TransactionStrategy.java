package org.atmMachine.strategy;

import org.atmMachine.model.Atm;
import org.atmMachine.model.Card;
import org.atmMachine.model.Transaction;
import org.atmMachine.service.BankService;

// Common context comes in as parameters; each strategy's own input (amount, notes) comes in via its constructor.
public interface TransactionStrategy {
    Transaction execute(Atm atm, Card card, BankService bank);
}
