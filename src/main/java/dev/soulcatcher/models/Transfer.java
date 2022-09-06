package dev.soulcatcher.models;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @Column(name = "transfer_id", unique = true, nullable = false)
    private String transferId;
    @OneToOne(mappedBy = "account_id")
    private Account from;
    @OneToOne(mappedBy = "account_id")
    private Account to;
    @Column(name = "amount", precision = 8, scale = 2, nullable = false)
    private double amount;

    public Transfer(Account from, Account to, double amount) {
        this.transferId = UUID.randomUUID().toString();
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public Transfer() {
        super();
    }
    public Transfer(String transferId, Account from, Account to, double amount) {
        this(from, to, amount);
        this.transferId = transferId;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return Double.compare(transfer.amount, amount) == 0 && Objects.equals(transferId, transfer.transferId) && Objects.equals(from, transfer.from) && Objects.equals(to, transfer.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, from, to, amount);
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId='" + transferId + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", amount=" + amount +
                '}';
    }
}
