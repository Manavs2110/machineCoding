package org.example;

import org.example.model.ExpenseType;
import org.example.model.Type;
import org.example.model.User;
import org.example.model.split.Split;
import org.example.repository.ExpenseRepository;
import org.example.service.SplitwiseService;
import org.example.service.UserService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        User user1 = new User(1, "u1","u1@gmail.com","9890098900");
        User user2 = new User(2, "u2","u2@gmail.com","9999999999");
        User user3 = new User(3, "u3","u3@gmail.com","9898989899");
        User user4 = new User(4, "u4","u4@gmail.com","8976478292");
        ExpenseRepository expenseRepository = new ExpenseRepository();
        UserService userService = new UserService(expenseRepository);
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        userService.addUser(user4);

        SplitwiseService service = new SplitwiseService(expenseRepository);
        while(true){
            Scanner sc = new Scanner(System.in);
            String[] input = sc.nextLine().split(" ");
            Type type = Type.of(input[0]);
            switch (type){
                case Type.EXPENSE:
                    String paidBy = input[1];
                    BigDecimal amount = new BigDecimal(input[2]);
                    int totalmember = Integer.parseInt(input[3]);
                    List<Split> splitList = new ArrayList<>();
                    int expenseIndex = 3 + totalmember + 1;
                    ExpenseType expenseType = ExpenseType.of(input[expenseIndex]);
                    BigDecimal splitAmount;
                    switch (expenseType){
                        case EQUAL:
                             splitAmount = amount.divide(BigDecimal.valueOf(totalmember),2,
                                    RoundingMode.HALF_UP);
                            for(int i=0;i<totalmember;i++){
                                splitList.add(new Split(userService.getUser(input[4+i]),splitAmount));
                            }
                            service.addExpense(paidBy, splitList, "Goa Expense");
                            break;
                        case EXACT:
                            for (int i = 0; i < totalmember; i++) {
                                splitAmount = new BigDecimal(input[expenseIndex+i+1]);
                                splitList.add(new Split(userService.getUser(input[4+i]),splitAmount));
                            }
                            service.addExpense(paidBy, splitList, "Goa Expense");
                            break;
                        case PERCENT:
                            for (int i = 0; i < totalmember; i++) {
                                BigDecimal splitPercent = new BigDecimal(input[expenseIndex+i+1]);
                                splitAmount = amount
                                        .multiply(splitPercent)
                                        .divide(
                                                BigDecimal.valueOf(100),
                                                2,
                                                RoundingMode.HALF_UP
                                        );
                                splitList.add(new Split(userService.getUser(input[4+i]),splitAmount));
                            }
                            service.addExpense(paidBy, splitList, "Goa Expense");
                            break;
                    }
                    break;
                case SHOW:
                    if(input.length == 1)
                        service.showBalances();
                    else
                        service.showBalance(input[1]);
                    break;
                case QUIT:
                    System.out.println("Quiting...");
                    return;
                default:
                    System.out.println("No Expected Argument Found");
                    break;

            }
        }
    }
}
