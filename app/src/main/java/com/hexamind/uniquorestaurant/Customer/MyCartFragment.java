package com.hexamind.uniquorestaurant.Customer;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hexamind.uniquorestaurant.Data.CartFoodItems;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.Data.GeneralError;
import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.Data.OrderCart;
import com.hexamind.uniquorestaurant.Data.OrderSuccess;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;
import com.hexamind.uniquorestaurant.Utils.Constants;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyCartFragment extends Fragment implements OnTotalComputedListener {
    private View root;
    private List<CartFoodItems> foodItemList = new ArrayList<>();
    private MyCartAdapter adapter;
    private RecyclerView recyclerView;
    private TextView foodPrice, totalCost, totalTax, totalTxt, noItems;
    private DecimalFormat df = new DecimalFormat("#.##");
    private ConstraintLayout paymentSecondaryLayout;
    private RelativeLayout paymentPrimaryLayout;
    private boolean layoutClicked = true;
    private AppCompatButton addOrder;
    private List<OrderCart> orderFromCartList = new ArrayList<>();
    private Boolean isCustomerTableAlreadyExists = false;
    private Map<Long, List<CartFoodItems>> cartItemsMap;
    private Long customerID;
    private ImageView up;
    private boolean paymentRemaining = false;
    private boolean isTableOccupied = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_cart, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        foodPrice = root.findViewById(R.id.foodPrice);
        paymentSecondaryLayout = root.findViewById(R.id.paymentSecondaryLayout);
        paymentPrimaryLayout = root.findViewById(R.id.paymentPrimaryLayout);
        totalCost = root.findViewById(R.id.totalCost);
        totalTax = root.findViewById(R.id.totalTax);
        totalTxt = root.findViewById(R.id.total);
        up = root.findViewById(R.id.up);
        addOrder = root.findViewById(R.id.addOrder);
        noItems = root.findViewById(R.id.noItems);
        customerID = SharedPreferencesUtils.getCustomerFromSharedPrefs(root.getContext(), Constants.CUSTOMER_OBJ_NAME).getPerson().getCustomer().getCustomerId();
        isCustomerTableAlreadyExists = SharedPreferencesUtils.getBooleanFromSharedPrefs(root.getContext(), Constants.TABLE_EXISTS_ALREADY_STRING);
        cartItemsMap = new HashMap<>();

        foodPrice.setText(getString(R.string.default_cart_price_string));

        if (SharedPreferencesUtils.getFoodItemsByCustomerFromSharedPrefs(root.getContext(), Constants.FOOD_ITEM_MAP_STRING) != null) {
            cartItemsMap = SharedPreferencesUtils.getFoodItemsByCustomerFromSharedPrefs(root.getContext(), Constants.FOOD_ITEM_MAP_STRING);
            if (cartItemsMap.get(customerID) == null) {
                noItems.setVisibility(View.VISIBLE);
            } else {
                foodItemList = cartItemsMap.get(customerID);
                if (foodItemList.isEmpty())
                    noItems.setVisibility(View.VISIBLE);
                else {
                    adapter = new MyCartAdapter(foodItemList, root.getContext(), this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                }
            }
        } else {
            noItems.setVisibility(View.VISIBLE);
        }

        /*if ((SharedPreferencesUtils.getLongFromSharedPrefs(root.getContext(), Constants.TABLE_ID_CONST_STRING) != 12
                && SharedPreferencesUtils.getLongFromSharedPrefs(root.getContext(), Constants.TABLE_ID_CONST_STRING) != 0)
                && isCustomerTableAlreadyExists) {
            Toast.makeText(root.getContext(), getString(R.string.payment_first_string), Toast.LENGTH_SHORT).show();
        } else
            addOrder.setEnabled(true);*/

        if (SharedPreferencesUtils.getLongFromSharedPrefs(root.getContext(), Constants.TABLE_ID_CONST_STRING) == 12 ||
                SharedPreferencesUtils.getBooleanFromSharedPrefs(root.getContext(), Constants.TABLE_EXISTS_ALREADY_STRING)) {

        } else {
            addOrder.setEnabled(true);
        }

        paymentPrimaryLayout.setOnClickListener(view -> {
            if (layoutClicked) {
                paymentSecondaryLayout.setVisibility(View.VISIBLE);
                layoutClicked = false;
                up.setImageDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.ic_keyboard_arrow_down_black_24dp));
            } else {
                paymentSecondaryLayout.setVisibility(View.GONE);
                layoutClicked = true;
                up.setImageDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.ic_keyboard_arrow_up_black_24dp));
            }
        });

        if (SharedPreferencesUtils.getLongFromSharedPrefs(root.getContext(), Constants.TABLE_ID_CONST_STRING) != 12
                && SharedPreferencesUtils.getLongFromSharedPrefs(root.getContext(), Constants.TABLE_ID_CONST_STRING) != 0) {
            if (SharedPreferencesUtils.getBooleanFromSharedPrefs(root.getContext(), Constants.TABLE_EXISTS_ALREADY_STRING))
                paymentRemaining = true;
            else
                paymentRemaining = false;
        } else {
            paymentRemaining = true;
            isTableOccupied = true;
        }

        addOrder.setOnClickListener(view -> {
            if (SharedPreferencesUtils.getLongFromSharedPrefs(root.getContext(), Constants.TABLE_ID_CONST_STRING) != 12
                    && SharedPreferencesUtils.getLongFromSharedPrefs(root.getContext(), Constants.TABLE_ID_CONST_STRING) != 0) {
                if (paymentRemaining) {
                    Toast.makeText(root.getContext(), getString(R.string.payment_first_string), Toast.LENGTH_SHORT).show();
                } else {
                    if (!foodItemList.isEmpty()) {
                        orderFromCartList.clear();
                        for (CartFoodItems foodItem : foodItemList) {
                            OrderCart orderCart = new OrderCart(foodItem.getFoodItem().getFoodItemId(), foodItem.getQuantity());
                            orderFromCartList.add(orderCart);
                        }
                        SharedPreferencesUtils.saveFoodItemsByCustomerToSharedPrefs(root.getContext(), Constants.FOOD_ITEM_MAP_STRING, cartItemsMap);
                        CustomerSuccess customer = SharedPreferencesUtils.getCustomerFromSharedPrefs(root.getContext(), Constants.CUSTOMER_OBJ_NAME);
                        Long customerId = customer.getPerson().getCustomer().getCustomerId();
                        Long tableId = SharedPreferencesUtils.getLongFromSharedPrefs(root.getContext(), Constants.TABLE_ID_CONST_STRING);
                        System.out.println("My Cart Table ID: " + tableId);
                        SharedPreferencesUtils.deleteBooleanFromSharedPrefs(root.getContext(), Constants.TABLE_EXISTS_ALREADY_STRING);
                        SharedPreferencesUtils.saveBooleanToSharedPrefs(root.getContext(), Constants.TABLE_EXISTS_ALREADY_STRING, true);

                        Order order = new Order(customerId, orderFromCartList, tableId);
                        ApiService api = RetrofitClient.getApiService();
                        Call<OrderSuccess> orderCall = api.createOrder(order);
                        orderCall.enqueue(new Callback<OrderSuccess>() {
                            @Override
                            public void onResponse(Call<OrderSuccess> call, Response<OrderSuccess> response) {
                                OrderSuccess orderSuccess = response.body();

                                if (response.code() == 200) {
                                    SharedPreferencesUtils.saveBooleanToSharedPrefs(root.getContext(), Constants.TABLE_EXISTS_ALREADY_STRING, true);
                                    Toast.makeText(root.getContext(), getString(R.string.order_successful_message_string), Toast.LENGTH_SHORT).show();
                                    viewBookingDialog(orderSuccess.getId(), tableId);
                                } else {
                                    Toast.makeText(root.getContext(), root.getContext().getString(R.string.order_error_message_string), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<OrderSuccess> call, Throwable t) {
                                new AlertDialog.Builder(root.getContext())
                                        .setMessage(getString(R.string.order_error_message_string) + "\n" +
                                                t.getMessage())
                                        .setCancelable(true)
                                        .show();
                            }
                        });
                    } else {
                        Toast.makeText(root.getContext(), getString(R.string.cart_empty_message_string), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                if (isTableOccupied)
                    ((CustomerHomeActivity) getActivity()).viewBookingDialog();
            }
        });

        return root;
    }

    private void viewBookingDialog(Long orderId, Long tableId) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(root.getContext());
        builder.setCancelable(true);
        LayoutInflater inflater = this.getLayoutInflater();
        View tableBooking = inflater.inflate(R.layout.layout_payment, null);
        builder.setView(tableBooking);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        AppCompatButton cash = tableBooking.findViewById(R.id.cash);
        AppCompatButton card = tableBooking.findViewById(R.id.card);
        ConstraintLayout cashLayout = tableBooking.findViewById(R.id.cashLayout);
        ConstraintLayout cardLayout = tableBooking.findViewById(R.id.cardLayout);
        TextInputEditText nameOnCard = tableBooking.findViewById(R.id.cardNumber);
        TextInputEditText expiryDate = tableBooking.findViewById(R.id.expiryDate);
        TextInputEditText cvv = tableBooking.findViewById(R.id.cvv);
        TextInputEditText name = tableBooking.findViewById(R.id.name);
        TextInputEditText email = tableBooking.findViewById(R.id.email);
        TextInputEditText tableNumber = tableBooking.findViewById(R.id.tableNumber);
        AppCompatButton payNow = tableBooking.findViewById(R.id.payNow);

        tableNumber.setText(String.valueOf(tableId));
        Calendar cal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateListener = (datePicker, year, month, day) -> {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);

            String myFormat = "yy/MM";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            expiryDate.setText(sdf.format(cal.getTime()));
        };
        expiryDate.setOnClickListener(view -> new DatePickerDialog(root.getContext(), dateListener, cal
                .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show());
        cash.setOnClickListener(view -> {
            cash.setBackgroundDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.drawable_table_booking_selected));
            cash.setTextColor(ContextCompat.getColor(root.getContext(), android.R.color.white));
            card.setBackgroundDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.drawable_table_booking_unselected));
            card.setTextColor(ContextCompat.getColor(root.getContext(), android.R.color.black));
            cashLayout.setVisibility(View.VISIBLE);
            cardLayout.setVisibility(View.GONE);
        });
        card.setOnClickListener(view -> {
            card.setBackgroundDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.drawable_table_booking_selected));
            card.setTextColor(ContextCompat.getColor(root.getContext(), android.R.color.white));
            cash.setBackgroundDrawable(ContextCompat.getDrawable(root.getContext(), R.drawable.drawable_table_booking_unselected));
            cash.setTextColor(ContextCompat.getColor(root.getContext(), android.R.color.black));
            cardLayout.setVisibility(View.VISIBLE);
            cashLayout.setVisibility(View.GONE);
        });
        payNow.setOnClickListener(view -> {
            ApiService api = RetrofitClient.getApiService();
            Call<GeneralError> call = api.getPaymentConfirmation(orderId);

            call.enqueue(new Callback<GeneralError>() {
                @Override
                public void onResponse(Call<GeneralError> call, Response<GeneralError> response) {
                    GeneralError payment = response.body();

                    if (response.code() == 200) {
                        Toast.makeText(root.getContext(), payment.getMessage(), Toast.LENGTH_SHORT).show();

                        SharedPreferencesUtils.deleteLongFromSharedPrefs(root.getContext(), Constants.TABLE_ID_CONST_STRING);
                        SharedPreferencesUtils.deleteBooleanFromSharedPrefs(root.getContext(), Constants.TABLE_EXISTS_ALREADY_STRING);
                        SharedPreferencesUtils.saveBooleanToSharedPrefs(root.getContext(), Constants.TABLE_EXISTS_ALREADY_STRING, false);
                        cartItemsMap.remove(customerID);
                        foodItemList.clear();
                        adapter.notifyDataSetChanged();
                        noItems.setVisibility(View.VISIBLE);
                        foodPrice.setText(getString(R.string.default_cart_price_string));
                        SharedPreferencesUtils.removeCartItems(root.getContext(), Constants.FOOD_ITEM_MAP_STRING);
                        SharedPreferencesUtils.saveFoodItemsByCustomerToSharedPrefs(root.getContext(), Constants.FOOD_ITEM_MAP_STRING, cartItemsMap);
                        dialog.dismiss();

                        startActivity(new Intent(getContext(), CustomerHomeActivity.class));
                        getActivity().finish();
                        //Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(R.id.action_menu_my_cart_to_menu_menu_card_repeat);
                    } else {
                        Toast.makeText(root.getContext(), getString(R.string.payment_failure_message_string), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<GeneralError> call, Throwable t) {
                    Toast.makeText(root.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        });
    }

    @Override
    public void onTotalComputed(Double total) {
        Double taxes = total / 1.15;

        if (total == 0.0)
            noItems.setVisibility(View.VISIBLE);

        foodPrice.setText(getString(R.string.default_price_string, df.format(total)));

        totalTxt.setText(df.format(total));
        totalTax.setText(df.format((total - taxes)));
        totalCost.setText(df.format(taxes));
    }

    @Override
    public void saveFoodItems(String objectName, List<CartFoodItems> foodItems) {
        SharedPreferencesUtils.saveFoodItemsToSharedPrefs(root.getContext(), objectName, foodItems);
        cartItemsMap.put(customerID, foodItems);
        SharedPreferencesUtils.saveFoodItemsByCustomerToSharedPrefs(root.getContext(), objectName, cartItemsMap);
    }
}
