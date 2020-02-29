package com.hexamind.uniquorestaurant.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PastOrdersAdapter extends RecyclerView.Adapter<PastOrdersAdapter.PastOrdersViewHolder> {
    private Context context;
    private ArrayList<Order> orderList;

    public PastOrdersAdapter(Context context, ArrayList<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public class PastOrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView itemsOrdered, dateOrdered, amountPaid;

        public PastOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            itemsOrdered = itemView.findViewById(R.id.itemsOrdered);
            dateOrdered = itemView.findViewById(R.id.dateOrdered);
            amountPaid = itemView.findViewById(R.id.amountPaid);
        }
    }

    @NonNull
    @Override
    public PastOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PastOrdersViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_past_orders, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PastOrdersViewHolder holder, int position) {
        Order order = orderList.get(position);

        /*for (FoodItems items : order.getFoodItems())
            holder.itemsOrdered.setText(items.getFoodItemName());
        holder.dateOrdered.setText(order.getOrderDate());
        holder.amountPaid.setText(context.getResources().getString(R.string.item_cost_string, String.valueOf(order.getTotalCost())));*/
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
