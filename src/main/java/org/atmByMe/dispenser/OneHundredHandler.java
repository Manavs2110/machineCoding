package org.atmByMe.dispenser;

import java.util.Map;

public class OneHundredHandler extends NoteHandler {
    public OneHundredHandler(NoteHandler nextNoteHandler) {
        super(nextNoteHandler);
    }

    @Override
    public void deductMoney(long amount, Map<Integer, Integer> cash) {
        int available = cash.getOrDefault(100, 0);
        int notes = (int) Math.min(amount / 100, available);
        cash.put(100, available - notes);

        long remaining = amount - (long) notes * 100;
        if (remaining > 0) super.deductMoney(remaining, cash);
    }
}
