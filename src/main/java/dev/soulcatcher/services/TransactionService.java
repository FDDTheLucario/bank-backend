package dev.soulcatcher.services;

import dev.soulcatcher.exceptions.InsufficientFundsException;
import dev.soulcatcher.models.Account;
import dev.soulcatcher.models.Transaction;
import dev.soulcatcher.repos.AccountRepository;
import dev.soulcatcher.repos.TransactionRepository;
import dev.soulcatcher.util.Generation;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;
    @Autowired
    public TransactionService(TransactionRepository transactionRepo, AccountRepository accountRepo) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
    }
    public void addTransaction(Transaction... transaction) {
        for (Transaction t : transaction) {
            Account account = t.getAccount();
            double balance = account.getAvailableBalance();
            account.getTransactions().add(t);
            transactionRepo.save(t);
            account.setAvailableBalance(balance - t.getAmount());
            accountRepo.save(account);
        }
    }
    public void addTransaction(double amount, String merchant, Account account) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        double balance = account.getAvailableBalance();
        transaction.setTransactionId(Generation.genId());
        transaction.setAmount(amount);
        transaction.setMerchant(merchant);
        addTransaction(transaction);
    }
    public void transferMoney(double amount, Account from, Account to) {
        double fromBalance = from.getAvailableBalance();
        double toBalance = to.getAvailableBalance();
        if (amount > from.getAvailableBalance()) {
            throw new InsufficientFundsException();
        }
        Transaction transactionFrom = new Transaction();
        Transaction transactionTo = new Transaction();
        transactionFrom.setTransactionId(Generation.genId());
        transactionTo.setTransactionId(Generation.genId());

        fromBalance -= amount;
        toBalance += amount;
        from.setAvailableBalance(fromBalance);
        to.setAvailableBalance(toBalance);

        transactionFrom.setAmount(fromBalance);
        transactionTo.setAmount(toBalance);
        transactionFrom.setAccount(from);
        transactionTo.setAccount(to);

        String transferText = String.format("Transfer from %s to %s", from.getNickname(), to.getNickname());
        transactionFrom.setMerchant(transferText);
        transactionTo.setMerchant(transferText);
        from.getTransactions().add(transactionFrom);
        to.getTransactions().add(transactionTo);

        transactionRepo.save(transactionFrom);
        transactionRepo.save(transactionTo);
        accountRepo.save(from);
        accountRepo.save(to);
    }
}
