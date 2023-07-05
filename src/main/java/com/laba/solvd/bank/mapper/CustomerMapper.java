package com.laba.solvd.bank.mapper;

import com.laba.solvd.bank.dao.interfaces.CustomerRepository;
import com.laba.solvd.bank.model.Customer;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class CustomerMapper implements CustomerRepository {
    private final SqlSession session;

    public CustomerMapper(SqlSession session) {
        this.session = session;
    }

    @Override
    public void create(Customer customer) {
        session.insert("create",customer);
    }

    @Override
    public List<Customer> findAll() {
        return session.selectList("findAll");
    }
}
