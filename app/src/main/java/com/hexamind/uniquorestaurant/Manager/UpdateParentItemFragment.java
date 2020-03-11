package com.hexamind.uniquorestaurant.Manager;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hexamind.uniquorestaurant.Customer.MenuCardAdapter;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateParentItemFragment extends Fragment {
    private View root;
    private RecyclerView recyclerView;
    private List<FoodItems> foodItems = new ArrayList<>();
    private UpdateFoodItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_parent_update_item, container, false);

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<FoodItems>> call = apiService.getAllFoodItems();

        call.enqueue(new Callback<List<FoodItems>>() {
            @Override
            public void onResponse(Call<List<FoodItems>> call, Response<List<FoodItems>> response) {
                foodItems = response.body();

                recyclerView = root.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                adapter = new UpdateFoodItemAdapter(foodItems, root.getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<FoodItems>> call, Throwable t) {
                Toast.makeText(root.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
