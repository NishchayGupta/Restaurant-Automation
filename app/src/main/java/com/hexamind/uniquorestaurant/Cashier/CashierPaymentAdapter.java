package com.hexamind.uniquorestaurant.Cashier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CashierPaymentAdapter extends RecyclerView.Adapter<CashierPaymentAdapter.CashierPaymentViewHolder> {
    private List<Order> paymentList;
    private Context context;

    public CashierPaymentAdapter(List<Order> paymentList, Context context) {
        this.paymentList = paymentList;
        this.context = context;
    }

    class CashierPaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tableNumber, itemOrderList, totalAmount, paymentType;
        ImageView expand;
        AppCompatButton viewReceipt;
        ConstraintLayout layout;
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

    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }
}
