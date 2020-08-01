package com.shoppingcart.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shoppingcart.HelperCartItems;
import com.shoppingcart.HelperFormatter;
import com.shoppingcart.R;
import com.shoppingcart.room.Item;
import com.shoppingcart.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private MyViewModel myViewModel;
    private RecyclerView cartItemView;
    private List<Item> itemList;
    private List<Item> cartItemList;
    private ItemsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private NavController navController;
    private View root;
    private LinearLayout emptyCartLayout, cartLayout;
    private TextView checkoutSubtotal;
    private Button checkoutButton;
    private double subtotal;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cart, container, false);
        emptyCartLayout = root.findViewById(R.id.empty_cart_layout);
        cartLayout = root.findViewById(R.id.cart_layout);
        checkoutButton = root.findViewById(R.id.checkout_button);
        checkoutSubtotal = root.findViewById(R.id.checkout_subtotal);

        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        itemList = myViewModel.getItems().getValue();

        cartItemList = new ArrayList<>();
        cartItemList = HelperCartItems.getCartItems(myViewModel.getItems().getValue());
        if ( cartItemList.size() == 0) {
            displayShoppingCartEmpty();
            return root;
        }
        populateSubtotal();

        cartItemView = root.findViewById(R.id.cart_items_view);

        layoutManager = new LinearLayoutManager(getContext());
        cartItemView.setLayoutManager(layoutManager);

        adapter = new ItemsAdapter(cartItemList);
        cartItemView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_navigation_cart_to_navigation_checkout);
            }
        });

        final Observer<List<Item>> itemsObserver = new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable final List<Item> its) {
                // Update the UI,
               populateSubtotal();
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        myViewModel.getItems().observe(getActivity(), itemsObserver);

        //need this to inflate the options in the menubar
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
        inflater.inflate(R.menu.cart_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.proceedtocheckout) {
            navController.navigate(R.id.action_navigation_cart_to_navigation_checkout);
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayShoppingCartEmpty() {
        emptyCartLayout.setVisibility(View.VISIBLE);
        cartLayout.setVisibility(View.GONE);
        setHasOptionsMenu(false);
    }

    private void populateSubtotal() {
        if (cartItemList == null)
            return;
        subtotal = 0;
        for (int i = 0; i < cartItemList.size(); i++) {
            subtotal += cartItemList.get(i).getCount() * cartItemList.get(i).getPrice();
        }
        subtotal = HelperFormatter.getTotal(subtotal);
        checkoutSubtotal.setText(HelperFormatter.getTotalString(subtotal));
    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
        private List<Item> items;

        ItemsAdapter(List<Item> itemList) {
            items = new ArrayList<>();
            items = itemList;
        }

        @NonNull
        @Override
        public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_cart, parent, false);
            return new ItemsAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemsAdapter.ViewHolder holder, final int position) {
            final Item itm = items.get(position);

            holder.name.setText(itm.getName());
            holder.brand.setText(itm.getBrand());
            holder.weight.setText(itm.getWeight());
            holder.pricePerItem.setText(HelperFormatter.getTotalString(itm.getPrice()));
            holder.count.setText(String.valueOf(itm.getCount()));
            holder.image.setBackgroundResource(itm.getImage());
            updateTotals(holder, itemList.indexOf(itm));

            holder.increase_quantity.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                int index = itemList.indexOf(itm);

                                                                if (index == -1)
                                                                    return;

                                                                itemList.get(index).increaseCount();
                                                                updateTotals(holder, index);

                                                                adapter.notifyDataSetChanged();
                                                                myViewModel.getItems().setValue(itemList);
                                                            }
                                                        }
            );

            holder.decrease_quantity.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                int index = itemList.indexOf(itm);

                                                                if (index == -1)
                                                                    return;

                                                                itemList.get(index).decreaseCount();

                                                                if (itemList.get(index).getCount() == 0) {
                                                                    cartItemList.remove(cartItemList.indexOf(itm));
                                                                }
                                                                updateTotals(holder, index);

                                                                adapter.notifyDataSetChanged();
                                                                myViewModel.getItems().setValue(itemList);

                                                                if (cartItemList.size() == 0) {
                                                                    displayShoppingCartEmpty();
                                                                }
                                                            }
                                                        }
            );

            holder.remove_item.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View v) {
                                                          int index = itemList.indexOf(itm);

                                                          if (index == -1)
                                                              return;

                                                          itemList.get(index).setCount(0);
                                                          updateTotals(holder, index);

                                                          cartItemList.remove(cartItemList.indexOf(itm));
                                                          adapter.notifyDataSetChanged();
                                                          myViewModel.getItems().setValue(itemList);

                                                          if (cartItemList.size() == 0) {
                                                              displayShoppingCartEmpty();
                                                          }
                                                      }
                                                  }
            );
        }

        private void updateTotals(ItemsAdapter.ViewHolder holder, int index)
        {
            holder.count.setText(String.valueOf(itemList.get(index).getCount()));
            holder.priceTotal.setText(HelperFormatter.getTotalString(itemList.get(index).getCount() * itemList.get(index).getPrice() ));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name, brand, weight, priceTotal, pricePerItem, count, remove_item;
            private ImageView increase_quantity, decrease_quantity;
            private ImageView image;

            ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.item_name);
                brand = itemView.findViewById(R.id.item_brand);
                weight = itemView.findViewById(R.id.item_weight);
                priceTotal = itemView.findViewById(R.id.item_price_total);
                pricePerItem = itemView.findViewById(R.id.item_price_per_item);
                count = itemView.findViewById(R.id.item_product_count);
                image = itemView.findViewById(R.id.item_image);
                increase_quantity = itemView.findViewById(R.id.button_increase_quantity);
                decrease_quantity = itemView.findViewById(R.id.button_decrease_quantity);
                remove_item = itemView.findViewById(R.id.button_remove_item);
            }
        }
    }
}
