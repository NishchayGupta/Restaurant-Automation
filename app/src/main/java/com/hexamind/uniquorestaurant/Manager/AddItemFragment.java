package com.hexamind.uniquorestaurant.Manager;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.hexamind.uniquorestaurant.R;

public class AddItemFragment extends Fragment {
    private View root;
    private ImageView foodImage;
    private TextInputEditText itemName, itemDesc, itemCost;
    private AppCompatButton addFoodItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_item, container, false);

        foodImage = root.findViewById(R.id.foodImage);
        itemName = root.findViewById(R.id.itemName);
        itemDesc = root.findViewById(R.id.itemDesc);
        itemCost = root.findViewById(R.id.itemCost);
        addFoodItem = root.findViewById(R.id.addFoodItem);

        return root;
    }
}
