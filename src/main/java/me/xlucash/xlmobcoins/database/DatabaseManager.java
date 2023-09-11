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
            // TODO: Create tables if not exist
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getDatabaseConnection() {
        database.getConnection();
    }

    public void closeDatabaseConnection() {
        if(database != null) {
            database.closeConnection();
        }
    }
}