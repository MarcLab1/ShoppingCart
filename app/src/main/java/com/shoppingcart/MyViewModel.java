package com.shoppingcart;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shoppingcart.room.Item;

import java.util.List;

public class MyViewModel extends ViewModel {
    private MutableLiveData<List<Item>> items;

    public MyViewModel() {
        items = new MutableLiveData<>();

    }

    public MutableLiveData<List<Item>> getItems() {
        if (items == null) {
            items = new MutableLiveData<List<Item>>();
        }
        return items;
    }


    public void clearCountsToZero()
    {
        for(int i=0; i< items.getValue().size(); i++)
        {
            items.getValue().get(i).setCount(0);
        }
    }

}
