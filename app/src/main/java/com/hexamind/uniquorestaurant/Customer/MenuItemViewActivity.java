package com.hexamind.uniquorestaurant.Customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.hexamind.uniquorestaurant.Data.BookTableSuccess;
import com.hexamind.uniquorestaurant.Data.CartFoodItems;
import com.hexamind.uniquorestaurant.Data.CheckAvailabilitySuccess;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.Data.GeneralError;
import com.hexamind.uniquorestaurant.Data.Person;
import com.hexamind.uniquorestaurant.LoginActivity;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;
import com.hexamind.uniquorestaurant.Utils.Constants;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.hexamind.uniquorestaurant.Utils.Constants.CUSTOMER_OBJ_NAME;
import static com.hexamind.uniquorestaurant.Utils.Constants.FOOD_ITEM_STRING;
import static com.hexamind.uniquorestaurant.Utils.Constants.IS_TABLE_BOOKED;

public class MenuItemViewActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer = null;
    private Long tableId;
    private Person person;
    private CustomerSuccess customer;
    private AppCompatButton addPersons, removePersons, addToCart;
    private AppCompatEditText quantity;
    private TextView foodItemName, price;
    private Boolean isTabledBooked = false;
    private FoodItems foodItem;
    private List<CartFoodItems> list = new ArrayList<>();
    private ImageView back;
    private Boolean isCustomerTableAlreadyExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_view);

        customer = SharedPreferencesUtils.getCustomerFromSharedPrefs(this, CUSTOMER_OBJ_NAME);
        person = customer.getPerson();
        list = SharedPreferencesUtils.getFoodItemsFromSharedPrefs(this, FOOD_ITEM_STRING);
        Gson gson = new Gson();
        String object = getIntent().getStringExtra(FOOD_ITEM_STRING);
        foodItem = gson.fromJson(object, FoodItems.class);
        isTabledBooked = SharedPreferencesUtils.getBooleanFromSharedPrefs(this, IS_TABLE_BOOKED);
        back = findViewById(R.id.back);
        isCustomerTableAlreadyExists = SharedPreferencesUtils.getBooleanFromSharedPrefs(this, Constants.TABLE_EXISTS_ALREADY_STRING);

        addPersons = findViewById(R.id.addPersons);
        removePersons = findViewById(R.id.removePersons);
        addToCart = findViewById(R.id.addToCart);
        foodItemName = findViewById(R.id.foodItemName);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);

        addPersons.setOnClickListener(view -> {
            int quantityInt = Integer.parseInt(this.quantity.getText().toString());

            quantityInt++;
            this.quantity.setText(String.valueOf(quantityInt));
        });
        removePersons.setOnClickListener(view -> {
            int quantityInt = Integer.parseInt(quantity.getText().toString());

            if (quantityInt > 1) {
                quantityInt--;
                quantity.setText(String.valueOf(quantityInt));
            }
        });

        if (isCustomerTableAlreadyExists)
            addToCart.setEnabled(false);
        else
            addToCart.setEnabled(true);

        addToCart.setOnClickListener(view -> {
            if (!isTabledBooked) {
                Toast.makeText(this, getString(R.string.book_tabled_needed_string), Toast.LENGTH_SHORT).show();
                viewBookingDialog();
            } else {
                list = SharedPreferencesUtils.getFoodItemsFromSharedPrefs(MenuItemViewActivity.this, FOOD_ITEM_STRING);
                CartFoodItems cartFood = new CartFoodItems(foodItem, Integer.parseInt(quantity.getText().toString()));
                if (list == null)
                    list = new ArrayList<>();
                list.add(cartFood);
                SharedPreferencesUtils.saveFoodItemsToSharedPrefs(MenuItemViewActivity.this, FOOD_ITEM_STRING, list);
                Toast.makeText(this, getString(R.string.item_add_success), Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(view ->
            startActivity(new Intent(MenuItemViewActivity.this, CustomerHomeActivity.class))
        );
        foodItemName.setText(foodItem.getFoodItemName());
        price.setText(String.valueOf(foodItem.getFoodItemPrice()));
    }

    private void viewBookingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View tableBooking = inflater.inflate(R.layout.layout_table_booking, null);
        builder.setView(tableBooking);

        AlertDialog dialog = builder.create();
        dialog.show();

        LinearLayout dineInLayout = tableBooking.findViewById(R.id.dineInLayout);
        LinearLayout takeOutLayout = tableBooking.findViewById(R.id.takeOutLayout);
        AppCompatButton dineIn = tableBooking.findViewById(R.id.dineIn);
        AppCompatButton takeOut = tableBooking.findViewById(R.id.takeOut);
        TextInputEditText custName = tableBooking.findViewById(R.id.custName);
        TextInputEditText phoneNumber = tableBooking.findViewById(R.id.custPhoneNumber);
        TextView addPersons = tableBooking.findViewById(R.id.addPersons);
        TextView tableAvailable = tableBooking.findViewById(R.id.tableAvailable);
        TextView timer = tableBooking.findViewById(R.id.timer);
        TextView removePersons = tableBooking.findViewById(R.id.removePersons);
        ImageView close = tableBooking.findViewById(R.id.close);
        AppCompatEditText numberOfPersons = tableBooking.findViewById(R.id.quantity);
        AppCompatButton checkAvailability = tableBooking.findViewById(R.id.checkAvailability);
        AppCompatButton bookTable = tableBooking.findViewById(R.id.bookTable);
        AppCompatButton reserveTakeout = tableBooking.findViewById(R.id.reserveTakeout);

        dineIn.setOnClickListener(view -> {
            dineIn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.drawable_table_booking_selected));
            takeOut.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.drawable_table_booking_unselected));
            dineIn.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            takeOut.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            dineInLayout.setVisibility(View.VISIBLE);
            takeOutLayout.setVisibility(View.GONE);
            takeOutLayout.animate().alpha(0.0f);
            dineInLayout.animate().alpha(1.0f);
        });
        takeOut.setOnClickListener(view -> {
            dineIn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.drawable_table_booking_unselected));
            takeOut.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.drawable_table_booking_selected));
            dineIn.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            takeOut.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            dineInLayout.setVisibility(View.GONE);
            takeOutLayout.setVisibility(View.VISIBLE);
            dineInLayout.animate().alpha(0.0f);
            takeOutLayout.animate().alpha(1.0f);
        });
        addPersons.setOnClickListener(view -> {
            int noOfPersons = Integer.parseInt(numberOfPersons.getText().toString());

            noOfPersons++;
            numberOfPersons.setText(String.valueOf(noOfPersons));
        });
        removePersons.setOnClickListener(view -> {
            int noOfPersons = Integer.parseInt(numberOfPersons.getText().toString());

            if (noOfPersons > 1) {
                noOfPersons--;
                numberOfPersons.setText(String.valueOf(noOfPersons));
            }
        });
        close.setOnClickListener(view -> dialog.dismiss());
        bookTable.setOnClickListener(view -> {
            Toast.makeText(this, "The table has successfully been booked", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        reserveTakeout.setOnClickListener(view -> dialog.dismiss());
        checkAvailability.setOnClickListener(view -> {
            ApiService apiService = RetrofitClient.getApiService();
            Call<GeneralError> initUpdateCall = apiService.checkAvailabilityCheck();

            initUpdateCall.enqueue(new Callback<GeneralError>() {
                @Override
                public void onResponse(Call<GeneralError> initCall, Response<GeneralError> response) {
                    GeneralError init = response.body();

                    if (init.getStatus().equals("OK")) {
                        ApiService api = RetrofitClient.getApiService();
                        Call<CheckAvailabilitySuccess> call = api.checkAvailability();
                        call.enqueue(new Callback<CheckAvailabilitySuccess>() {
                            @Override
                            public void onResponse(Call<CheckAvailabilitySuccess> call, Response<CheckAvailabilitySuccess> response) {
                                CheckAvailabilitySuccess checkAvailability = response.body();

                                if (countDownTimer != null)
                                    countDownTimer.cancel();
                                tableAvailable.setVisibility(View.VISIBLE);
                                timer.setVisibility(View.VISIBLE);
                                tableId = checkAvailability.getTableNumber();
                                if (checkAvailability.getWaitingTimeInMinutes() == 0) {
                                    tableAvailable.setText(getResources().getString(R.string.table_available_string, checkAvailability.getTableNumber()));
                                    timer.setVisibility(View.GONE);
                                    bookTable.setEnabled(true);
                                } else {
                                    tableAvailable.setText(getResources().getString(R.string.table_available_string, checkAvailability.getTableNumber()));

                                    countDownTimer = new CountDownTimer(getMillis(checkAvailability.getWaitingTimeInMinutes()), 1000) {
                                        @Override
                                        public void onTick(long l) {
                                            timer.setText(getResources().getString(R.string.table_available_waiting_time_string, String.format(Locale.getDefault(),
                                                    "%d:%d", TimeUnit.MILLISECONDS.toMinutes(l), TimeUnit.MILLISECONDS.toSeconds(l) -
                                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)))));
                                        }

                                        @Override
                                        public void onFinish() {
                                            Toast.makeText(MenuItemViewActivity.this, "Table no. " + checkAvailability.getTableNumber() + " is now available!!!", Toast.LENGTH_SHORT).show();
                                            bookTable.setEnabled(true);
                                        }
                                    }.start();

                                    bookTable.setEnabled(false);
                                }
                            }

                            @Override
                            public void onFailure(Call<CheckAvailabilitySuccess> call, Throwable t) {
                                Toast.makeText(MenuItemViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(MenuItemViewActivity.this, getString(R.string.check_availability_string), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GeneralError> call, Throwable t) {
                    Toast.makeText(MenuItemViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        bookTable.setOnClickListener(view -> {
            ApiService api = RetrofitClient.getApiService();
            Call<BookTableSuccess> call = api.bookTable(tableId, person);
            call.enqueue(new Callback<BookTableSuccess>() {
                @Override
                public void onResponse(@NotNull Call<BookTableSuccess> call, @NotNull Response<BookTableSuccess> response) {
                    BookTableSuccess bookTable = response.body();

                    if (response.code() == 200) {
                        Toast.makeText(MenuItemViewActivity.this, tableId + " was booked successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(MenuItemViewActivity.this, getString(R.string.booking_table_error_string), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MenuItemViewActivity.this, LoginActivity.class));
                        dialog.dismiss();
                        finish();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<BookTableSuccess> call, @NotNull Throwable t) {
                    Toast.makeText(MenuItemViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private long getMillis(Long minutes) {
        return minutes * 60 * 1000;
    }
}
