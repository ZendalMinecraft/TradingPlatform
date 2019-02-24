/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.core.config.server;

public enum TypeSource {
    LOCAL("local"),
    MONGO_DB("mongo_db"),
    MYSQL_DB("mysql_db");

    TypeSource(String typeDataBase) {

    }
}
