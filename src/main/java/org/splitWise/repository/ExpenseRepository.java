package org.splitWise.repository;

import org.splitWise.model.User;
import org.splitWise.model.split.Split;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseRepository {
    Map<String, User> users;
    Map<String,Map<String, BigDecimal>> balanceSheet;
    public ExpenseRepository(){
        users = new HashMap<>();
        balanceSheet = new HashMap<>();
    }

    public void addUser(User user){
        users.put(user.getUserName(),user);
        balanceSheet.put(user.getUserName(), new HashMap<>());
    }

    public User getUser(String userName){
        return users.get(userName);
    }

    public void addExpense(String paidBy, List<Split> splits, String title){
        if(!balanceSheet.containsKey(paidBy)) balanceSheet.put(paidBy, new HashMap<>());
        for(Split split: splits){
            String paidTo = split.getUser().getUserName();
            BigDecimal amount = split.getAmount();
            Map<String, BigDecimal> balances = balanceSheet.get(paidBy);
            if(!balances.containsKey(paidTo)){
                balances.put(paidTo,BigDecimal.ZERO);
            }
            balances.put(paidTo, balances.get(paidTo).add(amount));
            balances = balanceSheet.get(paidTo);

            if (!balances.containsKey(paidBy)) {
                balances.put(paidBy, BigDecimal.ZERO);
            }
            balances.put(paidBy, balances.get(paidBy).subtract(split.getAmount()));
        }
    }

    public List<String> getBalance(String user){
        List<String> balances = new ArrayList<>();
        Map<String, BigDecimal> balancesMap = balanceSheet.get(user);

        for(Map.Entry<String,BigDecimal> balance: balancesMap.entrySet()){
            balances.add(checkSign(user, balance.getKey(), balance.getValue()));
        }
        return balances;
    }
    public List<String> getBalance(){
        List<String> balances = new ArrayList<>();
//        Map<String, BigDecimal> balancesMap = balanceSheet.get(user.getUserName());
        for(Map.Entry<String, Map<String, BigDecimal>> balancesMap: balanceSheet.entrySet()){
            for(Map.Entry<String,BigDecimal> balance: balancesMap.getValue().entrySet()){
               if (balance.getValue().compareTo(BigDecimal.ZERO)>0)balances.add(checkSign(balancesMap.getKey(), balance.getKey(), balance.getValue()));
            }
        }

        return balances;
    }

    private String checkSign(String user1, String user2, BigDecimal amount) {
        String user1Name = users.get(user1).getUserName();
        String user2Name = users.get(user2).getUserName();
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return  user1Name + " owes " + user2Name + ": " + amount.abs();
        } else if (amount.compareTo(BigDecimal.ZERO) > 0) {
            return user2Name + " owes " + user1Name + ": " + amount.abs();
        }
        return "";
    }
}
