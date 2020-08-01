package com.shoppingcart.room;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        primaryKeys = { "oid", "iid" },
        foreignKeys = {
            @ForeignKey(entity = Order.class,
                    parentColumns = "orderId",
                    childColumns = "oid"),
            @ForeignKey(entity = Item.class,
                    parentColumns = "itemId",
                    childColumns = "iid"),

})

public class OrderItem  {
        private int oid;
        private int iid;
        private int quantity;

    public OrderItem(final int orderId, final int itemId, int quantity) {
        this.oid = orderId;
        this.iid = itemId;
        this.quantity = quantity;
    }

    public OrderItem() {

    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}


