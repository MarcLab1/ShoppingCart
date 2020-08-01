package com.shoppingcart.room;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.shoppingcart.HelperFormatter;

@Entity
public class Order implements Parcelable {
    @PrimaryKey (autoGenerate = true)
    private int orderId;
    private long date;
    private String email;
    private int numberOfItems;
    private double orderTotal;
    private double orderSubtotal;
    private double taxes;
    private double fees;

    public Order() {

    }

    protected Order(Parcel in) {
        orderId = in.readInt();
        date = in.readLong();
        email = in.readString();
        numberOfItems = in.readInt();
        orderTotal = in.readDouble();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getDateString()
    {
        return HelperFormatter.getFormattedDateEEEMMMdyyyhmma(date);
    }

    public double getOrderSubtotal() {
        return orderSubtotal;
    }

    public void setOrderSubtotal(double orderSubtotal) {
        this.orderSubtotal = orderSubtotal;
    }

    public double getTaxes() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderId);
        dest.writeLong(date);
        dest.writeString(email);
        dest.writeInt(numberOfItems);
        dest.writeDouble(orderTotal);
    }
}
