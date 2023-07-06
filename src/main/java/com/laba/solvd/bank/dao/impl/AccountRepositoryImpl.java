package com.laba.solvd.bank.dao.impl;

import com.laba.solvd.bank.dao.interfaces.AccountRepository;
import com.laba.solvd.bank.config.ConnectionPool;
import com.laba.solvd.bank.model.Account;
import com.laba.solvd.bank.model.Transaction;
import org.apache.log4j.Logger;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryImpl implements AccountRepository {
    Logger logger = Logger.getLogger(AccountRepositoryImpl.class.getName());
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();

    private String CREATE ="INSERT INTO accounts (account_type, balance) VALUES (?, ?)";
    private String FIND_ALL="SELECT a.id, a.account_type, a.balance, t.id, t.transaction_type, t.amount, t.transaction_date" +
            "FROM accounts a" +
            "INNER JOIN transactions t ON a.id = t.account_id;";
    private String UPDATE = "UPDATE accounts SET account_type = ?, balance = ? WHERE id = ?";

    @Override
    public void create(Account account) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(CREATE,Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getAccountType());
            statement.setDouble(2, account.getBalance());
            statement.executeUpdate();

            try(ResultSet resultSet=statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    account.setId((resultSet.getLong(1)));
                }
            }
        } catch (SQLException e) {
            logger.info("Unable to execute statement",e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public List<Account> findAll() {
        Connection connection = CONNECTION_POOL.getConnection();
        List<Account> accounts = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL)) {
            accounts = mapAccounts(resultSet);
        } catch (SQLException e) {
            logger.info("Unable to find accounts!",e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return accounts;
    }

    @Override
    public void update(Account account) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, account.getAccountType());
            statement.setDouble(2, account.getBalance());
            statement.setLong(3, account.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Unable to update!", e);
        }finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    private static Account findById(Long id, List<Account> accounts) {
        return accounts.stream()
                .filter(account -> account.getId().equals(id))
                .findFirst().orElseGet(() -> {
                    Account newAccount = new Account();
                    newAccount.setId(id);
                    accounts.add(newAccount);
                    return newAccount;
                });
    }

    private static List<Account> mapAccounts(ResultSet resultSet) throws SQLException{
        List<Account> accounts = new ArrayList<>();
        while (resultSet.next()) {
            Long id=resultSet.getLong("id");
            Account account = findById(id,accounts);
            account.setId(resultSet.getLong("id"));
            account.setAccountType(resultSet.getString("account_type"));
            account.setBalance(resultSet.getDouble("balance"));
            List<Transaction> transactions = TransactionRepositoryImpl.mapRow(resultSet, account.getTransaction());
            account.setTransaction(transactions);
        }
        return accounts;
    }

    public static List<Account> mapRow (ResultSet resultSet, List<Account> accounts) throws SQLException{
        long id=resultSet.getLong("id");

        if(id !=0){
            if(accounts==null){
                accounts=new ArrayList<>();
            }

            Account account=findById(id,accounts);
            account.setAccountType(resultSet.getString("account_type"));
            account.setBalance(resultSet.getDouble("balance"));
            account.setTransaction(TransactionRepositoryImpl.mapRow(resultSet,account.getTransaction()));
        }
        return accounts;
    }
}
