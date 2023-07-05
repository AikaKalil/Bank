package com.laba.solvd.bank.mapper;

import com.laba.solvd.bank.dao.interfaces.TransactionRepository;
import com.laba.solvd.bank.model.Transaction;
import org.apache.ibatis.session.SqlSession;
import java.util.List;

public class TransactionMapper implements TransactionRepository {

    private final SqlSession session;

    public TransactionMapper(SqlSession session) {
        this.session = session;
    }

    @Override
    public void create(Transaction transaction) {
        session.insert("create",transaction);

    }

    @Override
    public List<Transaction> findAll() {
        return session.selectList("findAll");
    }

    @Override
    public void update(Transaction transaction) {
        session.update("update",transaction);
    }
}
