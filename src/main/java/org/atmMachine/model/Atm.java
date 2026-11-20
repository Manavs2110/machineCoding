package org.atmMachine.model;

import java.util.Map;
import java.util.TreeMap;

public class Atm {
    long id;
    String location;
    AtmStatus status = AtmStatus.IN_SERVICE;
    Map<Integer, Integer> cash = new TreeMap<>(); // ATM_Cash table: denomination -> count

    public Atm(long id, String location) {
        this.id = id;
        this.location = location;
    }

    public void addNotes(int denomination, int count) {
        cash.put(denomination, cash.getOrDefault(denomination, 0) + count);
    }

    public void removeNotes(int denomination, int count) {
        cash.put(denomination, cash.get(denomination) - count);
    }

    public long getId() { return id; }
    public AtmStatus getStatus() { return status; }
    public Map<Integer, Integer> getCash() { return cash; }
}
