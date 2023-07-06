package com.laba.solvd.bank.factory;

import com.laba.solvd.bank.config.Config;
import com.laba.solvd.bank.config.ConnectionPool;
import com.laba.solvd.bank.config.MyBatis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionMethodFactory {
    private static final Logger logger = LogManager.getLogger(ConnectionMethodFactory.class.getName());
    private static String connectionMethod;

    public ConnectionMethodFactory() {
        connectionMethod =Config.CONNECTION_METHOD.getValue().toLowerCase();
    }

    public IConnectionMethod getMethod() {
        if (connectionMethod == null || connectionMethod.isBlank()) {
            logger.error("Connection method was not provided");
            logger.warn("Default connection method has been set to JDBC");
        }

        if (!connectionMethod.equals("jdbc") && !connectionMethod.equals("mybatis")) {
            logger.error("Provided connection is not valid");
            logger.warn("Default connection method has been set to JDBC");
        }

        if (isMyBatis()) {
            logger.info("Connection method has been set to MyBatis");
            return new MyBatis();
        }

        logger.info("Connection method has been set to JDBC Connection Pool");
        return new ConnectionPool();
    }

    public boolean isMyBatis() {
        if (connectionMethod == null || connectionMethod.isBlank())
            return false;
        return connectionMethod.equals("mybatis");
    }
}