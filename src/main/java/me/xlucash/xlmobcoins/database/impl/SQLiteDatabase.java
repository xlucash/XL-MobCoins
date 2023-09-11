package me.xlucash.xlmobcoins.database.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.xlucash.xlmobcoins.MobCoinsMain;
import me.xlucash.xlmobcoins.database.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteDatabase extends Database {
    private HikariDataSource dataSource;
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

        config.setJdbcUrl("jdbc:sqlite:" + MobCoinsMain.getPlugin(MobCoinsMain.class).getDataFolder() + "/database.db");
        config.setMaximumPoolSize(50);

        dataSource = new HikariDataSource(config);
        logConnectionSuccess("SQLite");
    }
}
