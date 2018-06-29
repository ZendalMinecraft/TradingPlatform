package ru.zendal.session.storage.connection.builder;

import org.dizitart.no2.Nitrite;
import ru.zendal.session.storage.connection.ConnectionBuilder;

public class NitriteConnectionBuilder implements ConnectionBuilder<Nitrite> {

    private String path = "plugins/TradingPlatform/";

    private String fileName = "TradingPlatform.db";

    private String user = "user";

    private String password = "password";


    public NitriteConnectionBuilder() {
    }


    public void setPath(String path) {
        this.path = path;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Nitrite build() {
        return Nitrite.builder().compressed().filePath(path + fileName).openOrCreate(user, password);
    }

    @Override
    public boolean hasConnected() {
        return false;
    }
}
