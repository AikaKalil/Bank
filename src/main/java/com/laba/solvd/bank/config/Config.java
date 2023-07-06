package com.laba.solvd.bank.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum Config {
    URL("url"),
    DRIVER("driver"),
    USERNAME("username"),
    PASSWORD("password"),
    CONNECTION_METHOD("connection_method"),
    POOL_SIZE("pool_size", String.valueOf(1));

    private static final String CONFIG_FILE_NAME = "src/main/resources/config.properties";
    private static Properties PROPERTIES;
    private final String key;
    private String defaultValue;


    static {
        PROPERTIES = loadProperties();
    }
    private static Properties loadProperties() {
        PROPERTIES = new Properties();
        try(FileInputStream ls = new FileInputStream(CONFIG_FILE_NAME)){
            PROPERTIES.load(ls);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't prepare config properties", e);
        }
        return PROPERTIES;
    }
    Config(String key, String defaultValue) {
        this(key);
        this.defaultValue = defaultValue;
    }

    Config(String key) {
        this.key = key;
    }

    public String getValue() {
        return PROPERTIES.getProperty(key, defaultValue);
    }


}
