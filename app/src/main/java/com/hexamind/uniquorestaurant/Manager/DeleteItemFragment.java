package com.hexamind.uniquorestaurant.Manager;


import android.os.Bundle;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class DeleteItemFragment extends Fragment {
    private View root;
    private RecyclerView recyclerView;
    private List<FoodItems> foodItems = new ArrayList<>();
    private DeleteFoodItemAdapter adapter;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_delete_item, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        progress = root.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<FoodItems>> call = apiService.getAllFoodItems();

        call.enqueue(new Callback<List<FoodItems>>() {
            @Override
            public void onResponse(Call<List<FoodItems>> call, Response<List<FoodItems>> response) {
                progress.setVisibility(View.GONE);
                if (response.code() == 200) {
                    foodItems = response.body();

                    recyclerView = root.findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    adapter = new DeleteFoodItemAdapter(foodItems, root.getContext());
                    recyclerView.setAdapter(adapter);
                } else if (response.code() == 400) {
                    Toast.makeText(root.getContext(), getString(R.string.four_error_message_string), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 500 ||  response.code() == 501) {
                    Toast.makeText(root.getContext(), getString(R.string.five_error_message_string), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(root.getContext(), getString(R.string.general_error_message_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FoodItems>> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(root.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
