package com.shoppingcart.ui.checkout;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.shoppingcart.HelperCartItems;
import com.shoppingcart.HelperFormatter;
import com.shoppingcart.HelperTaxesFees;
import com.shoppingcart.room.Item;
import com.shoppingcart.R;
import com.shoppingcart.room.ItemDB;
import com.shoppingcart.room.Order;
import com.shoppingcart.MyViewModel;
import com.shoppingcart.room.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class CheckoutFragment extends Fragment {

    private TextView checkoutTotal, checkoutSubTotal, checkoutFees, checkoutTaxes, checkoutNumberOfItems;
    private EditText checkoutEmail;
    private Button checkoutButton;
    private NavController navController;
    private View root;
    private MyViewModel myViewModel;
    private double total, subtotal, fees, taxes = 0;
    private String email;
    private int numberOfItems = 0;
    private List<Item> cartItemList;
    private LinearLayout emptyCartLayout, cartLayout;
    private long date;
    private static ItemDB itemDB;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        root = inflater.inflate(R.layout.fragment_checkout, container, false);

        emptyCartLayout = root.findViewById(R.id.empty_cart_layout);
        cartLayout = root.findViewById(R.id.cart_layout);

        cartItemList = new ArrayList<>();
        cartItemList = HelperCartItems.getCartItems(myViewModel.getItems().getValue());
        if (cartItemList.size() == 0) {
            displayShoppingCartEmpty();
            return root;
        }
        itemDB = Room.databaseBuilder(getContext(), ItemDB.class, "dbone").build();

        checkoutButton = root.findViewById(R.id.checkout_button);
        checkoutEmail = root.findViewById(R.id.checkout_email);
        checkoutTotal = root.findViewById(R.id.checkout_total);
        checkoutSubTotal = root.findViewById(R.id.checkout_subtotal);
        checkoutFees = root.findViewById(R.id.checkout_fees);
        checkoutTaxes = root.findViewById(R.id.checkout_taxes);
        checkoutNumberOfItems = root.findViewById(R.id.checkout_numberofitems);
        populateTotals();

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = checkoutEmail.getText().toString() + "";
                date = System.currentTimeMillis();

                Order order = new Order();
                order.setEmail(email);
                order.setDate(date);
                order.setNumberOfItems(numberOfItems);
                order.setOrderSubtotal(subtotal);
                order.setFees(fees);
                order.setTaxes(taxes);
                order.setOrderTotal(total);

                //insert into DB
                new PopulateDatabase(getActivity(), navController, order, cartItemList).execute();

            }
        });


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
        inflater.inflate(R.menu.checkout_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.placeorder) {
            navController.navigate(R.id.action_navigation_checkout_to_navigation_order);
        }
        return super.onOptionsItemSelected(item);
    }


    private void populateTotals() {
        if (cartItemList == null)
            return;

        int count;
        for (int i = 0; i < cartItemList.size(); i++) {
            count = cartItemList.get(i).getCount();
            subtotal += count * cartItemList.get(i).getPrice();
            numberOfItems += count;
        }
        subtotal = HelperFormatter.getTotal(subtotal);

        taxes = HelperTaxesFees.getTaxes();
        fees = HelperTaxesFees.getFees();

        total = (subtotal * taxes) + subtotal + fees;
        total = HelperFormatter.getTotal(total);

        checkoutNumberOfItems.setText(numberOfItems + "");
        checkoutSubTotal.setText(HelperFormatter.getTotalString(subtotal) + "");
        checkoutTaxes.setText(HelperTaxesFees.getTaxesString(taxes));
        checkoutFees.setText(HelperTaxesFees.getFeesString(fees));
        checkoutTotal.setText(HelperFormatter.getTotalString(total) + "");
    }

    private void displayShoppingCartEmpty() {
        emptyCartLayout.setVisibility(View.VISIBLE);
        cartLayout.setVisibility(View.GONE);

        setHasOptionsMenu(false);
    }

    private static class PopulateDatabase extends AsyncTask<Void, Void, Void> {
        private Activity activity;
        private NavController navController;
        private Order order;
        private List<Item> cartItemList;


        public PopulateDatabase(Activity activity, NavController navController, Order order, List<Item> cartItemList) {
            super();
            this.activity = activity;
            this.navController = navController;
            this.order = order;
            this.cartItemList = cartItemList;
        }

        public PopulateDatabase() {
            super();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            itemDB.getOrderDao().insert(order);

            int orderId = itemDB.getOrderDao().getOrderByEmailAndDate(order.getEmail(), order.getDate()).getOrderId();

            for(int i=0;i<cartItemList.size(); i++) {
                OrderItem orderItem = new OrderItem( orderId, cartItemList.get(i).getItemId(), cartItemList.get(i).getCount());
                itemDB.getOrderItemDao().insert(orderItem);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyViewModel myViewModel = ViewModelProviders.of((FragmentActivity) activity).get(MyViewModel.class);
            myViewModel.clearCountsToZero();
            navController.navigate(R.id.action_navigation_checkout_to_navigation_order);
        }
    }
}
