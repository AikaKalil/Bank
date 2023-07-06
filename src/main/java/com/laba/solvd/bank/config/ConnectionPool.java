package com.laba.solvd.bank.config;


import com.laba.solvd.bank.factory.IConnectionMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.laba.solvd.bank.Main.connectionFactory;

public class ConnectionPool implements IConnectionMethod {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class.getName());

    private static ConnectionPool instance;
    private ArrayList<Connection> connectionPool;

    private String driver;
    private String url;
    private String username;
    private String password;
    private int poolSize;

    public ConnectionPool() {
        initialize();
    }

    @Override
    public void initialize() {
        try {
            driver= Config.DRIVER.getValue();
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.error("Couldn't find Driver class", e);
        }
        url=Config.URL.getValue();
        username=Config.USERNAME.getValue();
        password=Config.PASSWORD.getValue();
        poolSize = Integer.parseInt(Config.POOL_SIZE.getValue());
        connectionPool = new ArrayList<>(poolSize);
        IntStream.range(0, poolSize)
                .boxed()
                .forEach(index -> connectionPool.add(createConnection()));
    }

    public Connection createConnection() {
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't create connection. ", e);
        }
    }

    public Connection getConnection() {
        synchronized (connectionPool) {
            if (connectionPool.isEmpty()) {
                try {
                    while (connectionPool.isEmpty()) {
                        connectionPool.wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException("Couldn't get connection. ", e);
                }
            }
            return connectionPool.remove(connectionPool.size() - 1);

        }
    }

    public void releaseConnection(Connection connection) {
        synchronized (connectionPool) {
            connectionPool.add(connection);
            connectionPool.notifyAll();
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null && !connectionFactory.isMyBatis())
            instance = (ConnectionPool)connectionFactory.getMethod();
        return instance;
    }


}
