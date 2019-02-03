package de.tageban.nextconomy.database;

import de.tageban.nextconomy.NextConomy;

import java.sql.*;

public class MySQLConnection implements Database {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final NextConomy plugin;
    private Connection connection;

    public MySQLConnection(NextConomy plugin) {
        this.plugin = plugin;
        Config config = new Config(plugin.getDataFolder(), "Database");
        host = config.getConfig().getString("Host");
        port = config.getConfig().getInt("Port");
        database = config.getConfig().getString("Database");
        username = config.getConfig().getString("Username");
        password = config.getConfig().getString("Password");
    }

    public void Connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useUnicode=yes", username, password);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void Disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            connection = null;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createTabel() {
        if (isTabelExist())
            return;
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS nextconomy (UUID VARCHAR(100), balance DOUBLE)");
            statement.executeUpdate();
        }
        catch (SQLException ex) {
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
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isTabelExist() {
        try {
            PreparedStatement ps = connection.prepareStatement("SHOW TABLES LIKE 'nextconomy'");
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void createUser(String uuid, double amount) {
        if (isUserExist(uuid))
            return;
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO nextconomy (UUID, balance) VALUES(?,?)");
            ps.setString(1, uuid);
            ps.setDouble(2, amount);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isUserExist(String uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT UUID FROM nextconomy WHERE UUID = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
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
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM nextconomy WHERE UUID = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                return rs.getInt("balance");
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return plugin.getStartBalance();
    }

    public void setBalance(String uuid, double amount) {
        if (!isUserExist(uuid)) {
            createUser(uuid, amount);
            return;
        }
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE nextconomy SET balance = ? WHERE UUID = ?");
            ps.setDouble(1, amount);
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
