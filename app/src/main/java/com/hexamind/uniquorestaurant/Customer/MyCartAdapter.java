package com.hexamind.uniquorestaurant.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexamind.uniquorestaurant.Data.FoodItem;
import com.hexamind.uniquorestaurant.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {
    private List<FoodItem> cartItemList;
    private Context context;
    private static int itemCount = 0;

    public MyCartAdapter(List<FoodItem> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }

    class MyCartViewHolder extends RecyclerView.ViewHolder {
        ImageView foodItemImage, deleteItem;
        TextView  foodItemName, foodItemDesc, foodItemPrice, addItem, itemCount, removeItem;

        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);

            foodItemImage = itemView.findViewById(R.id.foodItemImage);
            deleteItem = itemView.findViewById(R.id.deleteItem);
            foodItemName = itemView.findViewById(R.id.foodItemName);
            foodItemDesc = itemView.findViewById(R.id.foodItemDesc);
            foodItemPrice = itemView.findViewById(R.id.foodItemPrice);
            addItem = itemView.findViewById(R.id.addItem);
            itemCount = itemView.findViewById(R.id.itemCount);
            removeItem = itemView.findViewById(R.id.removeItem);
        }
    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCartViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_my_cart, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, int position) {
        FoodItem cartItem = cartItemList.get(position);

        itemCount = Integer.parseInt(holder.itemCount.getText().toString());
        holder.foodItemName.setText(cartItem.getFoodItemName());
        holder.foodItemDesc.setText(cartItem.getFoodItemDesc());
        holder.foodItemPrice.setText(String.valueOf(cartItem.getFoodItemPrice()));
        holder.addItem.setOnClickListener(view -> itemCount++);
        holder.removeItem.setOnClickListener(view -> {
            if (itemCount > 1)
                itemCount--;
        });
        holder.deleteItem.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }
}
