package com.laba.solvd.bank.mapper;

import com.laba.solvd.bank.dao.interfaces.AccountRepository;
import com.laba.solvd.bank.model.Account;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class AccountMapper implements AccountRepository {
    private final SqlSession session;

    public AccountMapper(SqlSession session) {
        this.session = session;
    }

    @Override
    public void create(Account account) {
        session.insert("create",account);
    }

    @Override
    public List<Account> findAll() {
        return session.selectList("findAll");
    }

    @Override
    public void update(Account account) {
        session.update("update",account);

    }
}
