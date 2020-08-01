package com.shoppingcart.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Item.class, Order.class, OrderItem.class}, version = ItemDB.VERSION)
public abstract class ItemDB extends RoomDatabase {
    static final int VERSION = 1;
    public abstract ItemDao getItemDao();
    public abstract OrderDao getOrderDao();
    public abstract OrderItemDao getOrderItemDao();
}