package com.laba.solvd.bank.service.impl;
import com.laba.solvd.bank.dao.impl.AccountRepositoryImpl;
import com.laba.solvd.bank.dao.interfaces.AccountRepository;
import com.laba.solvd.bank.dao.mapper.AccountMapperImpl;
import com.laba.solvd.bank.model.Account;
import com.laba.solvd.bank.model.Transaction;
import com.laba.solvd.bank.service.interfaces.AccountService;
import com.laba.solvd.bank.service.interfaces.TransactionService;
import java.util.List;
import java.util.stream.Collectors;
import static com.laba.solvd.bank.Main.connectionFactory;

public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private TransactionService transactionService;
    public AccountServiceImpl() {
        if(!connectionFactory.isMyBatis()){
            this.accountRepository=new AccountRepositoryImpl();
        }else{
            this.accountRepository=new AccountMapperImpl();
        }
        this.transactionService=new TransactionServiceImpl();
    }


    public Account createAccount(Account account) {
        account.setId(null);
        accountRepository.create(account);
        if (account.getTransaction() != null) {
            List<Transaction> transactions = account.getTransaction().stream().
                    map(transaction -> transactionService.createTransaction(transaction))
                    .collect(Collectors.toList());
            account.setTransaction(transactions);
    }
        return account;
    }

    public List<Account> getAllAccounts() {

        return accountRepository.findAll();
    }


}
