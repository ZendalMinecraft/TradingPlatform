package ru.zendal.session.storage.connection.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import ru.zendal.session.storage.connection.ConnectionBuilder;

public class MongoConnectionBuilder implements ConnectionBuilder<MongoConnection> {


    private int port;
    private String host;

    public MongoConnectionBuilder() {
        this.host = "localhost";
        this.port = 27017;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public MongoConnection build() {
        return new MongoConnection() {
            @Override
            public MongoDatabase getDataBase(String nameDataBase) {
                return ;
            }
        };
    }
}
