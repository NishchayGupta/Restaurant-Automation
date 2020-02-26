package com.hexamind.uniquorestaurant.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexamind.uniquorestaurant.Data.CartFoodItems;
import com.hexamind.uniquorestaurant.Data.FoodItem;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {
    private List<CartFoodItems> cartItemList;
    private Context context;
    private static int itemCount = 0;
    private OnTotalComputedListener listener;
    private Double total = 0.0;

    public MyCartAdapter(List<CartFoodItems> cartItemList, Context context, OnTotalComputedListener listener) {
        this.cartItemList = cartItemList;
        this.context = context;
        this.listener = listener;
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
        CartFoodItems cartItem = cartItemList.get(position);

        itemCount = Integer.parseInt(holder.itemCount.getText().toString());
        holder.foodItemName.setText(cartItem.getFoodItem().getFoodItemName());
        holder.foodItemDesc.setText(cartItem.getFoodItem().getFoodItemName());
        holder.foodItemPrice.setText(context.getString(R.string.default_price_string, String.valueOf(cartItem.getFoodItem().getFoodItemPrice())));
        holder.addItem.setOnClickListener(view -> itemCount++);
        holder.itemCount.setText(String.valueOf(cartItem.getQuantity()));
        holder.removeItem.setOnClickListener(view -> {
            int quantityInt = Integer.parseInt(holder.itemCount.getText().toString());

            if (quantityInt > 1) {
                quantityInt--;
                holder.itemCount.setText(String.valueOf(quantityInt));
            }

            Double price = cartItem.getFoodItem().getFoodItemPrice();
            int quantity = Integer.parseInt(holder.itemCount.getText().toString());

            total -= ((1.15 * price * quantity) - price);
            listener.onTotalComputed(total);
        });
        holder.addItem.setOnClickListener(view -> {
            int quantityInt = Integer.parseInt(holder.itemCount.getText().toString());

            quantityInt++;
            holder.itemCount.setText(String.valueOf(quantityInt));

            Double price = cartItem.getFoodItem().getFoodItemPrice();
            int quantity = Integer.parseInt(holder.itemCount.getText().toString());

            total += ((1.15 * price * quantity) + price);
            listener.onTotalComputed(total);
        });
        holder.deleteItem.setOnClickListener(view -> {

        });

        Double price = cartItem.getFoodItem().getFoodItemPrice();
        int quantity = Integer.parseInt(holder.itemCount.getText().toString());

        total += (1.15 * price * quantity);
        listener.onTotalComputed(total);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }
}
