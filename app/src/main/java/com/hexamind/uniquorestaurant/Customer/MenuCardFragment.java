package com.hexamind.uniquorestaurant.Customer;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hexamind.uniquorestaurant.Data.FoodItem;
import com.hexamind.uniquorestaurant.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuCardFragment extends Fragment {

    private RecyclerView recyclerView;
    private View root;
    private MenuCardAdapter adapter;
    private ArrayList<FoodItem> foodItemList = new ArrayList<>();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_menu_card, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MenuCardAdapter(root.getContext(), foodItemList);
        recyclerView.setAdapter(adapter);

        return root;
    }

}
