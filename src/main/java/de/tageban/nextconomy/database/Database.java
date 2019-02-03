package de.tageban.nextconomy.database;

public interface Database {
    void Connect();
    void Disconnect();
    double getBalance(String uuid);
    void setBalance(String uuid, double amount);
    void Clear();
    void createTabel();
    boolean isTabelExist();
    boolean isUserExist(String uuid);
    void createUser(String uuid, double amount);

}
