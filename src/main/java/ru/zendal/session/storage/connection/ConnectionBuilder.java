package ru.zendal.session.storage.connection;

public interface ConnectionBuilder<T> {

    /**
     * Get connection
     * @return
     */
    T build();
}
