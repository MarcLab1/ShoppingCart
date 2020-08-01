package com.shoppingcart.ui.home;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.shoppingcart.HelperFormatter;
import com.shoppingcart.room.Item;
import com.shoppingcart.room.ItemDB;
import com.shoppingcart.R;
import com.shoppingcart.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private MyViewModel myViewModel;
    private List<Item> itemList;
    private RecyclerView itemView;
    private FlexboxAdapter flexboxAdapter;
    private static ItemDB itemDB;
    private NavController navController;
    private View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);

        root = inflater.inflate(R.layout.fragment_home, container, false);
        itemView = root.findViewById(R.id.items_view);

        itemList = myViewModel.getItems().getValue();

        itemDB = Room.databaseBuilder(getContext(), ItemDB.class, "dbone").build();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (!preferences.getBoolean("firstrun", false)) {
            new DBAsync().execute();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstrun", true).apply();
        }
        new QueryItems().execute();

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.FLEX_START);
        itemView.setLayoutManager(layoutManager);

        //Required to inflate the options in the menubar
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(root);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.cart) {
            navController.navigate(R.id.action_navigation_home_to_navigation_cart);
        }
        return super.onOptionsItemSelected(item);
    }

    private static class DBAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Item item1 = new Item();
            item1.setName("Eggs");
            item1.setBrand("Omega 3");
            item1.setRegularPrice(2.99);
            item1.setSavePrice(0.50);
            item1.setWeight("12 count");
            item1.setInventory(10);
            item1.setImage(R.drawable.eggs);
            itemDB.getItemDao().insert(item1);

            Item item2 = new Item();
            item2.setName("Crackers");
            item2.setBrand("Breton");
            item2.setRegularPrice(3.59);
            item2.setSavePrice(0);
            item2.setWeight("225g");
            item2.setInventory(10);
            item2.setImage(R.drawable.crackers);
            itemDB.getItemDao().insert(item2);

            Item item3 = new Item();
            item3.setName("Orange Juice");
            item3.setBrand("Tropicana");
            item3.setRegularPrice(4.99);
            item3.setSavePrice(0);
            item3.setInventory(10);
            item3.setWeight("1.54L");
            item3.setImage(R.drawable.orange_juice);
            itemDB.getItemDao().insert(item3);

            Item item4 = new Item();
            item4.setName("Lettuce");
            item4.setRegularPrice(1.99);
            item4.setSavePrice(0);
            item4.setInventory(10);
            item4.setImage(R.drawable.lettuce);
            itemDB.getItemDao().insert(item4);

            Item item5 = new Item();
            item5.setName("Carrots");
            item5.setBrand("Farmers Market");
            item5.setRegularPrice(3.99);
            item5.setSavePrice(1.00);
            item5.setInventory(10);
            item5.setWeight("3lb bag");
            item5.setImage(R.drawable.carrots);
            itemDB.getItemDao().insert(item5);

            Item item6 = new Item();
            item6.setName("Burgers");
            item6.setBrand("PC");
            item6.setRegularPrice(18.99);
            item6.setSavePrice(5.00);
            item6.setInventory(10);
            item6.setWeight("1.13kg");
            item6.setImage(R.drawable.burgers);
            itemDB.getItemDao().insert(item6);

            Item item7 = new Item();
            item7.setName("English Muffins");
            item7.setRegularPrice(2.99);
            item7.setSavePrice(1.00);
            item7.setInventory(10);
            item7.setWeight("390g");
            item7.setBrand("Wonder");
            item7.setImage(R.drawable.eng_muffin);
            itemDB.getItemDao().insert(item7);

            Item item8 = new Item();
            item8.setName("Tomato");
            item8.setRegularPrice(1.00);
            item8.setSavePrice(0);
            item8.setInventory(10);
            item8.setWeight("50g");
            item8.setImage(R.drawable.tomato);
            itemDB.getItemDao().insert(item8);

            Item item9 = new Item();
            item9.setName("Peanut Butter");
            item9.setRegularPrice(4.99);
            item9.setSavePrice(2.00);
            item9.setInventory(10);
            item9.setWeight("1kg");
            item9.setBrand("Kraft");
            item9.setImage(R.drawable.peanut_butter);
            itemDB.getItemDao().insert(item9);

            Item item10 = new Item();
            item10.setName("Skittles");
            item10.setRegularPrice(2.99);
            item10.setSavePrice(1.00);
            item10.setInventory(10);
            item10.setWeight("191g");
            item10.setImage(R.drawable.skittles);
            itemDB.getItemDao().insert(item10);

            Item item11 = new Item();
            item11.setName("2% Milk");
            item11.setBrand("Natrel");
            item11.setRegularPrice(4.99);
            item11.setSavePrice(1.00);
            item11.setWeight("4L");
            item11.setInventory(10);
            item11.setImage(R.drawable.milk);
            itemDB.getItemDao().insert(item11);

            Item item12 = new Item();
            item12.setName("Cheese Slices");
            item12.setBrand("Kraft");
            item12.setRegularPrice(3.79);
            item12.setSavePrice(1.00);
            item12.setWeight("410g");
            item12.setInventory(10);
            item12.setImage(R.drawable.cheese);
            itemDB.getItemDao().insert(item12);

            return null;
        }
    }

    private class QueryItems extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            if (itemList == null)
                itemList = itemDB.getItemDao().getAllItemsInStock();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            flexboxAdapter = new FlexboxAdapter(itemList);
            itemView.setAdapter(flexboxAdapter);
            flexboxAdapter.notifyDataSetChanged();
        }
    }


    public class FlexboxAdapter extends RecyclerView.Adapter<FlexboxAdapter.ViewHolder> {

        private List<Item> items;

        public FlexboxAdapter(List<Item> itemList) {
            items = new ArrayList<>();
            items = itemList;
        }

        @Override
        public FlexboxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new FlexboxAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final FlexboxAdapter.ViewHolder holder, int position) {

            final Item itm = items.get(position);
            holder.name.setText(itm.getName());
            holder.brand.setText(itm.getBrand());
            holder.price.setText(HelperFormatter.getTotalString(itm.getPrice()));
            if (itm.getSavePrice() != 0)
                holder.save_price.setText(HelperFormatter.getSaveString(itm.getSavePrice()));
            holder.weight.setText(itm.getWeight());
            holder.image.setBackgroundResource(itm.getImage());

            if (itm.getCount() != 0) {
                holder.layout_quantity.setVisibility(View.VISIBLE);
                holder.layout_add.setVisibility(View.GONE);
            }
            holder.count.setText(String.valueOf(itm.getCount()));

            holder.increase_quantity.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if (itemList.indexOf(itm) == -1)
                                                                    return;

                                                                itemList.get(itemList.indexOf(itm)).increaseCount();
                                                                myViewModel.getItems().setValue(itemList);
                                                                holder.count.setText(String.valueOf(itm.getCount()));
                                                            }
                                                        }
            );

            holder.button_add.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         if (itemList.indexOf(itm) == -1)
                                                             return;

                                                         itemList.get(itemList.indexOf(itm)).setCount(1);
                                                         myViewModel.getItems().setValue(itemList);
                                                         holder.count.setText(String.valueOf(itm.getCount()));
                                                         holder.layout_quantity.setVisibility(View.VISIBLE);
                                                         holder.layout_add.setVisibility(View.GONE);
                                                     }
                                                 }
            );


            holder.decrease_quantity.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if (itemList.indexOf(itm) == -1)
                                                                    return;

                                                                itemList.get(itemList.indexOf(itm)).decreaseCount();

                                                                if (itemList.get(itemList.indexOf(itm)).getCount() == 0) {

                                                                    holder.layout_quantity.setVisibility(View.GONE);
                                                                    holder.layout_add.setVisibility(View.VISIBLE);
                                                                }
                                                                myViewModel.getItems().setValue(itemList);
                                                                holder.count.setText(String.valueOf(itm.getCount()));
                                                            }
                                                        }
            );
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name, brand, price, save_price, weight, count, button_add;
            private ImageView increase_quantity, decrease_quantity;
            private LinearLayout layout_quantity, layout_add;
            private ImageView image;

            ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.item_name);
                brand = itemView.findViewById(R.id.item_brand);
                price = itemView.findViewById(R.id.item_price);
                save_price = itemView.findViewById(R.id.item_save_price);
                weight = itemView.findViewById(R.id.item_weight);
                count = itemView.findViewById(R.id.item_product_count);
                image = itemView.findViewById(R.id.item_image);
                increase_quantity = itemView.findViewById(R.id.button_increase_quantity);
                decrease_quantity = itemView.findViewById(R.id.button_decrease_quantity);
                button_add = itemView.findViewById(R.id.button_add);

                layout_quantity = itemView.findViewById(R.id.layout_quantity);
                layout_add = itemView.findViewById(R.id.layout_add);
            }
        }
    }
}
