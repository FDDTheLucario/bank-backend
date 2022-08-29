package dev.soulcatcher.models;

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
}
