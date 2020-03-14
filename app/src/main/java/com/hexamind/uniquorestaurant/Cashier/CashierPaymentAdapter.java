package com.hexamind.uniquorestaurant.Cashier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hexamind.uniquorestaurant.Data.CartFoodItems;
import com.hexamind.uniquorestaurant.Data.ChefOrders;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.Manager.ManagerActivity;
import com.hexamind.uniquorestaurant.R;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CashierPaymentAdapter extends RecyclerView.Adapter<CashierPaymentAdapter.CashierPaymentViewHolder> {
    private List<ChefOrders> paymentList;
    private Context context;
    private boolean expanded = false;
    private DecimalFormat df = new DecimalFormat("#.##");

    public CashierPaymentAdapter(List<ChefOrders> paymentList, Context context) {
        this.paymentList = paymentList;
        this.context = context;
    }

    class CashierPaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tableNumber, itemOrderList, totalAmount, paymentType;
        ImageView expand;
        AppCompatButton viewReceipt;
        RelativeLayout layout;
        View view;

        public CashierPaymentViewHolder(@NonNull View itemView) {
            super(itemView);

            tableNumber = itemView.findViewById(R.id.tableNumber);
            itemOrderList = itemView.findViewById(R.id.itemsOrderList);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            paymentType = itemView.findViewById(R.id.paymentType);
            expand = itemView.findViewById(R.id.expand);
            viewReceipt = itemView.findViewById(R.id.viewReceipt);
            layout = itemView.findViewById(R.id.layout);
            view = itemView.findViewById(R.id.view);
        }
    }

    @NonNull
    @Override
    public CashierPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CashierPaymentViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_cashier_payment, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CashierPaymentViewHolder holder, int position) {
        ChefOrders orders = paymentList.get(position);

        holder.expand.setOnClickListener(view -> {
            if (expanded) {
                holder.layout.setVisibility(View.GONE);
                holder.view.setVisibility(View.GONE);
                expanded = false;
            } else {
                holder.layout.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.VISIBLE);
                expanded = true;
            }
        });
        holder.itemOrderList.setText("");
        for (CartFoodItems items : orders.getFoodItemOrder()) {
            holder.itemOrderList.append(items.getFoodItem().getFoodItemName() + " x " + items.getQuantity() + "\n");
        }
        holder.totalAmount.setText(context.getString(R.string.default_price_string, String.valueOf(orders.getTotalCost())));
        holder.tableNumber.setText(context.getString(R.string.default_table_string, String.valueOf(orders.getTable().getId())));
        holder.viewReceipt.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            LayoutInflater inflater = ((CashierActivity) context).getLayoutInflater();
            View confirm = inflater.inflate(R.layout.layout_invoice, null);
            builder.setView(confirm);

            AlertDialog dialog = builder.create();
            dialog.show();

            TextView invoiceDate = confirm.findViewById(R.id.invoiceDate);
            TextView items = confirm.findViewById(R.id.items);
            TextView subTotal = confirm.findViewById(R.id.subTotal);
            TextView taxes = confirm.findViewById(R.id.taxes);
            TextView total = confirm.findViewById(R.id.total);
            AppCompatButton close = confirm.findViewById(R.id.close);

            Calendar cal = Calendar.getInstance();
            cal.setTime(orders.getTable().getBookingDateTime());
            String dateString = cal.get(Calendar.DATE) + " " + getMonth(cal.get(Calendar.MONTH) + 1) + ", " + cal.get(Calendar.YEAR);
            invoiceDate.setText(dateString);
            items.setText("");
            for (CartFoodItems itemsOrdered : orders.getFoodItemOrder()) {
                items.append(itemsOrdered.getFoodItem().getFoodItemName() + " x " + itemsOrdered.getQuantity() + "\n");
            }
            subTotal.setText(df.format(orders.getTotalCost()));
            taxes.setText((df.format(orders.getTotalCost() * 1.15)));
            total.setText((df.format(orders.getTotalCost() + (orders.getTotalCost() * 1.15))));
            close.setOnClickListener(view1 -> {
                dialog.dismiss();
            });
        });
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
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
}
