package me.xlucash.xlmobcoins.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerDataManager {
    private final DatabaseManager databaseManager;

    public PlayerDataManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public double getCoins(UUID uuid) {
        String query = "SELECT coins FROM mobcoins WHERE uuid = ?";
        try (Connection connection = databaseManager.getDatabaseConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("coins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setCoins(UUID uuid, double coins) {
        try (Connection connection = databaseManager.getDatabaseConnection();
             PreparedStatement ps = connection.prepareStatement("REPLACE INTO mobcoins (uuid, coins) VALUES (?, ?)")) {
            ps.setString(1, uuid.toString());
            ps.setDouble(2, coins);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCoins(UUID uuid, double coinsToAdd) {
        setCoins(uuid, getCoins(uuid) + coinsToAdd);
    }

    public boolean removeCoins(UUID uuid, double coinsToRemove) {
        double currentCoins = getCoins(uuid);
        if (currentCoins < coinsToRemove) {
            return false;
        }
        setCoins(uuid, currentCoins - coinsToRemove);
        return true;
    }
}
