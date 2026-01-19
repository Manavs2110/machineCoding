package org.example.service;

import org.example.model.split.Split;
import org.example.repository.ExpenseRepository;

import java.util.List;

public class SplitwiseService {
    ExpenseRepository expenseRepository;

    public SplitwiseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void addExpense(String paidBy, List<Split> splits, String title){
        expenseRepository.addExpense(paidBy, splits, title);
    }

    public void showBalance(String userName) {
        List<String> balances = expenseRepository.getBalance(userName);
        if (balances.isEmpty()) {
            System.out.println("No balances");
        } else {
            for(String balance: balances){
                System.out.println(balance);
            }
        }
    }
    public void showBalances(){
        List<String> balances = expenseRepository.getBalance();
        if (balances.isEmpty()) {
            System.out.println("No balances");
        } else {
            for(String balance: balances){
                System.out.println(balance);
            }
        }
    }
}
