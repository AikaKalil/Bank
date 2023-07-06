package com.laba.solvd.bank;
import com.laba.solvd.bank.config.MyBatis;
import com.laba.solvd.bank.dao.impl.AccountRepositoryImpl;
import com.laba.solvd.bank.dao.impl.CustomerRepositoryImpl;
import com.laba.solvd.bank.dao.impl.TransactionRepositoryImpl;
import com.laba.solvd.bank.dao.interfaces.AccountRepository;
import com.laba.solvd.bank.dao.interfaces.CustomerRepository;
import com.laba.solvd.bank.dao.interfaces.TransactionRepository;
import com.laba.solvd.bank.factory.ConnectionMethodFactory;
import com.laba.solvd.bank.model.Account;
import com.laba.solvd.bank.model.Customer;
import com.laba.solvd.bank.model.Transaction;
import com.laba.solvd.bank.service.impl.AccountServiceImpl;
import com.laba.solvd.bank.service.impl.CustomerServiceImpl;
import com.laba.solvd.bank.service.impl.TransactionServiceImpl;
import com.laba.solvd.bank.service.interfaces.AccountService;
import com.laba.solvd.bank.service.interfaces.CustomerService;
import com.laba.solvd.bank.service.interfaces.TransactionService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Logger logger = Logger.getLogger(Main.class);
    public static ConnectionMethodFactory connectionFactory = new ConnectionMethodFactory();

    public static void main(String[] args) {


        TransactionService transactionService = new TransactionServiceImpl();
        AccountService accountService = new AccountServiceImpl();
        CustomerService customerService = new CustomerServiceImpl();

        Customer customer1 = new Customer();
        customer1.setFirstName("Ashley");
        customer1.setLastName("Connor");

        Customer customer2 = new Customer();
        customer2.setFirstName("Emily");
        customer2.setLastName( "Meyer");

        Account account1 = new Account("Savings", 1000.0);
        Account account2 = new Account("Checking", 100000);

        Transaction transaction1 = new Transaction("Deposit", 500.0);
        Transaction transaction2 = new Transaction("Withdrawal", 6500.0);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        List<Account> accounts=new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        account1.setTransaction(transactions);
        customer1.setAccount(accounts);

        customerService.createCustomer(customer1);
        transactionService.getAllTransactions();

        List<Customer> customerList = customerService.getAllCustomers();
        // Displaying the retrieved customers and accounts
        for (Customer c : customerList) {
            logger.info("Customer: " + c.getFirstName() + " " + c.getLastName());
            for (Account a : c.getAccount()) {
                logger.info("Account: " + a.getAccountType() + ", Balance: " + a.getBalance());
                for (Transaction t : a.getTransaction()) {
                    logger.info("Transaction: " + t.getTransactionType() + ", Amount: " + t.getAmount());
                }
            }
        }
    }
}