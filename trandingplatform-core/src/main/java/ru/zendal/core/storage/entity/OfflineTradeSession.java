package ru.zendal.core.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
@Builder
public class OfflineTradeSession {
    private final String uniqMainTrader;

    private final String uniqSecondaryTrader;

    private final Collection<Item> itemsMainTrader;

    private final Collection<Item> itemsSecondaryTrader;



}
