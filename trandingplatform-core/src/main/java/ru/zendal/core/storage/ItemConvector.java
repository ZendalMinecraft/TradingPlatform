package ru.zendal.core.storage;

import ru.zendal.core.storage.entity.Item;

public interface ItemConvector<I> {


     Item toItem(I item);

     I fromItem(Item item);
}
