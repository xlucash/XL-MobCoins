package me.xlucash.xlmobcoins.database.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.xlucash.xlmobcoins.config.ConfigManager;
import me.xlucash.xlmobcoins.database.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLDatabase extends Database {
    private HikariDataSource dataSource;
    private final ConfigManager configManager;

    public MySQLDatabase(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setup() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + configManager.getMySQLHost() + ":" + configManager.getMySQLPort() + "/" + configManager.getMySQLDatabase());
        config.setUsername(configManager.getMySQLUsername());
        config.setPassword(configManager.getMySQLPassword());
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(60000);
        config.setIdleTimeout(1740000);
        config.setMaxLifetime(1740000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("tcpKeepAlive", "true");
        config.addDataSourceProperty("autoReconnect", "true");

        dataSource = new HikariDataSource(config);
        logConnectionSuccess("MySQL");
    }
}