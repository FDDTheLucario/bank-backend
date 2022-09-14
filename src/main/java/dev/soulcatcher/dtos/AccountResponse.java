package dev.soulcatcher.dtos;

import dev.soulcatcher.models.Account;
import dev.soulcatcher.util.Generation;
import lombok.Data;

@Data
public class AccountResponse {
    private String accountName;
    private String accountId;
    private String lastFour;
    private double currentBalance;

    public AccountResponse(Account account) {
        this.accountName = account.getNickname();
        this.lastFour = Generation.getLastFour(account.getAccountNumber());
        this.currentBalance = account.getCurrentBalance();
        this.accountId = account.getAccountId();
    }
}
