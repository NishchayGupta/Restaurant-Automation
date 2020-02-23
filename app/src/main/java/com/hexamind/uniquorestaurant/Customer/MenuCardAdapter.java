package com.hexamind.uniquorestaurant.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hexamind.uniquorestaurant.Data.FoodItem;
import com.hexamind.uniquorestaurant.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuCardAdapter extends RecyclerView.Adapter<MenuCardAdapter.MenuCardViewHolder> {
    private Context context;
    private ArrayList<FoodItem> foodItemList;

    public MenuCardAdapter(Context context, ArrayList<FoodItem> foodItemList) {
        this.context = context;
        this.foodItemList = foodItemList;
    }

    public class MenuCardViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemName, itemCost;

        public MenuCardViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.imageView);
            itemName = itemView.findViewById(R.id.itemName);
            itemCost = itemView.findViewById(R.id.itemCost);
        }
    }

    @NonNull
    @Override
    public MenuCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuCardViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_menu_items, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuCardViewHolder holder, int position) {
        FoodItem item = foodItemList.get(position);

        holder.itemName.setText(item.getFoodItemName());
        holder.itemCost.setText(context.getResources().getString(R.string.item_cost_string, String.valueOf(item.getFoodItemPrice())));
        Glide.with(context).load(item.getFoodItemPicture()).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }
}
