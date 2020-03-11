package com.hexamind.uniquorestaurant.Manager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hexamind.uniquorestaurant.Data.FoodItem;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.Data.FoodItemsManager;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Utils.Constants;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class UpdateFoodItemAdapter extends RecyclerView.Adapter<UpdateFoodItemAdapter.UpdateFoodItemViewHolder> {
    private List<FoodItems> foodItemList;
    private Context context;

    public UpdateFoodItemAdapter(List<FoodItems> foodItemList, Context context) {
        this.foodItemList = foodItemList;
        this.context = context;
    }

    class UpdateFoodItemViewHolder extends RecyclerView.ViewHolder {
        ImageView foodItemImage;
        TextView foodItemName, foodItemPrice;
        RelativeLayout foodItemView, deleteItem;

        public UpdateFoodItemViewHolder(@NonNull View itemView) {
            super(itemView);

            foodItemImage = itemView.findViewById(R.id.foodItemImage);
            foodItemView = itemView.findViewById(R.id.foodItemView);
            deleteItem = itemView.findViewById(R.id.delete);
            foodItemName = itemView.findViewById(R.id.foodItemName);
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
        FoodItems foodItem = foodItemList.get(position);

        holder.deleteItem.setVisibility(View.GONE);
        Glide.with(context)
                .load(foodItem.getFoodItemPicture())
                .into(holder.foodItemImage);
        holder.foodItemView.setOnClickListener(view -> {
            SharedPreferencesUtils.saveLongToSharedPrefs(context, Constants.FOOD_ITEM_ID, foodItem.getFoodItemId().longValue());
            Navigation.findNavController((ManagerActivity) context, R.id.managerNavHostFragment).navigate(R.id.action_menu_edit_item_to_updateItemFragment);
        });
        holder.foodItemPrice.setText(context.getString(R.string.default_price_string, String.valueOf(foodItem.getFoodItemPrice())));
        holder.foodItemName.setText(foodItem.getFoodItemName());
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }
}
