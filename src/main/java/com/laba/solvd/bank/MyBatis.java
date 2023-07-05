package com.laba.solvd.bank;

import com.laba.solvd.bank.dao.interfaces.AccountRepository;
import com.laba.solvd.bank.dao.interfaces.CustomerRepository;
import com.laba.solvd.bank.dao.interfaces.Factory;
import com.laba.solvd.bank.dao.interfaces.TransactionRepository;
import com.laba.solvd.bank.mapper.AccountMapper;
import com.laba.solvd.bank.mapper.CustomerMapper;
import com.laba.solvd.bank.mapper.TransactionMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatis implements Factory {
    private static SqlSessionFactory sqlSessionFactory;
    public MyBatis() {
    }
    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            synchronized (MyBatis.class) {
                if (sqlSessionFactory == null) {
                    String resource = "mybatis-config.xml";
                    InputStream inputStream;
                    try {
                        inputStream = Resources.getResourceAsStream(resource);
                    } catch (IOException e) {
                        throw new RuntimeException("Error loading MyBatis configuration file.", e);
                    }
                    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                }
            }
        }
        return sqlSessionFactory;
    }
    @Override
    public CustomerRepository createCustomerRepository() {
        return new CustomerMapper(createSqlSession());
    }

    @Override
    public AccountRepository createAccountRepository() {
        return new AccountMapper(createSqlSession());
    }

    @Override
    public TransactionRepository createTransactionRepository() {
        return new TransactionMapper(createSqlSession());
    }
    public SqlSession createSqlSession() {
        return sqlSessionFactory.openSession();
    }
}
