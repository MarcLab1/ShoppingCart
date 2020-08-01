package com.shoppingcart;

import com.shoppingcart.room.Item;

import java.util.ArrayList;
import java.util.List;

public final class HelperCartItems {

    public static List<Item> getCartItems(List<Item> items) {

        if(items == null)
            return new ArrayList<>();

        List<Item> cartItems = new ArrayList<>();
        for(int i =0; i< items.size(); i++)
        {
            if(items.get(i).getCount() != 0) {
                cartItems.add(items.get(i));
            }
        }
        return cartItems;
    }
}
