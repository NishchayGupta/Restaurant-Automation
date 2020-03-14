package com.hexamind.uniquorestaurant.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hexamind.uniquorestaurant.Data.CartFoodItems;
import com.hexamind.uniquorestaurant.Data.ChefOrders;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.Data.OrderCart;
import com.hexamind.uniquorestaurant.Data.OrderSuccess;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;
import com.hexamind.uniquorestaurant.Utils.Constants;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {
    private List<CartFoodItems> cartItemList;
    private Context context;
    private static int itemCount = 0;
    private OnTotalComputedListener listener;
    private static Double total = 0.0;
    private boolean minQuantityReached = false, paymentRemaining = false, isTableOccupied = false;
    private Double price = 0.0;
    private Long customerID;
    private static boolean tableExistsAlready = false;

    public MyCartAdapter(List<CartFoodItems> cartItemList, Context context, OnTotalComputedListener listener, Long customerID) {
        this.cartItemList = cartItemList;
        this.context = context;
        this.listener = listener;
        this.customerID = customerID;
    }

    class MyCartViewHolder extends RecyclerView.ViewHolder {
        ImageView foodItemImage;
        TextView  foodItemName, foodItemDesc, foodItemPrice, addItem, itemCount, removeItem;
        RelativeLayout delete;

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

        ApiService api = RetrofitClient.getApiService();
        Call<ChefOrders> callTableExists = api.getCustomerTableExists(customerID);
        callTableExists.enqueue(new Callback<ChefOrders>() {
            @Override
            public void onResponse(Call<ChefOrders> call, Response<ChefOrders> response) {
                ChefOrders order = response.body();

                if (response.code() == 200) {
                    tableExistsAlready = order.getExistingOrder();

                    if (SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(context, Constants.TABLE_ID_MAP_CONST_STRING) != null) {
                        Long tableId = SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(context, Constants.TABLE_ID_MAP_CONST_STRING).get(customerID);
                        if  (tableId != null) {
                            if (tableId != 12 && tableId != 0) {
                                if (tableExistsAlready)
                                    paymentRemaining = true;
                                else
                                    paymentRemaining = false;
                            } else {
                                paymentRemaining = true;
                                isTableOccupied = true;
                            }
                        }
                    } else {
                        paymentRemaining = true;
                        isTableOccupied = true;
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.customer_table_exist_error_message_string), Toast.LENGTH_SHORT).show();
                    tableExistsAlready = false;

                    SharedPreferencesUtils.saveBooleanToSharedPrefs(context, Constants.TABLE_EXISTS_ALREADY_STRING, tableExistsAlready);
                }
            }

            @Override
            public void onFailure(Call<ChefOrders> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                tableExistsAlready = false;
            }
        });

        if (holder.getAdapterPosition() == 0)
            total = 0.0;
        itemCount = Integer.parseInt(holder.itemCount.getText().toString());
        holder.foodItemName.setText(cartItem.getFoodItem().getFoodItemName());
        Glide.with(context)
                .load(cartItem.getFoodItem().getFoodItemPicture())
                .into(holder.foodItemImage);
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
            listener.saveFoodItems(Constants.FOOD_ITEM_MAP_STRING, cartItemList);
            listener.onTotalComputed(total);
        });
        holder.addItem.setOnClickListener(view -> {
            int quantityInt = Integer.parseInt(holder.itemCount.getText().toString());

            quantityInt++;
            holder.itemCount.setText(String.valueOf(quantityInt));
            cartItemList.get(position).setQuantity(quantityInt);
            listener.saveFoodItems(Constants.FOOD_ITEM_MAP_STRING, cartItemList);
            price = cartItem.getFoodItem().getFoodItemPrice();

            total += 1.15 * price;
            listener.onTotalComputed(total);
        });
        holder.delete.setOnClickListener(view -> {
            if (SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(context, Constants.TABLE_ID_MAP_CONST_STRING) != null) {
                if (SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(context, Constants.TABLE_ID_MAP_CONST_STRING).get(customerID) != null) {
                    Long tableId = SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(context, Constants.TABLE_ID_MAP_CONST_STRING).get(customerID);
                    if (tableId != 12 && tableId != 0) {
                        if (paymentRemaining) {
                            Toast.makeText(context, context.getString(R.string.payment_first_string), Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(((CustomerHomeActivity) context), R.id.navHostFragment).navigate(R.id.action_menu_my_cart_to_menu_past_orders);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setCancelable(false);
                            LayoutInflater inflater = ((CustomerHomeActivity) context).getLayoutInflater();
                            View confirm = inflater.inflate(R.layout.layout_confirm_dialog, null);
                            builder.setView(confirm);

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            TextView title = confirm.findViewById(R.id.title);
                            TextView message = confirm.findViewById(R.id.message);
                            AppCompatButton yes = confirm.findViewById(R.id.confirmBtn);
                            AppCompatButton no = confirm.findViewById(R.id.cancelBtn);

                            message.setText(context.getString(R.string.delete_item_message_string));
                            title.setText(context.getString(R.string.delete_item_title_string));
                            yes.setOnClickListener(view1 -> {
                                int newPosition = holder.getAdapterPosition();
                                delete(newPosition);
                                listener.saveFoodItems(Constants.FOOD_ITEM_MAP_STRING, cartItemList);
                                if (cartItemList.isEmpty())
                                    listener.onTotalComputed(0.0);
                                total = 0.0;

                                dialog.dismiss();
                            });
                            no.setOnClickListener(view1 -> dialog.dismiss());
                        }
                    }
                }
            }
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
