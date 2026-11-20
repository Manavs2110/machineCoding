package org.atmByMe.strategy;

import org.atmByMe.model.Account;
import org.atmByMe.model.Card;
import org.atmByMe.model.Transaction;
import org.atmByMe.model.TransactionStatus;
import org.atmByMe.model.Type;
import org.atmByMe.service.AtmService;
import org.atmByMe.service.BankService;

public class BalanceEnquiryOperation implements AtmOperations {
    @Override
    public void perform(AtmService atmService) {
        Card card = atmService.getCurrentCard();
        BankService bank = atmService.getBank();
        Account account = bank.getAccount(card.getAccountId());

        Transaction txn = bank.createTransaction(atmService.getAtm().getId(), card.getCardNumber(), Type.ENQUIRY, 0);
        txn.setStatus(TransactionStatus.SUCCESS);
        System.out.println("Balance " + account.getBalance());
    }
}
