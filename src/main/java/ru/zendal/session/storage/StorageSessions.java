package ru.zendal.session.storage;

import ru.zendal.session.TradeOfflineSession;
import ru.zendal.session.TradeSession;

import java.util.List;

public interface StorageSessions {


    void saveSession(TradeSession session);

    void saveOfflineSession(TradeOfflineSession session);

    List<TradeSession> getAllSessions();

    List<TradeOfflineSession> getAllOfflineSessions();
}
