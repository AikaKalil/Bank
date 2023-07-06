package com.laba.solvd.bank.service.interfaces;
import com.laba.solvd.bank.model.Transaction;
import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
}
