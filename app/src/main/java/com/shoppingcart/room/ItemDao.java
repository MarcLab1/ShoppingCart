package com.shoppingcart.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item... item);

    @Query("SELECT * FROM Item WHERE inventory >= 0")
    List<Item> getAllItemsInStock();

    @Query("SELECT * FROM Item WHERE itemId LIKE :itemId")
    Item getItemByItemId(int itemId);

}
