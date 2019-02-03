package de.tageban.nextconomy.database;

import de.tageban.nextconomy.NextConomy;

import java.io.File;
import java.sql.*;

public class SQLiteConnection implements Database {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final NextConomy plugin;
    private Connection connection;

    public SQLiteConnection(NextConomy plugin) {
        this.plugin = plugin;
        Config config = new Config(plugin.getDataFolder(), "Database");
        host = config.getConfig().getString("Host");
        port = config.getConfig().getInt("Port");
        database = config.getConfig().getString("Database");
        username = config.getConfig().getString("Username");
        password = config.getConfig().getString("Password");
    }

    public void Connect() {
        File db = new File(plugin.getDataFolder(), "Database.db");
        if (!db.getParentFile().exists()) {
            db.getParentFile().mkdirs();
        }
        if (!db.exists()) {
            db.mkdirs();
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/Database.db");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void Disconnect() {
        try {
            connection.close();
            connection = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void Clear() {
        if (!isTabelExist()) {
            createTabel();
        }
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM nextconomy");
            statement.executeUpdate();
            //statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createTabel() {
        if (isTabelExist())
            return;
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS nextconomy (UUID VARCHAR(100), balance DOUBLE)");
            statement.executeUpdate();
            //statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isTabelExist() {/*
        try {
            PreparedStatement ps = connection.prepareStatement("SHOW TABLES LIKE 'nextconomy'");
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }*/
        return false;
    }

    public void createUser(String uuid, double amount) {
        if (isUserExist(uuid))
            return;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO nextconomy (UUID, balance) VALUES(?,?)");
            statement.setString(1, uuid);
            statement.setDouble(2, amount);
            statement.executeUpdate();
            //statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isUserExist(String uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT UUID FROM nextconomy WHERE UUID = ?");
            statement.setString(1, uuid);
            ResultSet rs = statement.executeQuery();
            //statement.close();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public double getBalance(String uuid) {
        if (!isUserExist(uuid)) {
            createUser(uuid, plugin.getStartBalance());
            return plugin.getStartBalance();
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM nextconomy WHERE UUID = ?");
            statement.setString(1, uuid);
            ResultSet rs = statement.executeQuery();
            //statement.close();
            while (rs.next())
                return rs.getInt("balance");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void setBalance(String uuid, double amount) {
        if (!isUserExist(uuid)) {
            createUser(uuid, amount);
            return;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE nextconomy SET balance = ? WHERE UUID = ?");
            statement.setDouble(1, amount);
            statement.setString(2, uuid);
            statement.executeUpdate();
            //statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}