package org.atmByMe.state;

import org.atmByMe.service.AtmService;
import org.atmByMe.strategy.AtmOperations;

public class AuthenticatedState extends AtmStates{
    @Override
    public void perform(AtmOperations operation, AtmService atmService){
        operation.perform(atmService); // state only says "allowed"; the strategy does the work
    }

    @Override
    public void ejectCard(AtmService atmService){
        System.out.println("card Ejected");
        atmService.setCurrentCard(null);
        atmService.setState(new IdleState());
    }
}
