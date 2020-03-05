package com.hexamind.uniquorestaurant.Customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.hexamind.uniquorestaurant.Data.BookTableSuccess;
import com.hexamind.uniquorestaurant.Data.CheckAvailabilitySuccess;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.Data.GeneralError;
import com.hexamind.uniquorestaurant.Data.OrderSuccess;
import com.hexamind.uniquorestaurant.Data.Person;
import com.hexamind.uniquorestaurant.LoginActivity;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;
import com.hexamind.uniquorestaurant.Utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CustomerHomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private AppBarConfiguration  appbarConfig;
    private NavController navController;
    private NavigationView navView;
    private Long tableId;
    private Person person;
    private CustomerSuccess customer;
    private CountDownTimer countDownTimer = null;
    boolean tableExistsAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        setSupportActionBar(findViewById(R.id.toolbar));
        drawerLayout = findViewById(R.id.drawer);
        navView = findViewById(R.id.navView);

        final Set<Integer> menuItems = new ArraySet<>();
        menuItems.add(R.id.menu_menu_card);
        menuItems.add(R.id.menu_past_orders);
        menuItems.add(R.id.menu_my_cart);
        menuItems.add(R.id.menu_notifications);
        menuItems.add(R.id.menu_profile);

        appbarConfig = new AppBarConfiguration.Builder(menuItems)
                .setDrawerLayout(drawerLayout)
                .build();

        navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appbarConfig);
        NavigationUI.setupWithNavController(navView, navController);

        MenuItem logout = navView.getMenu().findItem(R.id.menu_logout);
        logout.setOnMenuItemClickListener(menuItem -> {
                    startActivity(new Intent(CustomerHomeActivity.this, LoginActivity.class));
                    Toast.makeText(CustomerHomeActivity.this, getString(R.string.logout_success_message_string), Toast.LENGTH_SHORT).show();
                    finish();
                    return true;
                });

        customer = SharedPreferencesUtils.getCustomerFromSharedPrefs(this, Constants.CUSTOMER_OBJ_NAME);
        person = customer.getPerson();
        if (SharedPreferencesUtils.getLongFromSharedPrefs(this, Constants.TABLE_ID_CONST_STRING) != 12
                && SharedPreferencesUtils.getLongFromSharedPrefs(this, Constants.TABLE_ID_CONST_STRING) != 0) {
            if (SharedPreferencesUtils.getBooleanFromSharedPrefs(this, Constants.TABLE_EXISTS_ALREADY_STRING)) {
                if (!getCustomerTableAlreadyExists()) {

                }
            }
        } else {
            viewBookingDialog();
            SharedPreferencesUtils.deleteLongFromSharedPrefs(this, Constants.TABLE_ID_CONST_STRING);
            SharedPreferencesUtils.saveBooleanToSharedPrefs(this, Constants.TABLE_EXISTS_ALREADY_STRING, false);
        }

        Toast.makeText(this, "Customer name: " + person.getName(), Toast.LENGTH_SHORT).show();
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
        LinearLayout progress = tableBooking.findViewById(R.id.progress);

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
            progress.setVisibility(View.VISIBLE);
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
                                progress.setVisibility(View.GONE);
                                tableAvailable.setVisibility(View.VISIBLE);
                                timer.setVisibility(View.VISIBLE);
                                tableId = checkAvailability.getTableNumber();
                                System.out.println("\n\n\n\n\nTable ID: " + tableId);
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
                                            Toast.makeText(CustomerHomeActivity.this, "Table no. " + checkAvailability.getTableNumber() + " is now available!!!", Toast.LENGTH_SHORT).show();
                                            bookTable.setEnabled(true);
                                        }
                                    }.start();

                                    bookTable.setEnabled(false);
                                }
                            }

                            @Override
                            public void onFailure(Call<CheckAvailabilitySuccess> call, Throwable t) {
                                Toast.makeText(CustomerHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(CustomerHomeActivity.this, getString(R.string.check_availability_string), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GeneralError> call, Throwable t) {
                    Toast.makeText(CustomerHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                        SharedPreferencesUtils.saveBooleanToSharedPrefs(CustomerHomeActivity.this, Constants.IS_TABLE_BOOKED, true);
                        SharedPreferencesUtils.saveLongToSharedPrefs(CustomerHomeActivity.this, Constants.TABLE_ID_CONST_STRING, tableId);

                        System.out.println("\n\n\n\n\nBooked Table ID: " + tableId);
                        Toast.makeText(CustomerHomeActivity.this, tableId + " was booked successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(CustomerHomeActivity.this, getString(R.string.booking_table_error_string), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CustomerHomeActivity.this, LoginActivity.class));
                        dialog.dismiss();
                        finish();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<BookTableSuccess> call, @NotNull Throwable t) {
                    Toast.makeText(CustomerHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private long getMillis(Long minutes) {
        return minutes * 60 * 1000;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appbarConfig) || super.onSupportNavigateUp();
    }

    private boolean getCustomerTableAlreadyExists() {
        ApiService api = RetrofitClient.getApiService();
        Call<OrderSuccess> callTableExists = api.getCustomerTableExists(customer.getPerson().getCustomer().getCustomerId());
        callTableExists.enqueue(new Callback<OrderSuccess>() {
            @Override
            public void onResponse(Call<OrderSuccess> call, Response<OrderSuccess> response) {
                OrderSuccess order = response.body();

                if (response.code() == 200) {
                    Long id = order.getId();

                    if (id != 12)
                        tableExistsAlready = true;
                    else {
                        if (order.getTable() != null && order.isOrderPrepared())
                            tableExistsAlready = false;
                        else
                            tableExistsAlready = true;
                    }

                    SharedPreferencesUtils.saveBooleanToSharedPrefs(CustomerHomeActivity.this, Constants.TABLE_EXISTS_ALREADY_STRING, tableExistsAlready);
                } else {
                    Toast.makeText(CustomerHomeActivity.this, getString(R.string.customer_table_exist_error_message_string), Toast.LENGTH_SHORT).show();
                    tableExistsAlready = false;

                    SharedPreferencesUtils.saveBooleanToSharedPrefs(CustomerHomeActivity.this, Constants.TABLE_EXISTS_ALREADY_STRING, tableExistsAlready);
                }
            }

            @Override
            public void onFailure(Call<OrderSuccess> call, Throwable t) {
                Toast.makeText(CustomerHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                tableExistsAlready = false;

                SharedPreferencesUtils.saveBooleanToSharedPrefs(CustomerHomeActivity.this, Constants.TABLE_EXISTS_ALREADY_STRING, tableExistsAlready);
            }
        });

        return tableExistsAlready;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
