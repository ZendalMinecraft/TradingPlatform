/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session.storage.connection.builder;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.event.ServerHeartbeatFailedEvent;
import com.mongodb.event.ServerHeartbeatStartedEvent;
import com.mongodb.event.ServerHeartbeatSucceededEvent;
import com.mongodb.event.ServerMonitorListener;
import ru.zendal.session.storage.connection.ConnectionBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MongoConnectionBuilder implements ConnectionBuilder<MongoDatabase> {

    private String name;
    private String password;
    private String dataBaseName = "TradingPlatform";

    private int port;
    private String host;

    private boolean hasConnected = false;

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
    public MongoDatabase build() {
        if (name == null || password == null) {
            return this.buildWithOutCredential();
        }

        return null;
    }

    public boolean hasConnected() {
        return hasConnected;
    }

    private MongoDatabase buildWithOutCredential() {
        ConnectionString connectionString = new ConnectionString("mongodb://" + host + ":" + port);
        ClusterSettings clusterSettings = ClusterSettings.builder().serverSelectionTimeout(5, TimeUnit.SECONDS).build();
        MongoClientSettings settings = MongoClientSettings.builder().applyToServerSettings(builder -> builder.addServerMonitorListener(new ServerMonitorListener() {
            @Override
            public void serverHearbeatStarted(ServerHeartbeatStartedEvent event) {
                hasConnected = false;
            }

            @Override
            public void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent event) {
                hasConnected = true;
            }

            @Override
            public void serverHeartbeatFailed(ServerHeartbeatFailedEvent event) {
                hasConnected = false;
            }
        })).applyConnectionString(connectionString).
                applyToClusterSettings(builder -> builder.applySettings(clusterSettings)).build();

        try {
            MongoClients.create(settings).listDatabaseNames().first();
        } catch (MongoTimeoutException e) {
            hasConnected = false;
        }
        return MongoClients.create(settings).getDatabase(dataBaseName);
    }
}
