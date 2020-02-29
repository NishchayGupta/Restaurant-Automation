package com.hexamind.uniquorestaurant.Customer;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexamind.uniquorestaurant.Data.CartFoodItems;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Utils.Constants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {
    private List<CartFoodItems> cartItemList;
    private Context context;
    private static int itemCount = 0;
    private OnTotalComputedListener listener;
    private static Double total = 0.0;
    private boolean minQuantityReached = false;
    private Double price = 0.0;

    public MyCartAdapter(List<CartFoodItems> cartItemList, Context context, OnTotalComputedListener listener) {
        this.cartItemList = cartItemList;
        this.context = context;
        this.listener = listener;
    }

    class MyCartViewHolder extends RecyclerView.ViewHolder {
        ImageView foodItemImage;
        TextView  foodItemName, foodItemDesc, foodItemPrice, addItem, itemCount, removeItem;
        ConstraintLayout delete;

        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);

            foodItemImage = itemView.findViewById(R.id.foodItemImage);
            foodItemName = itemView.findViewById(R.id.foodItemName);
            foodItemDesc = itemView.findViewById(R.id.foodItemDesc);
            foodItemPrice = itemView.findViewById(R.id.foodItemPrice);
            addItem = itemView.findViewById(R.id.addItem);
            itemCount = itemView.findViewById(R.id.itemCount);
            removeItem = itemView.findViewById(R.id.removeItem);
            delete = itemView.findViewById(R.id.delete);
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

        if (holder.getAdapterPosition() == 0)
            total = 0.0;
        itemCount = Integer.parseInt(holder.itemCount.getText().toString());
        holder.foodItemName.setText(cartItem.getFoodItem().getFoodItemName());
        holder.foodItemDesc.setText(cartItem.getFoodItem().getFoodItemName());
        holder.foodItemPrice.setText(context.getString(R.string.default_price_string, String.valueOf(cartItem.getFoodItem().getFoodItemPrice())));
        holder.addItem.setOnClickListener(view -> itemCount++);
        holder.itemCount.setText(String.valueOf(cartItem.getQuantity()));

        holder.removeItem.setOnClickListener(view -> {
            int quantityInt = Integer.parseInt(holder.itemCount.getText().toString());
            if(quantityInt == 1)
                minQuantityReached = true;
            else
                minQuantityReached = false;

            if (quantityInt > 1) {
                quantityInt--;
                holder.itemCount.setText(String.valueOf(quantityInt));
            }

            price = cartItem.getFoodItem().getFoodItemPrice();

            if (quantityInt > 0) {
                if (!minQuantityReached)
                    total -= 1.15 * price;

                if (quantityInt == 1)
                    minQuantityReached = true;
            }
            cartItemList.get(position).setQuantity(quantityInt);
            listener.saveFoodItems(Constants.FOOD_ITEM_STRING, cartItemList);
            listener.onTotalComputed(total);
        });
        holder.addItem.setOnClickListener(view -> {
            int quantityInt = Integer.parseInt(holder.itemCount.getText().toString());

            quantityInt++;
            holder.itemCount.setText(String.valueOf(quantityInt));
            cartItemList.get(position).setQuantity(quantityInt);
            listener.saveFoodItems(Constants.FOOD_ITEM_STRING, cartItemList);
            price = cartItem.getFoodItem().getFoodItemPrice();

            total += 1.15 * price;
            listener.onTotalComputed(total);
        });
        holder.delete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.delete_item_title_string))
                    .setMessage(context.getString(R.string.delete_item_message_string))
                    .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                        int newPosition = holder.getAdapterPosition();
                        delete(newPosition);
                        listener.saveFoodItems(Constants.FOOD_ITEM_STRING, cartItemList);
                        total = 0.0;
                    })
                    .setNegativeButton(android.R.string.no, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).show();
        });

        Double totalPrice = cartItem.getFoodItem().getFoodItemPrice();
        int quantity = Integer.parseInt(holder.itemCount.getText().toString());

        total += (totalPrice * quantity);
        if (holder.getAdapterPosition() == (cartItemList.size()-1))
            total *= 1.15;
        listener.onTotalComputed(total);
    }

    private void delete(int position) {
        cartItemList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }
}
