package dev.soulcatcher.models;

import dev.soulcatcher.dtos.NewAccountRequest;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "account_id", unique = true, nullable = false)
    private String accountId;
    @Column(name = "account_number", unique = true, nullable = false)
    private long accountNumber;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = true)
    private String nickname;
    @Column(name = "current_balance", precision = 8, scale = 2)
    private double currentBalance;
    @Column(name = "available_balance", precision = 8, scale = 2)
    private double availableBalance;
    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    public Account(NewAccountRequest accountRequest) {
        this.user = accountRequest.getUser();
        this.nickname = accountRequest.getNickname();
        this.accountId = UUID.randomUUID().toString();
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    public void addTransaction(Transaction... transaction) {
        transactions.addAll(Arrays.asList(transaction));
    }
    public void addTransaction(double amount, String merchant) {
        Transaction transaction = new Transaction();
        transaction.setAccount(this);
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAmount(amount);
        transaction.setMerchant(merchant);
        transactions.add(transaction);
    }
}
