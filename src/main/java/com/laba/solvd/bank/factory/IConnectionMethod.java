package com.laba.solvd.bank.factory;

import com.laba.solvd.bank.dao.interfaces.AccountRepository;
import com.laba.solvd.bank.dao.interfaces.CustomerRepository;
import com.laba.solvd.bank.dao.interfaces.TransactionRepository;

public interface IConnectionMethod {
    void initialize();
}
