package ru.zendal.session.storage;

import ru.zendal.session.Session;

import java.util.List;

public interface StorageSessions {

    /**
     * Save  session into Storage
     *
     * @param session Offline session
     * @return unique Id session
     */
    String saveSession(Session session);

    /**
     * Get all Sessions
     *
     * @return List Sessions
     */
    List<Session> getAllSessions();

}
