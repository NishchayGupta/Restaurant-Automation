package com.hexamind.uniquorestaurant.Manager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hexamind.uniquorestaurant.Customer.CustomerHomeActivity;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.Data.GeneralError;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;
import com.hexamind.uniquorestaurant.Utils.Constants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteFoodItemAdapter extends RecyclerView.Adapter<DeleteFoodItemAdapter.DeleteFoodItemViewHolder> {
    private List<FoodItems> foodItemList;
    private Context context;

    public DeleteFoodItemAdapter(List<FoodItems> foodItemList, Context context) {
        this.foodItemList = foodItemList;
        this.context = context;
    }

    class DeleteFoodItemViewHolder extends RecyclerView.ViewHolder {
        ImageView foodItemImage;
        TextView foodItemName, foodItemPrice;
        RelativeLayout foodItemView, deleteItem;

        public DeleteFoodItemViewHolder(@NonNull View itemView) {
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
    public DeleteFoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeleteFoodItemViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_delete_food_items, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteFoodItemViewHolder holder, int position) {
        FoodItems foodItem = foodItemList.get(position);

        Glide.with(context)
                .load(foodItem.getFoodItemPicture())
                .into(holder.foodItemImage);
        holder.foodItemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateItemFragment.class);
            intent.putExtra(Constants.FOOD_ITEM_ID, foodItem.getFoodItemId());
            context.startActivity(intent);
        });
        holder.foodItemPrice.setText(context.getString(R.string.default_price_string, String.valueOf(foodItem.getFoodItemPrice())));
        holder.foodItemName.setText(foodItem.getFoodItemName());
        holder.deleteItem.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            LayoutInflater inflater = ((ManagerActivity) context).getLayoutInflater();
            View confirm = inflater.inflate(R.layout.layout_confirm_dialog, null);
            builder.setView(confirm);

            AlertDialog dialog = builder.create();
            dialog.show();

            TextView title = confirm.findViewById(R.id.title);
            TextView message = confirm.findViewById(R.id.message);
            AppCompatButton yes = confirm.findViewById(R.id.confirmBtn);
            AppCompatButton no = confirm.findViewById(R.id.cancelBtn);

            message.setText(context.getString(R.string.confirm_delete_food_item_message_string));
            title.setText(context.getString(R.string.food_item_delete_title_string));
            yes.setOnClickListener(view1 -> {
                ApiService apiService = RetrofitClient.getApiService();
                Call<GeneralError> call = apiService.deleteFoodItem(foodItem.getFoodItemId());

                call.enqueue(new Callback<GeneralError>() {
                    @Override
                    public void onResponse(Call<GeneralError> call, Response<GeneralError> response) {
                        GeneralError success = response.body();

                        if (response.code() == 200) {
                            Toast.makeText(context, success.getMessage(), Toast.LENGTH_SHORT).show();
                            int newPosition = holder.getAdapterPosition();
                            delete(newPosition);
                        } else
                            Toast.makeText(context, context.getString(R.string.food_item_delete_failure_string), Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<GeneralError> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            });
            no.setOnClickListener(view1 -> dialog.dismiss());
        });
    }

    private void delete(int position) {
        foodItemList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }
}
