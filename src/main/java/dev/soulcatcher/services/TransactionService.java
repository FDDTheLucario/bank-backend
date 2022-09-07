package dev.soulcatcher.services;

import dev.soulcatcher.exceptions.InsufficientFundsException;
import dev.soulcatcher.models.Account;
import dev.soulcatcher.models.Transaction;
import dev.soulcatcher.repos.AccountRepository;
import dev.soulcatcher.repos.TransactionRepository;
import dev.soulcatcher.util.Generation;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellMethod;
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
            double balance = account.getCurrentBalance();
            account.getTransactions().add(t);
            transactionRepo.save(t);
            account.setCurrentBalance(balance - t.getAmount());
            accountRepo.save(account);
        }
    }
    public void addTransaction(double amount, String merchant, Account account) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        double balance = account.getCurrentBalance();
        transaction.setTransactionId(Generation.genId());
        transaction.setAmount(amount);
        transaction.setMerchant(merchant);
        addTransaction(transaction);
    }
    public void transferMoney(double amount, Account from, Account to) {
        double fromBalance = from.getCurrentBalance();
        double toBalance = to.getCurrentBalance();
        if (amount > from.getCurrentBalance()) {
            throw new InsufficientFundsException();
        }
        Transaction transactionFrom = new Transaction();

        fromBalance -= amount;
        toBalance += amount;
        from.setCurrentBalance(fromBalance);
        to.setCurrentBalance(toBalance);

        transactionFrom.setAmount(toBalance);
        transactionFrom.setAccount(from);

        String transferText = String.format("Transfer from %s to %s", from.getNickname(), to.getNickname());
        transactionFrom.setMerchant(transferText);
        from.getTransactions().add(transactionFrom);

        transactionRepo.save(transactionFrom);
        accountRepo.save(from);
        accountRepo.save(to);
    }
}
