package org.example.service;

import org.example.model.User;
import org.example.repository.ExpenseRepository;

public class UserService {
    ExpenseRepository expenseRepository;

    public UserService(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }
    public void addUser(User user){
        expenseRepository.addUser(user);
    }
    public User getUser(String userName){
        return expenseRepository.getUser(userName);
    }
}
