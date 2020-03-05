package com.hexamind.uniquorestaurant.Chef;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hexamind.uniquorestaurant.Data.CartFoodItems;
import com.hexamind.uniquorestaurant.Data.ChefOrders;
import com.hexamind.uniquorestaurant.Data.FoodItem;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ChefOrdersAdapter extends RecyclerView.Adapter<ChefOrdersAdapter.ChefOrdersViewHolder> {
    private List<ChefOrders> orderList;
    private Context context;
    private boolean cardExpanded = false;

    public ChefOrdersAdapter(List<ChefOrders> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    class ChefOrdersViewHolder extends RecyclerView.ViewHolder {
        TextView tableNumber, itemOrderList;
        ImageView expand;
        AppCompatButton orderReady;
        LinearLayout layout;
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
        ChefOrders order = orderList.get(position);

        holder.tableNumber.setText(context.getString(R.string.default_table_string, String.valueOf(order.getTable().getId())));
        holder.expand.setOnClickListener(view -> {
            if (!cardExpanded) {
                openCard(holder.layout);
                holder.view.setVisibility(View.VISIBLE);
            } else {
                closeCard(holder.layout);
                holder.view.setVisibility(View.GONE);
            }
        });

        for (CartFoodItems foodItem : order.getFoodItemOrder()) {
            holder.itemOrderList.setText(context.getString(R.string.chef_orders_items_view, foodItem.getFoodItem().getFoodItemName(), String.valueOf(foodItem.getQuantity())));
        }
        holder.orderReady.setOnClickListener(view -> {

        });
    }

    private void openCard(View view) {
        view.setVisibility(View.VISIBLE);
        cardExpanded = true;
    }

    private void closeCard(View view) {
        view.setVisibility(View.GONE);
        cardExpanded = false;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
