package com.hexamind.uniquorestaurant.Chef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hexamind.uniquorestaurant.Data.ChefOrders;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.LoginActivity;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;
import com.hexamind.uniquorestaurant.Utils.Constants;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class ChefOrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChefOrdersAdapter adapter;
    private List<ChefOrders> orderList = new ArrayList<>();
    private ImageView back;
    private TextView title, noOrders;
    private CustomerSuccess customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_orders);

        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        noOrders = findViewById(R.id.noOrders);

        title.setText(getString(R.string.chef_orders_title_string));
        customer = SharedPreferencesUtils.getCustomerFromSharedPrefs(this, Constants.CUSTOMER_OBJ_NAME);

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<ChefOrders>> call = apiService.getAllOrdersForChef();
        call.enqueue(new Callback<List<ChefOrders>>() {
            @Override
            public void onResponse(Call<List<ChefOrders>> call, Response<List<ChefOrders>> response) {
                orderList = response.body();

                if (orderList.isEmpty()) {
                    noOrders.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noOrders.setVisibility(View.GONE);
                    adapter = new ChefOrdersAdapter(orderList, ChefOrdersActivity.this, customer);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ChefOrdersActivity.this));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ChefOrders>> call, Throwable t) {
                Toast.makeText(ChefOrdersActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(view -> {
            startActivity(new Intent(ChefOrdersActivity.this, LoginActivity.class));
            Toast.makeText(this, getString(R.string.logout_success_message_string), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChefOrdersActivity.this, LoginActivity.class));
        Toast.makeText(this, getString(R.string.logout_success_message_string), Toast.LENGTH_SHORT).show();
        finish();
    }
}
