package ru.zendal.session;

public interface TradeSessionCallback {

    public void onReady(Session tradeSession);

    public void processTrade(Session tradeSession);
}
