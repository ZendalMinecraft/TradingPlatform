package ru.zendal.session.storage;

import ru.zendal.session.TradeOffline;
import ru.zendal.session.TradeOfflineSession;

import java.util.List;

public class MySqlSessionStorage implements SessionsStorage {

    public MySqlSessionStorage()

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public String saveSession(TradeOfflineSession session) {
        return null;
    }

    @Override
    public List<TradeOffline> getAllSessions() {
        return null;
    }

    @Override
    public void removeTradeOffline(TradeOffline tradeOffline) {

    }
}
