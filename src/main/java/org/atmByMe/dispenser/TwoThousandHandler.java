package org.atmByMe.dispenser;

import java.util.Map;

public class TwoThousandHandler extends NoteHandler {
    public TwoThousandHandler(NoteHandler nextNoteHandler) {
        super(nextNoteHandler);
    }

    @Override
    public void deductMoney(long amount, Map<Integer, Integer> cash) {
        int available = cash.getOrDefault(2000, 0);
        int notes = (int) Math.min(amount / 2000, available);
        cash.put(2000, available - notes);

        long remaining = amount - (long) notes * 2000;
        if (remaining > 0) super.deductMoney(remaining, cash);
    }
}
