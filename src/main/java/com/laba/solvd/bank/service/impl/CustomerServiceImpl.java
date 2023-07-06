package com.laba.solvd.bank.service.impl;

import com.laba.solvd.bank.dao.impl.CustomerRepositoryImpl;
import com.laba.solvd.bank.dao.interfaces.AccountRepository;
import com.laba.solvd.bank.dao.interfaces.CustomerRepository;
import com.laba.solvd.bank.dao.mapper.CustomerMapperImpl;
import com.laba.solvd.bank.model.Account;
import com.laba.solvd.bank.model.Customer;
import com.laba.solvd.bank.service.interfaces.AccountService;
import com.laba.solvd.bank.service.interfaces.CustomerService;

import java.util.List;
import java.util.stream.Collectors;

import static com.laba.solvd.bank.Main.connectionFactory;

public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private AccountService accountService;

    public CustomerServiceImpl() {
        if (!connectionFactory.isMyBatis()) {
            this.customerRepository = new CustomerRepositoryImpl();
            }
        else {
            this.customerRepository = new CustomerMapperImpl();

        }
        this.accountService = new AccountServiceImpl();
    }
    @Override
    public Customer createCustomer(Customer customer) {
        customer.setId(null);
        customerRepository.create(customer);
        if (customer.getAccount()!= null) {
            List<Account> accounts = customer.getAccount().stream()
                    .map(account -> accountService.createAccount(account))
                    .collect(Collectors.toList());
            customer.setAccount(accounts);
        }
        return customer;
    }


    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

}

