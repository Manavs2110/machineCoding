package org.atmMachine.state;

import org.atmMachine.model.Transaction;
import org.atmMachine.service.AtmMachine;
import org.atmMachine.strategy.TransactionStrategy;

// Each state overrides only the actions allowed in it; everything else is rejected here.
public abstract class AtmState {
    public void insertCard(AtmMachine machine, String cardNumber) { reject(); }
    public void enterPin(AtmMachine machine, String pin) { reject(); }
    public Transaction perform(AtmMachine machine, TransactionStrategy strategy) { reject(); return null; }
    public void ejectCard(AtmMachine machine) { reject(); }

    private void reject() {
        throw new RuntimeException("Action not allowed in " + getClass().getSimpleName());
    }
}
