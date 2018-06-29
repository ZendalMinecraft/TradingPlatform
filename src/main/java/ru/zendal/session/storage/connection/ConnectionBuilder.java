package ru.zendal.session.storage.connection;

public interface ConnectionBuilder<T> {

    /**
     * Get connection
     *
     * @return
     */
    T build();

    /**
     * Has connected
     * @return {@code true} if has connected else {@code false}
     */
    boolean hasConnected();
}
