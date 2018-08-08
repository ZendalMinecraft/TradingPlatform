/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.session.storage.connection.builder;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteBuilder;
import ru.zendal.session.storage.connection.ConnectionBuilder;

public class NitriteConnectionBuilder implements ConnectionBuilder<Nitrite> {

    private String path = "plugins/TradingPlatform/";

    private String fileName = "TradingPlatform.db";

    private String user = null;

    private String password = null;


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
        NitriteBuilder builder = Nitrite.builder().compressed().filePath(path + fileName);
        if (user != null && password != null) {
            return builder.openOrCreate(user, password);
        } else {
            return builder.openOrCreate();
        }
    }

    @Override
    public boolean hasConnected() {
        return true;
    }
}
