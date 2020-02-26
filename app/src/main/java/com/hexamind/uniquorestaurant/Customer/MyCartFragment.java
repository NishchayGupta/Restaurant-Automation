package com.hexamind.uniquorestaurant.Customer;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hexamind.uniquorestaurant.Data.Cart;
import com.hexamind.uniquorestaurant.Data.CartFoodItems;
import com.hexamind.uniquorestaurant.Data.FoodItem;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;
import com.hexamind.uniquorestaurant.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment implements OnTotalComputedListener {
    private View root;
    private List<CartFoodItems> foodItemList = new ArrayList<>();
    private MyCartAdapter adapter;
    private RecyclerView recyclerView;
    private List<CartFoodItems> list = new ArrayList<>();
    private TextView foodPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_cart, container, false);

        list = SharedPreferencesUtils.getFoodItemsFromSharedPrefs(root.getContext(), Utils.FOOD_ITEM_STRING);
        recyclerView = root.findViewById(R.id.recyclerView);
        foodPrice = root.findViewById(R.id.foodPrice);

        foodItemList = SharedPreferencesUtils.getFoodItemsFromSharedPrefs(root.getContext(), Utils.FOOD_ITEM_STRING);
        adapter = new MyCartAdapter(foodItemList, root.getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onTotalComputed(Double total) {
        foodPrice.setText(getString(R.string.default_price_string, String.valueOf(total)));
    }
}
