package com.hexamind.uniquorestaurant.Customer;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hexamind.uniquorestaurant.Data.Order;
import com.hexamind.uniquorestaurant.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PastOrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private View root;
    private PastOrdersAdapter adapter;
    private ArrayList<Order> orderList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_past_orders, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PastOrdersAdapter(root.getContext(), orderList);
        recyclerView.setAdapter(adapter);

        return root;
    }

}
