package org.atmByMe.service;

import org.atmByMe.model.Atm;
import org.atmByMe.model.Card;
import org.atmByMe.state.AtmStates;
import org.atmByMe.state.IdleState;
import org.atmByMe.strategy.AtmOperations;

public class AtmService {
    Atm atm;
    BankService bank;
    Card currentCard;
    AtmStates atmStates;

    public AtmService(Atm atm, BankService bank) {
        this.atm = atm;
        this.bank = bank;
        atmStates = new IdleState();
    }

    public void insertCard(String cardNumber) { atmStates.insertCard(cardNumber, this); }
    public void enterPin(long pin) { atmStates.enterPin(pin, this); }
    public void perform(AtmOperations operation) { atmStates.perform(operation, this); }
    public void ejectCard() { atmStates.ejectCard(this); }

    public Atm getAtm() { return atm; }
    public BankService getBank() { return bank; }
    public Card getCurrentCard() { return currentCard; }
    public void setCurrentCard(Card currentCard) { this.currentCard = currentCard; }
    public void setState(AtmStates atmStates) { this.atmStates = atmStates; }
}
