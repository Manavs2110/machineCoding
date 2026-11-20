package org.atmByMe.model;

import org.atmByMe.dispenser.FiveHundredHandler;
import org.atmByMe.dispenser.NoteHandler;
import org.atmByMe.dispenser.OneHundredHandler;
import org.atmByMe.dispenser.TwoHundredHandler;
import org.atmByMe.dispenser.TwoThousandHandler;

import java.util.HashMap;
import java.util.Map;

public class Atm {
    long id;
    AtmStatus atmStatus= AtmStatus.SERVICEBLE;
    Map<Integer, Integer> cash = new HashMap<>();

    public Atm(long id) {
        this.id = id;
    }
    public void addCash(int denominator, int qty){
        cash.put(denominator, cash.getOrDefault(denominator, 0 ) + qty);
    }

    public void withdrawCash(long amount){
        NoteHandler chain = new TwoThousandHandler(new FiveHundredHandler(new TwoHundredHandler(new OneHundredHandler(null))));
        Map<Integer, Integer> copy = new HashMap<>(cash); // work on a copy so a failed withdraw leaves cash untouched
        chain.deductMoney(amount, copy);
        cash = copy;
    }

    public long getId() { return id; }
    public Map<Integer, Integer> getCash() { return cash; }
    public AtmStatus getAtmStatus() { return atmStatus; }
    public void setAtmStatus(AtmStatus atmStatus) { this.atmStatus = atmStatus; }
}
