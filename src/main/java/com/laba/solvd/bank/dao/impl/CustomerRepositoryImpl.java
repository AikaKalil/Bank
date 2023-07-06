package com.laba.solvd.bank.dao.impl;

import com.laba.solvd.bank.config.ConnectionPool;
import com.laba.solvd.bank.dao.interfaces.CustomerRepository;
import com.laba.solvd.bank.model.Account;
import com.laba.solvd.bank.model.Customer;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CustomerRepositoryImpl implements CustomerRepository {
    private final ConnectionPool CONNECTION_POOL=ConnectionPool.getInstance();
    Logger logger = Logger.getLogger(CustomerRepositoryImpl.class.getName());
    private String FIND_ALL="SELECT c.id as customer_id, c.first_name as first_name,c.last_name as last_name, a.id as account_id, a.account_type as account_type, a.balance as balance, a.date_opened as date_opened" +
            "FROM customers c" +
            "LEFT JOIN accounts a ON c.id = a.customer_id;";

    private String CREATE="INSERT INTO customers (firstName, lastName) VALUES (?, ?)";

    @Override
    public void create(Customer customer) {
        Connection connection = CONNECTION_POOL.getConnection();
        try(PreparedStatement prepareStatement= connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)){
            prepareStatement.setString(2, customer.getFirstName());
            prepareStatement.setString(3, customer.getLastName());
            prepareStatement.executeUpdate();
            ResultSet resultSet=prepareStatement.getGeneratedKeys();
            while(resultSet.next()){
                customer.setId(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            logger.info("Failed to create customer", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public List<Customer> findAll() {
        Connection connection = CONNECTION_POOL.getConnection();
        List<Customer>customers=null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            customers =mapCustomers(resultSet);

        } catch (SQLException e) {
            logger.info("Failed to retrieve customers", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return customers;
    }

    private static Customer findById(Long id, List<Customer> customers) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst()
                .orElseGet(() -> {
                    Customer createdDepartment = new Customer();
                    createdDepartment.setId(id);
                    customers.add(createdDepartment);
                    return createdDepartment;
                });
    }


    private static List<Customer>mapCustomers(ResultSet resultSet)throws SQLException{
        List<Customer> customers=new ArrayList<>();
        while(resultSet.next()){
            Long id=resultSet.getLong("id");
            Customer customer=findById(id,customers);
            customer.setFirstName("first_name");
            customer.setLastName("last_name");
            List<Account> accounts=AccountRepositoryImpl.mapRow(resultSet,customer.getAccount());
            customer.setAccount(accounts);
        }
        return customers;
    }
}
