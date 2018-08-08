/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config;

public enum TypeStorage {
    MONGO_DB("Mongo"),
    MYSQL_DB("MySQL"),
    LOCAL_STORAGE("Local");

    TypeStorage(String typeStorage) {

    }

    public static TypeStorage fromName(String name) {
        for (TypeStorage typeStorage : TypeStorage.values()) {
            if (typeStorage.name().equalsIgnoreCase(name))
                return typeStorage;
        }
        return null;
    }

    public static boolean hasTypeStorage(String name) {
        return TypeStorage.fromName(name) != null;
    }
}
