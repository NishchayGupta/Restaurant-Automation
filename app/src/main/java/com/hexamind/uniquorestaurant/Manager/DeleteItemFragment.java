package com.hexamind.uniquorestaurant.Manager;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hexamind.uniquorestaurant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteItemFragment extends Fragment {
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_delete_item, container, false);

        return root;
    }
}
