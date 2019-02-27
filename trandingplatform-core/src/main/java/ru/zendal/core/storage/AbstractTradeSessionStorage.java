package ru.zendal.core.storage;

import ru.zendal.core.storage.entity.OfflineTradeSession;
import ru.zendal.core.storage.exception.SaveTradeSessionStorageException;

/**
 * The type Abstract trade session storage.
 */
public abstract class AbstractTradeSessionStorage {


    private final ItemConvector itemConvector;

    /**
     * Instantiates a new Abstract trade session storage.
     *
     * @param itemConvector the item convector
     */
    public AbstractTradeSessionStorage(ItemConvector itemConvector) {
        this.itemConvector = itemConvector;
    }


    /**
     * Save trade session
     *
     * @param offlineTradeSession entity offline trade session
     * @throws SaveTradeSessionStorageException the save trade session storage exception
     */
    abstract public void saveTradeSession(OfflineTradeSession offlineTradeSession) throws SaveTradeSessionStorageException;

    /**
     * Get item convector
     *
     * @return component for convert items
     */
    protected ItemConvector getItemConvector() {
        return itemConvector;
    }
}
