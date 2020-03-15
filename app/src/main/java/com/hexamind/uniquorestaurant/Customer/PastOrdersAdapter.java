package com.hexamind.uniquorestaurant.Customer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hexamind.uniquorestaurant.Data.CartFoodItems;
import com.hexamind.uniquorestaurant.Data.ChefOrders;
import com.hexamind.uniquorestaurant.Data.Customer;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.Data.GeneralError;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;
import com.hexamind.uniquorestaurant.Utils.Constants;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastOrdersAdapter extends RecyclerView.Adapter<PastOrdersAdapter.PastOrdersViewHolder> {
    private Context context;
    private List<ChefOrders> orderList;
    private CustomerSuccess customer;
    private DecimalFormat df = new DecimalFormat("#.##");

    public PastOrdersAdapter(Context context, List<ChefOrders> orderList, CustomerSuccess customer) {
        this.context = context;
        this.orderList = orderList;
        this.customer = customer;
    }

    public class PastOrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView itemsOrdered, dateOrdered, amountPaid, paidText;

        public PastOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            itemsOrdered = itemView.findViewById(R.id.itemsOrdered);
            dateOrdered = itemView.findViewById(R.id.dateOrdered);
            amountPaid = itemView.findViewById(R.id.amountPaid);
            paidText = itemView.findViewById(R.id.paidText);
        }
    }

    @NonNull
    @Override
    public PastOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PastOrdersViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_past_orders, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PastOrdersViewHolder holder, int position) {
        ChefOrders order = orderList.get(position);

        holder.itemsOrdered.setText("");
        for (CartFoodItems foodItems: order.getFoodItemOrder()) {
            holder.itemsOrdered.append(context.getString(R.string.chef_orders_items_view, foodItems.getFoodItem().getFoodItemName(), String.valueOf(foodItems.getQuantity())));
        }
        Calendar cal = Calendar.getInstance();
        if (order.getTable().getId() != 11)
            cal.setTime(order.getTable().getBookingDateTime());
        else
            cal.setTime(order.getTable().getStartDateTime());

        String dateString = cal.get(Calendar.DATE) + " " + getMonth(cal.get(Calendar.MONTH)+1) + ", " + cal.get(Calendar.YEAR);
        holder.dateOrdered.setText(dateString);
        holder.amountPaid.setText(context.getString(R.string.default_price_string, df.format((order.getTotalCost() * 1.15))));
        if (order.getExistingOrder()) {
            holder.paidText.setText(context.getString(R.string.not_paid_string));
            holder.paidText.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        } else {
            holder.paidText.setText(context.getString(R.string.paid_string));
            holder.paidText.setTextColor(Color.parseColor("#4CAF50"));
        }
        holder.paidText.setOnClickListener(view -> {
            if (holder.paidText.getText().toString().equals(context.getString(R.string.not_paid_string))) {
                showPayExistingOrderDialog(order, holder);
            } else {
                showInvoiceDialog(order);
            }
        });
    }

    private void showInvoiceDialog(ChefOrders order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        LayoutInflater inflater = ((CustomerHomeActivity) context).getLayoutInflater();
        View invoice = inflater.inflate(R.layout.layout_invoice, null);
        builder.setView(invoice);

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView invoiceDate = invoice.findViewById(R.id.invoiceDate);
        TextView items = invoice.findViewById(R.id.items);
        TextView subTotal = invoice.findViewById(R.id.subTotal);
        TextView taxes = invoice.findViewById(R.id.taxes);
        TextView total = invoice.findViewById(R.id.total);
        TextView close = invoice.findViewById(R.id.close);

        close.setText(context.getString(R.string.close_dialog_string));
        close.setOnClickListener(view -> dialog.dismiss());
        Calendar cal = Calendar.getInstance();
        if (order.getTable().getId() == 11) {
            cal.setTime(order.getTable().getStartDateTime());
        } else {
            cal.setTime(order.getTable().getBookingDateTime());
        }
        String dateString = cal.get(Calendar.DATE) + " " + getMonth(cal.get(Calendar.MONTH)+1) + ", " + cal.get(Calendar.YEAR);
        invoiceDate.setText(dateString);
        items.setText("");
        for (CartFoodItems orderedItems : order.getFoodItemOrder()) {
            items.append(context.getString(R.string.chef_orders_items_view, orderedItems.getFoodItem().getFoodItemName(), String.valueOf(orderedItems.getQuantity())));
        }
        subTotal.setText(context.getString(R.string.default_price_string, df.format(order.getTotalCost())));
        total.setText(context.getString(R.string.default_price_string, df.format((order.getTotalCost() * 1.15))));
        taxes.setText(context.getString(R.string.default_price_string, df.format(((order.getTotalCost() * 1.15) - order.getTotalCost()))));
    }

    private void showPayExistingOrderDialog(ChefOrders order, PastOrdersViewHolder holder) {
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

        title.setText(context.getString(R.string.confirm_payment_title_string));
        message.setText(context.getString(R.string.confirm_payment_message_string));
        yes.setOnClickListener(view1 -> {
            viewBookingDialog(order, order.getId().longValue(), order.getTable().getId(), holder);
            dialog.dismiss();
        });
        no.setOnClickListener(view1 -> {
            dialog.dismiss();
        });
    }

    private String getMonth(int monthInt) {
        String month;
        switch (monthInt) {
            case 1:
                month = "January";
                break;
            case 2:
                month =  "February";
                break;
            case 3:
                month =  "March";
                break;
            case 4:
                month =  "April";
                break;
            case 5:
                month =  "May";
                break;
            case 6:
                month =  "June";
                break;
            case 7:
                month =  "July";
                break;
            case 8:
                month =  "August";
                break;
            case 9:
                month =  "September";
                break;
            case 10:
                month =  "October";
                break;
            case 11:
                month =  "November";
                break;
            case 12:
                month =  "December";
                break;
            default:
                month =  "";
                break;
        }

        return month;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private void viewBookingDialog(ChefOrders order, Long orderId, Long tableId, PastOrdersViewHolder holder) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setCancelable(true);
        LayoutInflater inflater = ((CustomerHomeActivity) context).getLayoutInflater();
        View tableBooking = inflater.inflate(R.layout.layout_payment, null);
        builder.setView(tableBooking);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        AppCompatButton cash = tableBooking.findViewById(R.id.cash);
        AppCompatButton card = tableBooking.findViewById(R.id.card);
        ConstraintLayout cashLayout = tableBooking.findViewById(R.id.cashLayout);
        ConstraintLayout cardLayout = tableBooking.findViewById(R.id.cardLayout);
        TextInputEditText cardNumber = tableBooking.findViewById(R.id.cardNumber);
        TextInputEditText expiryDate = tableBooking.findViewById(R.id.expiryDate);
        TextInputEditText cvv = tableBooking.findViewById(R.id.cvv);
        TextInputEditText name = tableBooking.findViewById(R.id.name);
        TextInputEditText email = tableBooking.findViewById(R.id.email);
        TextInputEditText tableNumber = tableBooking.findViewById(R.id.tableNumber);
        AppCompatButton payNow = tableBooking.findViewById(R.id.payNow);
        TextInputLayout tableNumberInput = tableBooking.findViewById(R.id.tableNumberInput);

        tableNumber.setText(String.valueOf(tableId));
        cash.setOnClickListener(view -> {
            cash.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.drawable_table_booking_selected));
            cash.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            card.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.drawable_table_booking_unselected));
            card.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            cashLayout.setVisibility(View.VISIBLE);
            cardLayout.setVisibility(View.GONE);
        });
        card.setOnClickListener(view -> {
            card.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.drawable_table_booking_selected));
            card.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            cash.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.drawable_table_booking_unselected));
            cash.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            cardLayout.setVisibility(View.VISIBLE);
            cashLayout.setVisibility(View.GONE);
        });
        Calendar cal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            expiryDate.setText(sdf.format(cal.getTime()));
        };
        if (order.getTable().getId() == 11)
            tableNumberInput.setVisibility(View.GONE);
        else
            tableNumberInput.setVisibility(View.VISIBLE);
        expiryDate.setOnClickListener(view -> {
            new DatePickerDialog(context, date, cal
                    .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });
        payNow.setOnClickListener(view -> {
            if (cashLayout.getVisibility() == View.VISIBLE) {
                if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || !isValidUsername(email.getText().toString())) {
                    if (name.getText().toString().isEmpty()) {
                        name.setError(context.getString(R.string.invalid_name_error_string));
                    } else if (email.getText().toString().isEmpty()) {
                        email.setError(context.getString(R.string.invalid_email_error_string));
                    } else if (isValidUsername(email.getText().toString())) {
                        email.setError(context.getString(R.string.invalid_email_error_string));
                    }
                } else {
                    ApiService api = RetrofitClient.getApiService();
                    Call<GeneralError> call = api.getPaymentConfirmation(orderId);

                    call.enqueue(new Callback<GeneralError>() {
                        @Override
                        public void onResponse(Call<GeneralError> call, Response<GeneralError> response) {
                            GeneralError payment = response.body();

                            if (response.code() == 200) {
                                Toast.makeText(context, payment.getMessage(), Toast.LENGTH_SHORT).show();
                                Map<Long, Long> tableIdMap = SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(context, Constants.TABLE_ID_MAP_CONST_STRING);
                                tableIdMap.remove(customer.getPerson().getCustomer().getCustomerId());
                                SharedPreferencesUtils.saveTableIdByCustomerToSharedPrefs(context, Constants.TABLE_ID_MAP_CONST_STRING, tableIdMap);
                                SharedPreferencesUtils.deleteBooleanFromSharedPrefs(context, Constants.TABLE_EXISTS_ALREADY_STRING);
                                Toast.makeText(context, context.getString(R.string.payment_success_string), Toast.LENGTH_SHORT).show();

                                Map<Long, List<CartFoodItems>> cartItemsMap = SharedPreferencesUtils.getFoodItemsByCustomerFromSharedPrefs(context, Constants.FOOD_ITEM_MAP_STRING);
                                List<CartFoodItems> cartItems = cartItemsMap.get(customer.getPerson().getCustomer().getCustomerId());
                                if (cartItems != null) {
                                    cartItemsMap.remove(customer.getPerson().getCustomer().getCustomerId());
                                    SharedPreferencesUtils.removeCartItems(context, Constants.FOOD_ITEM_MAP_STRING);
                                    SharedPreferencesUtils.saveFoodItemsByCustomerToSharedPrefs(context, Constants.FOOD_ITEM_MAP_STRING, cartItemsMap);
                                }
                                order.setExistingOrder(false);
                                holder.paidText.setText(context.getString(R.string.paid_string));
                                holder.paidText.setTextColor(Color.parseColor("#4CAF50"));
                                holder.paidText.setEnabled(false);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(context, context.getString(R.string.payment_failure_message_string), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<GeneralError> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            } else if (cardLayout.getVisibility() == View.VISIBLE) {
                if (cardNumber.getText().toString().isEmpty() || expiryDate.getText().toString().isEmpty() || cvv.getText().toString().isEmpty()) {
                    if (cardNumber.getText().toString().isEmpty()) {
                        cardNumber.setError(context.getString(R.string.invalid_card_number_error_string));
                    } else if (expiryDate.getText().toString().isEmpty()) {
                        expiryDate.setError(context.getString(R.string.invalid_date_error_string));
                    } else if (cvv.getText().toString().isEmpty()) {
                        cvv.setError(context.getString(R.string.invalid_cvv_error_string));
                    }
                } else {
                    ApiService api = RetrofitClient.getApiService();
                    Call<GeneralError> call = api.getPaymentConfirmation(orderId);

                    call.enqueue(new Callback<GeneralError>() {
                        @Override
                        public void onResponse(Call<GeneralError> call, Response<GeneralError> response) {
                            GeneralError payment = response.body();

                            if (response.code() == 200) {
                                Toast.makeText(context, payment.getMessage(), Toast.LENGTH_SHORT).show();
                                Map<Long, Long> tableIdMap = SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(context, Constants.TABLE_ID_MAP_CONST_STRING);
                                tableIdMap.remove(customer.getPerson().getCustomer().getCustomerId());
                                SharedPreferencesUtils.saveTableIdByCustomerToSharedPrefs(context, Constants.TABLE_ID_MAP_CONST_STRING, tableIdMap);
                                SharedPreferencesUtils.deleteBooleanFromSharedPrefs(context, Constants.TABLE_EXISTS_ALREADY_STRING);
                                Toast.makeText(context, context.getString(R.string.payment_success_string), Toast.LENGTH_SHORT).show();

                                Map<Long, List<CartFoodItems>> cartItemsMap = SharedPreferencesUtils.getFoodItemsByCustomerFromSharedPrefs(context, Constants.FOOD_ITEM_MAP_STRING);
                                List<CartFoodItems> cartItems = cartItemsMap.get(customer.getPerson().getCustomer().getCustomerId());
                                if (cartItems != null) {
                                    cartItemsMap.remove(customer.getPerson().getCustomer().getCustomerId());
                                    SharedPreferencesUtils.removeCartItems(context, Constants.FOOD_ITEM_MAP_STRING);
                                    SharedPreferencesUtils.saveFoodItemsByCustomerToSharedPrefs(context, Constants.FOOD_ITEM_MAP_STRING, cartItemsMap);
                                }
                                order.setExistingOrder(false);
                                holder.paidText.setText(context.getString(R.string.paid_string));
                                holder.paidText.setTextColor(Color.parseColor("#4CAF50"));
                                holder.paidText.setEnabled(false);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(context, context.getString(R.string.payment_failure_message_string), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<GeneralError> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private boolean isValidUsername(CharSequence username)  {
        return (!TextUtils.isEmpty(username) && Patterns.EMAIL_ADDRESS.matcher(username).matches());
    }
}
