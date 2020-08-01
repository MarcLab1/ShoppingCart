package com.shoppingcart.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insert(Order... order);

    @Query("SELECT * FROM `Order` ")
    List<Order> getAllOrders();

    @Query("SELECT * FROM `Order` WHERE email LIKE :email AND date LIKE :date")
    Order getOrderByEmailAndDate(String email, long date);

    @Query("SELECT * FROM `Order` WHERE orderId LIKE :orderId")
    Order getOrderByOrderId(int orderId);

    @Query("SELECT * FROM `Order` WHERE orderId LIKE :orderId")
    OrderWithItem getItemsOfOrders(int orderId);

}
