package com.hexamind.uniquorestaurant.Cashier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hexamind.uniquorestaurant.Chef.ChefOrdersAdapter;
import com.hexamind.uniquorestaurant.Customer.MenuCardAdapter;
import com.hexamind.uniquorestaurant.Data.ChefOrders;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class CashierActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CashierPaymentAdapter adapter;
    private List<ChefOrders> orderList = new ArrayList<>();
    private ImageView back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        ApiService api = RetrofitClient.getApiService();
        Call<List<ChefOrders>> call = api.getAllOrdersCashier();

        call.enqueue(new Callback<List<ChefOrders>>() {
            @Override
            public void onResponse(Call<List<ChefOrders>> call, Response<List<ChefOrders>> response) {
                orderList = response.body();

                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(CashierActivity.this));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                adapter = new CashierPaymentAdapter(orderList, CashierActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ChefOrders>> call, Throwable t) {
                Toast.makeText(CashierActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
