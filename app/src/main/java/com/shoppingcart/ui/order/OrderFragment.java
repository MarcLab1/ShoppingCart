package com.shoppingcart.ui.order;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.shoppingcart.HelperFormatter;
import com.shoppingcart.R;
import com.shoppingcart.room.ItemDB;
import com.shoppingcart.room.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private RecyclerView orderView;
    private List<Order> orderList;
    private RecyclerView.LayoutManager layoutManager;
    private OrdersAdapter adapter;
    private static ItemDB itemDB;
    private NavController navController;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_order, container, false);

        orderView = root.findViewById(R.id.orders_view);

        layoutManager = new LinearLayoutManager(getContext());
        orderView.setLayoutManager(layoutManager);

        itemDB = Room.databaseBuilder(getContext(), ItemDB.class, "dbone").build();

        new QueryOrders().execute();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(root);
    }

    private class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
        private List<Order> orders;

        OrdersAdapter(List<Order> orderList) {
            orders = new ArrayList<>();
            orders = orderList;
        }

        @NonNull
        @Override
        public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_order, parent, false);
            return new OrdersAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final OrdersAdapter.ViewHolder holder, final int position) {
            final Order ord = orders.get(position);

             holder.email.setText(ord.getEmail());
             holder.total.setText(HelperFormatter.getTotalString(ord.getOrderTotal()));
             if(ord.getNumberOfItems() == 1)
                holder.numberOfItems.setText(ord.getNumberOfItems() + " Item");
             else
                holder.numberOfItems.setText(ord.getNumberOfItems() + " Items");
             holder.date.setText(String.valueOf(ord.getDateString()));
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView email, total, date, numberOfItems;

            ViewHolder(View itemView) {
                super(itemView);
                email = itemView.findViewById(R.id.order_email);
                total = itemView.findViewById(R.id.order_total);
                numberOfItems = itemView.findViewById(R.id.order_numberofitems);
                date = itemView.findViewById(R.id.order_date);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("orderid", orderList.get(getAdapterPosition()).getOrderId());
                navController.navigate(R.id.action_navigation_order_to_navigation_order_detail, bundle);
            }
        }
    }

    private class QueryOrders extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            if (orderList == null)
                orderList = itemDB.getOrderDao().getAllOrders();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new OrdersAdapter(orderList);
            orderView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
