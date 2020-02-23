package com.hexamind.uniquorestaurant.Chef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.LoginActivity;
import com.hexamind.uniquorestaurant.R;

import java.util.ArrayList;
import java.util.List;

public class ChefOrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChefOrdersAdapter adapter;
    private List<Order> orderList = new ArrayList<>();
    private ImageView back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_orders);

        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);

        adapter = new ChefOrdersAdapter(orderList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        back.setOnClickListener(view -> {
            startActivity(new Intent(ChefOrdersActivity.this, LoginActivity.class));
            Toast.makeText(this, getString(R.string.logout_success_message_string), Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
