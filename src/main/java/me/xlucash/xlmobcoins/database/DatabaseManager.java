package me.xlucash.xlmobcoins.database;

import me.xlucash.xlmobcoins.MobCoinsMain;
import me.xlucash.xlmobcoins.config.ConfigManager;
import me.xlucash.xlmobcoins.database.impl.MySQLDatabase;
import me.xlucash.xlmobcoins.database.impl.SQLiteDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private Database database;
    private final ConfigManager configManager;

    public DatabaseManager(MobCoinsMain plugin) {
        this.configManager = new ConfigManager(plugin);

        String databaseType = configManager.getDatabaseType();
        if (databaseType.equalsIgnoreCase("mysql")) {
            database = new MySQLDatabase(configManager);
        } else if (databaseType.equalsIgnoreCase("sqlite")) {
            database = new SQLiteDatabase();
        }

        database.setup();
        createTablesIfNotExist();
    }

    private void createTablesIfNotExist() {
        try (Connection connection = database.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS mobcoins (uuid VARCHAR(36) NOT NULL PRIMARY KEY, coins DOUBLE NOT NULL);"
            );
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS shop_items (id INT AUTO_INCREMENT PRIMARY KEY, category VARCHAR(50), item_id INT, stock INT, rotation_time TIMESTAMP)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getDatabaseConnection() {
        try {
            return database.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void closeDatabaseConnection() {
        if(database != null) {
            database.closeConnection();
        }
    }
}