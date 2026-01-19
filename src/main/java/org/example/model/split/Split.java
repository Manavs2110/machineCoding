package org.example.model.split;

import org.example.model.User;

import java.math.BigDecimal;

public class Split {
    private User user;
    BigDecimal amount;

    public Split(User user, BigDecimal amount) {
        this.user = user;
        this.amount = amount;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
