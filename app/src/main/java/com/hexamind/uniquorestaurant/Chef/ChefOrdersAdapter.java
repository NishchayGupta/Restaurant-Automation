package com.hexamind.uniquorestaurant.Chef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexamind.uniquorestaurant.Data.FoodItem;
import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ChefOrdersAdapter extends RecyclerView.Adapter<ChefOrdersAdapter.ChefOrdersViewHolder> {
    private List<Order> orderList;
    private Context context;

    public ChefOrdersAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    class ChefOrdersViewHolder extends RecyclerView.ViewHolder {
        TextView tableNumber, itemOrderList;
        ImageView expand;
        AppCompatButton orderReady;
        ConstraintLayout layout;
        View view;

        public ChefOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            layout =  itemView.findViewById(R.id.layout);
            view =  itemView.findViewById(R.id.view);
            tableNumber =  itemView.findViewById(R.id.tableNumber);
            itemOrderList =  itemView.findViewById(R.id.itemsOrderList);
            expand =  itemView.findViewById(R.id.expand);
            orderReady =  itemView.findViewById(R.id.orderReady);
        }
    }

    @NonNull
    @Override
    public ChefOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChefOrdersViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_chef_orders, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChefOrdersViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tableNumber.setText(String.valueOf(order.getTable().getId()));
        for (FoodItem foodItem : order.getFoodItems()) {
            holder.itemOrderList.setText(foodItem + "\n");
        }
        holder.expand.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
