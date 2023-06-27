package com.laba.solvd.bank;
import com.laba.solvd.bank.dao.impl.AccountRepositoryImpl;
import com.laba.solvd.bank.dao.impl.CustomerRepositoryImpl;
import com.laba.solvd.bank.dao.impl.TransactionRepositoryImpl;
import com.laba.solvd.bank.dao.interfaces.AccountRepository;
import com.laba.solvd.bank.dao.interfaces.CustomerRepository;
import com.laba.solvd.bank.dao.interfaces.TransactionRepository;
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
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName());


        AccountRepository accountRepository = new AccountRepositoryImpl();
        CustomerRepository customerRepository = new CustomerRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();

        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
        AccountService accountService = new AccountServiceImpl(accountRepository, transactionService);
        CustomerService customerService = new CustomerServiceImpl(customerRepository, accountService);

        Customer customer1 = new Customer("Ashley", "Connor");
        Customer customer2 = new Customer("Emily", "Meyer");

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

        List<Customer> customerList = customerService.getAllCustomers();
        List<Account> accountList = accountService.getAllAccounts();

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