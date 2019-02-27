package ru.zendal.core.storage;

import ru.zendal.core.storage.entity.OfflineTradeSession;
import ru.zendal.core.storage.exception.SaveTradeSessionStorageException;

public class MongoTradeSessionStorage extends AbstractTradeSessionStorage {
    /**
     * Instantiates a new Abstract trade session storage.
     *
     * @param itemConvector the item convector
     */
    public MongoTradeSessionStorage(ItemConvector itemConvector) {
        super(itemConvector);
    }

    @Override
    public void saveTradeSession(OfflineTradeSession offlineTradeSession) throws SaveTradeSessionStorageException {
            offlineTradeSession.getUniqMainTrader()
    }
}
