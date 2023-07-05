package com.laba.solvd.bank.dao.interfaces;

public interface Factory {
    CustomerRepository createCustomerRepository();
    AccountRepository createAccountRepository();
    TransactionRepository createTransactionRepository();
}
