package com.shoppingcart.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface OrderItemDao {
    @Insert
    void insert(OrderItem... orderItem);
    @Transaction
    @Query("SELECT * FROM OrderItem WHERE oid LIKE :orderId")
    List<OrderItem> getOrderItems(int orderId);

    @Query("SELECT * FROM `Order`")
    List<Order> getOrders();

    @Query("SELECT * FROM Item")
    List<Item> getItems();

    @Query("SELECT * FROM `Order`")
    List<OrderWithItem> getAllItemsWithOrder();
}
