package org.atmByMe.dispenser;

import java.util.Map;

public class TwoHundredHandler extends NoteHandler {
    public TwoHundredHandler(NoteHandler nextNoteHandler) {
        super(nextNoteHandler);
    }

    @Override
    public void deductMoney(long amount, Map<Integer, Integer> cash) {
        int available = cash.getOrDefault(200, 0);
        int notes = (int) Math.min(amount / 200, available);
        cash.put(200, available - notes);

        long remaining = amount - (long) notes * 200;
        if (remaining > 0) super.deductMoney(remaining, cash);
    }
}
