package com.shoppingcart.room;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class OrderWithItem {


    @Embedded
    Order order;
    @Relation(
            parentColumn = "orderId",
            entity = Item.class,
            entityColumn = "itemId",
            associateBy = @Junction(
                    value = OrderItem.class,
                    parentColumn = "oid",
                    entityColumn = "iid"
            )
    )
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}


