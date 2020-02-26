package com.hexamind.uniquorestaurant.Customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hexamind.uniquorestaurant.Data.FoodItem;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Utils.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MenuCardAdapter extends RecyclerView.Adapter<MenuCardAdapter.MenuCardViewHolder> {
    private Context context;
    private ArrayList<FoodItems> foodItemList;

    public MenuCardAdapter(Context context, ArrayList<FoodItems> foodItemList) {
        this.context = context;
        this.foodItemList = foodItemList;
    }

    public class MenuCardViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemName, itemCost;
        public CardView cardView;

        public MenuCardViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.imageView);
            itemName = itemView.findViewById(R.id.itemName);
            itemCost = itemView.findViewById(R.id.itemCost);
            cardView = itemView.findViewById(R.id.cardView);
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
        FoodItems item = foodItemList.get(position);

        holder.itemName.setText(item.getFoodItemName());
        holder.itemCost.setText(context.getResources().getString(R.string.item_cost_string, String.valueOf(item.getFoodItemPrice())));
        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MenuItemViewActivity.class);
            Gson gson = new Gson();
            intent.putExtra(Utils.FOOD_ITEM_STRING, gson.toJson(item));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }
}
