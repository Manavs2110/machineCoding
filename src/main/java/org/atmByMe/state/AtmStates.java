package org.atmByMe.state;

import org.atmByMe.service.AtmService;
import org.atmByMe.strategy.AtmOperations;

public abstract class AtmStates {
    public void insertCard(String cardNumber, AtmService atmService){
        reject();
    }
    public void enterPin(long pin, AtmService atmService){
        reject();
    }
    public void perform(AtmOperations operation, AtmService atmService){
        reject();
    }
    public void ejectCard(AtmService atmService){
        reject();
    }

    private void reject(){
        System.out.println("Invalid action in " + getClass().getSimpleName());
    }
}
