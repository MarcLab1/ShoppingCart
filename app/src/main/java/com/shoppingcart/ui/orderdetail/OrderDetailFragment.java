package com.shoppingcart.ui.orderdetail;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.shoppingcart.HelperFormatter;
import com.shoppingcart.HelperTaxesFees;
import com.shoppingcart.R;
import com.shoppingcart.room.Item;
import com.shoppingcart.room.ItemDB;
import com.shoppingcart.room.Order;
import com.shoppingcart.room.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailFragment extends Fragment {

    private RecyclerView orderDetailsView;
    private int orderId;
    private TextView orderDetailTotal, orderDetailSubTotal, orderDetailFees,orderDetailTaxes, orderDetailNumberOfItems, orderDetailOrderId, orderDetailEmail;
    private RecyclerView.LayoutManager layoutManager;
    private List<Item> itemList;
    private ItemsAdapter adapter;
    private Order order;
    private ItemDB itemDB;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_order_detail, container, false);

        Bundle bundle = getArguments();

        if (bundle != null) {

            orderId = bundle.getInt("orderid");
            itemDB = Room.databaseBuilder(getContext(), ItemDB.class, "dbone").build();

            orderDetailOrderId = root.findViewById(R.id.order_detail_orderId);
            orderDetailEmail = root.findViewById(R.id.order_detail_email);
            orderDetailSubTotal = root.findViewById(R.id.order_detail_subtotal);
            orderDetailTotal = root.findViewById(R.id.order_detail_total);
            orderDetailTaxes = root.findViewById(R.id.order_detail_taxes);
            orderDetailFees = root.findViewById(R.id.order_detail_fees);
            orderDetailNumberOfItems = root.findViewById(R.id.order_detail_numberofitems);

            orderDetailsView = root.findViewById(R.id.order_details_view);
            layoutManager = new LinearLayoutManager(getContext());
            orderDetailsView.setLayoutManager(layoutManager);

            new QueryOrder().execute();
        }

        return root;
    }

    private class ItemsAdapter extends RecyclerView.Adapter<OrderDetailFragment.ItemsAdapter.ViewHolder> {
        private List<Item> items;

        ItemsAdapter(List<Item> itemList) {
            items = new ArrayList<>();
            items = itemList;
        }

        @NonNull
        @Override
        public OrderDetailFragment.ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_order_detail, parent, false);
            return new OrderDetailFragment.ItemsAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final OrderDetailFragment.ItemsAdapter.ViewHolder holder, final int position) {
            final Item itm = items.get(position);

            holder.name.setText(itm.getName());
            holder.brand.setText(itm.getBrand());
            holder.weight.setText(itm.getWeight());
            holder.pricePerItem.setText(HelperFormatter.getTotalString(itm.getPrice()));
            holder.priceTotal.setText(HelperFormatter.getTotalString(itm.getCount() * itm.getPrice() ));
            holder.count.setText(String.valueOf(itm.getCount()));
            holder.image.setBackgroundResource(itm.getImage());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name, brand, weight, priceTotal, pricePerItem, count;
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
            }
        }
    }

    private class QueryOrder extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            if (order == null) {
                order = itemDB.getOrderDao().getOrderByOrderId(orderId);
            }
            List<OrderItem> orderItemList = itemDB.getOrderItemDao().getOrderItems(orderId);

            itemList = itemDB.getOrderDao().getItemsOfOrders(orderId).getItems();

            for(int i=0; i<itemList.size(); i++) {
                itemList.get(i).setCount(orderItemList.get(i).getQuantity());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            orderDetailOrderId.setText(order.getOrderId() + "");
            orderDetailEmail.setText(order.getEmail());
            orderDetailTotal.setText(HelperFormatter.getTotalString(order.getOrderTotal()));
            orderDetailSubTotal.setText(HelperFormatter.getTotalString(order.getOrderSubtotal()));
            orderDetailNumberOfItems.setText(order.getNumberOfItems() + "");
            orderDetailFees.setText(HelperTaxesFees.getFeesString(order.getFees()));
            orderDetailTaxes.setText( HelperTaxesFees.getTaxesString(order.getTaxes()) );

            adapter = new ItemsAdapter(itemList);
            orderDetailsView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }
}