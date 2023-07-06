package com.laba.solvd.bank.dao.mapper;

import com.laba.solvd.bank.config.MyBatis;
import com.laba.solvd.bank.dao.interfaces.CustomerRepository;
import com.laba.solvd.bank.model.Customer;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CustomerMapperImpl implements CustomerRepository {

    private static final Logger logger = LogManager.getLogger(CustomerMapperImpl.class.getName());
    private static final MyBatis MY_BATIS = MyBatis.getInstance();

    @Override
    public void create(Customer customer) {
        try (SqlSession session = MY_BATIS.getSession();) {
            session.insert("create",customer);
            session.commit();
            logger.info("Customer created in SqlSession");
        }
    }

    @Override
    public List<Customer> findAll() {
        try (SqlSession session = MY_BATIS.getSession();) {
            logger.info("Find all customers in SqlSession");
            return session.selectList("findAll");
        }
    }
}
