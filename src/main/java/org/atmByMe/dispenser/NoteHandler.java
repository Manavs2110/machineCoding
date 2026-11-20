package org.atmByMe.dispenser;

import java.util.Map;

// Chain of Responsibility: each handler gives its own notes, then passes what is left to the next handler.
public abstract class NoteHandler {
    NoteHandler nextNoteHandler;

    public NoteHandler(NoteHandler nextNoteHandler) {
        this.nextNoteHandler = nextNoteHandler;
    }

    public void deductMoney(long amount, Map<Integer, Integer> cash) {
        if (nextNoteHandler != null) {
            nextNoteHandler.deductMoney(amount, cash);
        } else {
            throw new RuntimeException("Cannot dispense " + amount);
        }
    }
}
