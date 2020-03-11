package com.hexamind.uniquorestaurant.Customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Utils.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MenuCardAdapter extends RecyclerView.Adapter<MenuCardAdapter.MenuCardViewHolder> {
    private Context context;
    private ArrayList<FoodItems> foodItemList;
    private DecimalFormat dfDoubleInt = new DecimalFormat("#");

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

            itemImage = itemView.findViewById(R.id.itemImage);
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
        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MenuItemViewActivity.class);
            Gson gson = new Gson();
            intent.putExtra(Constants.FOOD_ITEM_STRING, gson.toJson(item));
            context.startActivity(intent);
        });
        if (item.getFoodItemPrice() % 1 == 0)
            holder.itemCost.setText(context.getString(R.string.default_price_string, String.valueOf(dfDoubleInt.format(item.getFoodItemPrice()))));
        else
            holder.itemCost.setText(context.getString(R.string.default_price_string, String.valueOf(item.getFoodItemPrice())));
        Glide.with(context)
                .load(item.getFoodItemPicture())
                .placeholder(R.drawable.ic_image_black_24dp)
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }
}
