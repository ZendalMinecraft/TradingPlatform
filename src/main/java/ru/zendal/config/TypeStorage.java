/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.config;

public enum TypeStorage {
    MONGO_DB("MongoDB"),
    MYSQL_DB("MySQL"),
    LOCAL_STORAGE("Local");

    private String name;

    TypeStorage(String typeStorage) {
        this.name = typeStorage;
    }

    @Override
    public String toString(){
        return name;
    }

    public static TypeStorage fromName(String name) {
        for (TypeStorage typeStorage : TypeStorage.values()) {
            if (typeStorage.toString().equalsIgnoreCase(name)) {
                return typeStorage;
            }
        }
        return null;
    }

    public static boolean hasTypeStorage(String name) {
        return TypeStorage.fromName(name) != null;
    }
}
