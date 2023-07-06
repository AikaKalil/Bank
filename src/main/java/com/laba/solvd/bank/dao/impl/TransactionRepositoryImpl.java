package com.laba.solvd.bank.dao.impl;

import com.laba.solvd.bank.config.ConnectionPool;
import com.laba.solvd.bank.dao.interfaces.TransactionRepository;
import com.laba.solvd.bank.model.Transaction;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {

    private static final Logger log = Logger.getLogger(TransactionRepositoryImpl.class.getName());
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();

    private  String FIND_ALL="SELECT account_id, COUNT(*) FROM transactions GROUP BY account_id;";
    private String CREATE ="INSERT INTO Transactions (transaction_type, amount,transaction_date) VALUES (?, ?,?)";
    private String UPDATE = "UPDATE transactions SET amount = ? WHERE id = ?";

    @Override
    public void create(Transaction transaction) {
        Connection connection = CONNECTION_POOL.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, transaction.getTransactionType());
            preparedStatement.setDouble(2, transaction.getAmount());
            preparedStatement.setDate(3,transaction.getTransactionDate());
            preparedStatement.executeUpdate();

            ResultSet ResultSet = preparedStatement.getGeneratedKeys();
            while (ResultSet.next()) {
                transaction.setId(ResultSet.getLong("id"));
            }
        } catch (SQLException e) {
            log.info("Unable to create Transaction", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }
    @Override
    public List<Transaction> findAll() {
        Connection connection =CONNECTION_POOL.getConnection();
        List<Transaction> transactionList=new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet=statement.executeQuery()) {
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(resultSet.getLong("id"));
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setTransactionType(resultSet.getString("transaction_type"));
                transaction.setTransactionDate(resultSet.getDate("transaction_date"));
                transactionList.add(transaction);
            }
        }catch (SQLException e) {
            log.info("Unable to find Transactions!",e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return transactionList;
}

    @Override
    public void update(Transaction transaction) {
        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
                statement.setDouble(1, transaction.getAmount());
                statement.setLong(2, transaction.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
            log.error("Unable to update the transaction!", e);
        }finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    private static Transaction findById(Long id, List<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> transaction.getId().equals(id))
                .findFirst().orElseGet(() -> {
                    Transaction newTransaction = new Transaction();
                    newTransaction.setId(id);
                    transactions.add(newTransaction);
                    return newTransaction;
                });
    }
    public static List<Transaction> mapRow(ResultSet resultSet,List<Transaction> transactions) throws SQLException{
        long id=resultSet.getLong("id");
        if(id !=0){
            if(transactions==null){
                transactions=new ArrayList<>();
            }

            Transaction transaction=findById(id,transactions);
            transaction.setAmount(resultSet.getDouble("amount"));
            transaction.setTransactionType(resultSet.getString("transaction_type"));
            transaction.setTransactionDate(resultSet.getDate("transaction_date"));
        }
        return transactions;
    }
}
