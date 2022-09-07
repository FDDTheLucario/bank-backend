package dev.soulcatcher.models;

import dev.soulcatcher.util.Generation;

import javax.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    private String transactionId;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    @Column(name = "amount", precision = 8, scale = 2)
    private double amount;
    private String merchant;

    public Transaction() {
        this.transactionId = Generation.genId();
    }

    public Transaction(Account account, double amount, String merchant) {
        this.transactionId = Generation.genId();
        this.account = account;
        this.amount = amount;
        this.merchant = merchant;
    }
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }
}
