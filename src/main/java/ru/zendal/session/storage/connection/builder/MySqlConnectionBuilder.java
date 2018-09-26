package ru.zendal.session.storage.connection.builder;

import ru.zendal.session.storage.MySqlSessionStorage;
import ru.zendal.session.storage.connection.ConnectionBuilder;

import java.sql.DriverManager;

public class MySqlConnectionBuilder implements ConnectionBuilder<MySqlSessionStorage> {

    private final String defaultConnectionUrl = "jdbc:mysql://";

    private String host;
    private int port;

    private String dataBaseName;

    private String userName;


    public MySqlConnectionBuilder() {
        this.host = "localhost";
        this.port = 3306;
    }

    public MySqlConnectionBuilder setDataBase(String dataBaseName) {
        this.dataBaseName = dataBaseName;
        return this;
    }


    /**
     * Setup user name for DataBase MySQL
     *
     * @param userName user name dataBase
     * @return self
     */
    public MySqlConnectionBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Override
    public MySqlSessionStorage build() {
        return DriverManager.getConnection(
                this.buildConnectionUrl(),
                userName,
                ""
        );
    }


    private String buildConnectionUrl() {
        return defaultConnectionUrl +
                host + ":" + port + "/" + dataBaseName;
    }

    @Override
    public boolean hasConnected() {
        return false;
    }
}
