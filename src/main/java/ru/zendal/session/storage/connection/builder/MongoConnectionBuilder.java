package ru.zendal.session.storage.connection.builder;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import ru.zendal.session.storage.connection.ConnectionBuilder;

public class MongoConnectionBuilder implements ConnectionBuilder<MongoClient> {


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
    public MongoClient build() {
        return MongoClients.create("mongodb://" + host + ":" + port);
    }
}
