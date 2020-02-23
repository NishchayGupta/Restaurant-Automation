package com.hexamind.uniquorestaurant.Cashier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexamind.uniquorestaurant.Chef.ChefOrdersAdapter;
import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.R;

import java.util.ArrayList;
import java.util.List;

public class CashierActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChefOrdersAdapter adapter;
    private List<Order> orderList = new ArrayList<>();
    private ImageView back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
    }
}
