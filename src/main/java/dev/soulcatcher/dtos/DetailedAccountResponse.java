package dev.soulcatcher.dtos;

import dev.soulcatcher.models.Account;
import dev.soulcatcher.models.Transaction;
import lombok.Data;

import java.util.List;

@Data
public class DetailedAccountResponse {
    private String accountId;
    private double currentBalance;
    private List<Transaction> transactions;

    public DetailedAccountResponse(Account account) {
        this.accountId = account.getAccountId();
        this.currentBalance = account.getCurrentBalance();
        this.transactions = account.getTransactions();
    }
}
