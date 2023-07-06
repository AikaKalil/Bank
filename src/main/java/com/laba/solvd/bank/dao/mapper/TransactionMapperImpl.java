package com.laba.solvd.bank.dao.mapper;

import com.laba.solvd.bank.config.MyBatis;
import com.laba.solvd.bank.dao.interfaces.TransactionRepository;
import com.laba.solvd.bank.model.Transaction;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TransactionMapperImpl implements TransactionRepository {

    private static final Logger logger = LogManager.getLogger(TransactionMapperImpl.class.getName());
    private static final MyBatis MY_BATIS = MyBatis.getInstance();
    @Override
    public void create(Transaction transaction) {
        try (SqlSession session = MY_BATIS.getSession();){
            session.insert("create", transaction);
            session.commit();
            logger.info("Transaction created in SqlSession");
        }
    }
    @Override
    public List<Transaction> findAll() {
        try (SqlSession session = MY_BATIS.getSession();) {
            logger.info("Received all transactions");
            return session.selectList("findAll");
        }
    }

    @Override
    public void update(Transaction transaction) {
        try (SqlSession session = MY_BATIS.getSession();) {
            session.update("update", transaction);
            session.commit();
            logger.info("Transaction updated in SqlSession");
        }
        }
}
