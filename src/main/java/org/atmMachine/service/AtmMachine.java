package org.atmMachine.service;

import org.atmMachine.model.Atm;
import org.atmMachine.model.Card;
import org.atmMachine.model.Transaction;
import org.atmMachine.state.AtmState;
import org.atmMachine.state.IdleState;
import org.atmMachine.strategy.TransactionStrategy;

// State-machine context: every user action is forwarded to the current state.
public class AtmMachine {
    Atm atm;
    BankService bank;
    AtmState state = new IdleState();
    Card currentCard;

    public AtmMachine(Atm atm, BankService bank) {
        this.atm = atm;
        this.bank = bank;
    }

    public void insertCard(String cardNumber) { state.insertCard(this, cardNumber); }
    public void enterPin(String pin) { state.enterPin(this, pin); }
    public Transaction perform(TransactionStrategy strategy) { return state.perform(this, strategy); }
    public void ejectCard() { state.ejectCard(this); }

    public Atm getAtm() { return atm; }
    public BankService getBank() { return bank; }
    public Card getCurrentCard() { return currentCard; }
    public void setCurrentCard(Card currentCard) { this.currentCard = currentCard; }
    public void setState(AtmState state) { this.state = state; }
}
