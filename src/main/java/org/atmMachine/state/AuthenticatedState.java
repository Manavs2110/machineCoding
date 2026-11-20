package org.atmMachine.state;

import org.atmMachine.model.Transaction;
import org.atmMachine.service.AtmMachine;
import org.atmMachine.strategy.TransactionStrategy;

public class AuthenticatedState extends AtmState {
    @Override
    public Transaction perform(AtmMachine machine, TransactionStrategy strategy) {
        return strategy.execute(machine.getAtm(), machine.getCurrentCard(), machine.getBank());
    }

    @Override
    public void ejectCard(AtmMachine machine) {
        machine.setCurrentCard(null);
        machine.setState(new IdleState());
    }
}
