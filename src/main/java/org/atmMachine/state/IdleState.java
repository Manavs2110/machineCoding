package org.atmMachine.state;

import org.atmMachine.model.AtmStatus;
import org.atmMachine.model.Card;
import org.atmMachine.model.CardStatus;
import org.atmMachine.service.AtmMachine;

public class IdleState extends AtmState {
    @Override
    public void insertCard(AtmMachine machine, String cardNumber) {
        if (machine.getAtm().getStatus() == AtmStatus.OUT_OF_SERVICE) throw new RuntimeException("ATM out of service");
        Card card = machine.getBank().getCard(cardNumber);
        if (card.getStatus() == CardStatus.BLOCKED) throw new RuntimeException("Card blocked");
        machine.setCurrentCard(card);
        machine.setState(new CardInsertedState());
    }
}
