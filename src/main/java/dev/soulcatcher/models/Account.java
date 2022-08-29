package dev.soulcatcher.models;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "account_id", unique = true, nullable = false)
    private String accountId;
    @ManyToOne
    @JoinColumn(name = "account_type_id")
    private AccountType type;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
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
}
