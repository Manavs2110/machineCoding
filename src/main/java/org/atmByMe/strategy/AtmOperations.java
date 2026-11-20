package org.atmByMe.strategy;

import org.atmByMe.service.AtmService;

// Strategy: each operation gets its own input (amount, notes) through its constructor.
public interface AtmOperations {
    void perform(AtmService atmService);
}
