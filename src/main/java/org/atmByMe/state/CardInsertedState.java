package org.atmByMe.state;

import org.atmByMe.model.Card;
import org.atmByMe.model.Status;
import org.atmByMe.service.AtmService;

public class CardInsertedState extends AtmStates{
    @Override
    public void enterPin(long pin, AtmService atmService){
        Card card = atmService.getCurrentCard();
        if(card.checkPin(pin)){
            System.out.println("PIN correct");
            atmService.setState(new AuthenticatedState());
            return;
        }
        if(card.getStatus() == Status.BLOCKED){
            System.out.println("Card blocked after 3 wrong PINs");
            ejectCard(atmService);
            return;
        }
        System.out.println("Wrong PIN");
    }

    @Override
    public void ejectCard(AtmService atmService){
        System.out.println("card Ejected");
        atmService.setCurrentCard(null);
        atmService.setState(new IdleState());
    }
}
