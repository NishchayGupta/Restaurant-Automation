package com.hexamind.uniquorestaurant.Manager;

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

public class UpdateFoodItemAdapter extends RecyclerView.Adapter<UpdateFoodItemAdapter.UpdateFoodItemViewHolder> {
    private List<FoodItem> foodItemList;
    private Context context;

    public UpdateFoodItemAdapter(List<FoodItem> foodItemList, Context context) {
        this.foodItemList = foodItemList;
        this.context = context;
    }

    class UpdateFoodItemViewHolder extends RecyclerView.ViewHolder {
        ImageView foodItemImage, deleteItem;
        TextView foodItemName, foodItemDesc, foodItemPrice;

        public UpdateFoodItemViewHolder(@NonNull View itemView) {
            super(itemView);

            foodItemImage = itemView.findViewById(R.id.foodItemImage);
            deleteItem = itemView.findViewById(R.id.deleteItem);
            foodItemName = itemView.findViewById(R.id.foodItemName);
            foodItemDesc = itemView.findViewById(R.id.foodItemDesc);
            foodItemPrice = itemView.findViewById(R.id.foodItemPrice);
        }
    }

    @NonNull
    @Override
    public UpdateFoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UpdateFoodItemViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_delete_food_items, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateFoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }
}
