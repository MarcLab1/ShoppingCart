package com.shoppingcart.room;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.shoppingcart.HelperFormatter;

@Entity
public class Item implements Parcelable {
    @PrimaryKey (autoGenerate = true)
    private int itemId;
    private double regularPrice, savePrice;
    private String name;
    private String brand;
    private String weight;
    private int inventory;
    private String category;
    private int count ; //= 0;
    private int image;
    private boolean isChecked = false;

    public Item() {

    }

    protected Item(Parcel in) {
        itemId = in.readInt();
        name = in.readString();
        brand = in.readString();
        weight = in.readString();
        regularPrice = in.readDouble();
        savePrice = in.readDouble();
        inventory = in.readInt();
        category = in.readString();
        count = in.readInt();
        image = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(double regular_price) {
        this.regularPrice = regular_price;
    }

    public double getPrice() {
        return HelperFormatter.getTotal(regularPrice - savePrice);
    }


    public double getSavePrice() {
        return savePrice;
    }

    public void setSavePrice(double save_price) {
        this.savePrice = save_price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemId);
        dest.writeString(name);
        dest.writeString(brand);
        dest.writeString(weight);
        dest.writeDouble(regularPrice);
        dest.writeDouble(savePrice);
        dest.writeInt(inventory);
        dest.writeString(category);
        dest.writeInt(count);
        dest.writeInt(image);
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void increaseCount()
    {
        count++;
    }

    public void decreaseCount()
    {
        count--;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
