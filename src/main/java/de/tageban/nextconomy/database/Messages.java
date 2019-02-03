package de.tageban.nextconomy.database;

public enum Messages {

    Prefix("Prefix","§7[§bNext§aConomy§7]"),
    Balance("Balance","§7Du hast §b{balance}7."),
    BalanceOther("BalanceOther","§a{player} §7hat §b{balance}€§7."),
    PlayerNotExist("PlayerNotExist","§cDer Spieler war noch nie auf den Server."),
    NichtGenugGeld("NichtGenugGeld","§cDu hast nicht genügend Geld."),
    NotPaySelf("NotPaySelf","§cDu kannst nicht dir selber Geld spenden."),
    GetPayedMoney("GetPayedMoney","§7Du hast von §b{player} §a{balance} §7erhalten."),
    MehrGeld("MehrGeld","§cBitte schreibe eine Größere Zahl."),
    PayToggled("PayToggled","§cEr akeptiert keine Spenden."),
    PayedMoney("PayedMoney","§7Du hast §b{player} §a{balance}€ §7gespendet."),
    PayOn("PayOn","§7Du akzeptierst nun §awieder §7Spenden."),
    PayOff("PayOff","§7Du akzeptierst §ckeine §7nun Spenden."),
    PlayerNotOnline("PlayerNotOnline","§cDer Spieler ist nicht Online."),
    onlyPlayers("onlyPlayers","§cDas können nur Spielern machen."),
    NoPerms("NoPerms","§7Du hast §ckeinen §7Zugriff auf diesen Befehl."),
    CommandFail("CommandFail","§cBitte benutze /"),
    Reset("Reset","§7Du hast alle Accounts Resetet."),
    ResetPlayer("ResetPlayer","§7Du hast den Account von §a{player}§7 resetet."),
    AddMoney("AddMoney","§7Du hast §a{player} §b{amount}e §7hinzugefügt."),
    RemoveMoney("RemoveMoney","§7Du hast §a{player} §b{amount}e §7entfernt."),
    KeinZahl("MussZahl","§cBitte geben sie eine Zahl ein."),



    /* MySQL */
    MySQLError("MySQLError","§cEs gab ein Proplem mit MySQL."),
    MySQLCreateTable("MySQLCreateTable","§aEs wurde erfolgreich die MySQL Tabbelle {tabelle} erstellt."),
    MySQLDisconnect("MySQLDisconnect","§aDie verbindung zu MySQL ist abgebrochen."),
    MySQLDisconnectError("MySQLDisconnectError","§cMySQL kann die verbindung abbrechen."),
    MySQLConnectError("MySQLConnectError","§cMySQL kann die verbindung nicht aufbauen."),
    MySQLConnect("MySQLConnect","§aDie verbindung zu MySQL ist aufgebaut.");

    private final String message;
    private final String value;
    Messages(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getMessage() {
        Config config = new Config("plugins/NextConomy","Messages");
        String format;
        if (config.getConfig().getString(value) == null || config.getConfig().getString(value).equals("")) {
            if (value.equals("Prefix")) {
                format = message + " ";
            } else {
                format = Prefix.message + message;
            }
        }else {
            if (value.equals("Prefix")) {
                format = config.getConfig().getString(value) + " ";
            } else {
                format = config.getConfig().getString(Prefix.value) + " " + config.getConfig().getString(value);
            }
        }
        return format;
    }


}
