package org.atmMachine.dispenser;

import java.util.Map;

// Chain of Responsibility: 2000 -> 500 -> 200 -> 100.
// Each handler takes as many of its notes as it can and passes the rest to the next one.
public class NoteDispenser {
    int denomination;
    NoteDispenser next;

    public NoteDispenser(int denomination, NoteDispenser next) {
        this.denomination = denomination;
        this.next = next;
    }

    // Fills plan (denomination -> count). Returns the amount nobody could serve (0 = success).
    public int plan(int amount, Map<Integer, Integer> cash, Map<Integer, Integer> plan) {
        int notes = Math.min(amount / denomination, cash.getOrDefault(denomination, 0));
        if (notes > 0) plan.put(denomination, notes);
        int left = amount - notes * denomination;
        if (next == null) return left;
        return next.plan(left, cash, plan);
    }

    public static NoteDispenser buildChain() {
        NoteDispenser hundred = new NoteDispenser(100, null);
        NoteDispenser twoHundred = new NoteDispenser(200, hundred);
        NoteDispenser fiveHundred = new NoteDispenser(500, twoHundred);
        return new NoteDispenser(2000, fiveHundred);
    }
}
