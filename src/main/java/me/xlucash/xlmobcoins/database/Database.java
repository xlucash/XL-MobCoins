package me.xlucash.xlmobcoins.database;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Database {
    protected Connection connection;
    public abstract Connection getConnection();
    public abstract void setup();
    protected void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void logConnectionSuccess(String databaseType) {
        Bukkit.getLogger().info("[XL-Mine] Połączono z bazą danych: " + databaseType);
    }
}