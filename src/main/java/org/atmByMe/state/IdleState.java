package org.atmByMe.state;

import org.atmByMe.model.AtmStatus;
import org.atmByMe.model.Card;
import org.atmByMe.model.Status;
import org.atmByMe.service.AtmService;

public class IdleState extends AtmStates{
    @Override
    public void insertCard(String cardNumber, AtmService atmService){
        if(atmService.getAtm().getAtmStatus() == AtmStatus.OUT_OF_SERVICE){
            System.out.println("ATM out of service");
            return;
        }
        Card card = atmService.getBank().getCard(cardNumber);
        if(card == null || card.getStatus() == Status.BLOCKED){
            System.out.println("Invalid or blocked card");
            return;
        }
        System.out.println("card Inserted");
        atmService.setCurrentCard(card);
        atmService.setState(new CardInsertedState());
    }
}
