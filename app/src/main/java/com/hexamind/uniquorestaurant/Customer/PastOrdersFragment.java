package com.hexamind.uniquorestaurant.Customer;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hexamind.uniquorestaurant.Data.ChefOrders;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.Data.GeneralError;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;
import com.hexamind.uniquorestaurant.Utils.Constants;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class PastOrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private View root;
    private PastOrdersAdapter adapter;
    private List<ChefOrders> orderList = new ArrayList<>();
    private CustomerSuccess customer;
    private Long customerId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_past_orders, container, false);

        customer = SharedPreferencesUtils.getCustomerFromSharedPrefs(root.getContext(), Constants.CUSTOMER_OBJ_NAME);
        customerId = customer.getPerson().getCustomer().getCustomerId();

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<ChefOrders>> callOrders = apiService.getAllOrders(customerId);
        callOrders.enqueue(new Callback<List<ChefOrders>>() {
            @Override
            public void onResponse(Call<List<ChefOrders>> call, Response<List<ChefOrders>> response) {
                orderList  = response.body();

                recyclerView = root.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                adapter = new PastOrdersAdapter(root.getContext(), orderList, customer);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ChefOrders>> call, Throwable t) {
                Toast.makeText(root.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
