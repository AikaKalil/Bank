package com.laba.solvd.bank.dao.mapper;

import com.laba.solvd.bank.config.MyBatis;
import com.laba.solvd.bank.dao.interfaces.AccountRepository;
import com.laba.solvd.bank.model.Account;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class AccountMapperImpl implements AccountRepository {
    private static final Logger logger = LogManager.getLogger(AccountMapperImpl.class.getName());
    private static final MyBatis MY_BATIS = MyBatis.getInstance();

    @Override
    public void create(Account account) {
        try (SqlSession session = MY_BATIS.getSession();) {
            session.insert("create", account);
            session.commit();
            logger.info("Account created in SqlSession");
        }
    }

    @Override
    public List<Account> findAll() {
        try (SqlSession session = MY_BATIS.getSession();) {
            logger.info("Find all accounts in SqlSession");
            return session.selectList("findAll");
        }
    }

    @Override
    public void update(Account account) {
        try (SqlSession session = MY_BATIS.getSession();) {
            session.update("update", account);
            session.commit();
            logger.info("Account updated in SqlSession");

        }
    }
}
