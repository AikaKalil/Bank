package com.laba.solvd.bank.service.impl;

import com.laba.solvd.bank.dao.impl.TransactionRepositoryImpl;
import com.laba.solvd.bank.dao.interfaces.TransactionRepository;
import com.laba.solvd.bank.dao.mapper.TransactionMapperImpl;
import com.laba.solvd.bank.model.Transaction;
import com.laba.solvd.bank.service.interfaces.TransactionService;

import java.util.List;

import static com.laba.solvd.bank.Main.connectionFactory;

public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;
    public TransactionServiceImpl() {
        if(!connectionFactory.isMyBatis()){
            this.transactionRepository = new TransactionRepositoryImpl();
            }
            else{
                this.transactionRepository=new TransactionMapperImpl();

            }
        }
    @Override
    public Transaction createTransaction(Transaction transaction) {
        transaction.setId(null);
        transactionRepository.create(transaction);
        return transaction;

    }
    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
