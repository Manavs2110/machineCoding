package org.atmByMe.dispenser;

import java.util.Map;

public class FiveHundredHandler extends NoteHandler {
    public FiveHundredHandler(NoteHandler nextNoteHandler) {
        super(nextNoteHandler);
    }

    @Override
    public void deductMoney(long amount, Map<Integer, Integer> cash) {
        int available = cash.getOrDefault(500, 0);
        int notes = (int) Math.min(amount / 500, available);
        cash.put(500, available - notes);

        long remaining = amount - (long) notes * 500;
        if (remaining > 0) super.deductMoney(remaining, cash);
    }
}
