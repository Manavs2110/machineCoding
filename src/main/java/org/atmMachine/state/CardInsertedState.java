package org.atmMachine.state;

import org.atmMachine.model.Card;
import org.atmMachine.model.CardStatus;
import org.atmMachine.service.AtmMachine;

public class CardInsertedState extends AtmState {
    @Override
    public void enterPin(AtmMachine machine, String pin) {
        Card card = machine.getCurrentCard();
        if (card.checkPin(pin)) {
            machine.setState(new AuthenticatedState());
            return;
        }
        if (card.getStatus() == CardStatus.BLOCKED) {
            ejectCard(machine);
            throw new RuntimeException("Card blocked after too many wrong PINs");
        }
        throw new RuntimeException("Wrong PIN");
    }

    @Override
    public void ejectCard(AtmMachine machine) {
        machine.setCurrentCard(null);
        machine.setState(new IdleState());
    }
}
