package com.hexamind.uniquorestaurant.Manager;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.Data.FoodItemsManager;
import com.hexamind.uniquorestaurant.Data.GeneralError;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;
import com.hexamind.uniquorestaurant.Utils.Constants;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UpdateItemFragment extends Fragment {
    private View root;
    private ImageView foodImage;
    private EditText itemName, itemCost;
    private AppCompatButton updateFoodItem;
    private Long foodItemId;
    private static final int GALLERY_REQUEST_CODE = 2;
    private String mainPath = "";
    private StorageReference ref;
    private OnBackPressedCallback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getActivity(), R.id.managerNavHostFragment).navigate(R.id.action_updateItemFragment_to_menu_edit_item);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_update_item, container, false);

        foodImage = root.findViewById(R.id.imageView2);
        itemName = root.findViewById(R.id.itemName);
        itemCost = root.findViewById(R.id.itemCost);
        updateFoodItem = root.findViewById(R.id.updateFoodItem);
        ref = FirebaseStorage.getInstance().getReference();

        foodItemId = SharedPreferencesUtils.getLongFromSharedPrefs(root.getContext(), Constants.FOOD_ITEM_ID);
        ApiService apiService = RetrofitClient.getApiService();
        Call<FoodItems> call = apiService.getSpecificFoodItem(foodItemId);

        call.enqueue(new Callback<FoodItems>() {
            @Override
            public void onResponse(Call<FoodItems> call, Response<FoodItems> response) {
                FoodItems foodItem = response.body();

                mainPath = foodItem.getFoodItemPicture();
                Glide.with(root.getContext())
                        .load(foodItem.getFoodItemPicture())
                        .into(foodImage);
                itemName.setText(foodItem.getFoodItemName());
                itemCost.setText(String.valueOf(foodItem.getFoodItemPrice()));
            }

            @Override
            public void onFailure(Call<FoodItems> call, Throwable t) {

            }
        });
        updateFoodItem.setOnClickListener(view -> {
            if (itemName.getText().toString().isEmpty() || itemCost.getText().toString().isEmpty()) {
                if (itemName.getText().toString().isEmpty()) {
                    itemName.setError(getString(R.string.invalid_item_name));
                } else if (itemCost.getText().toString().isEmpty()) {
                    itemCost.setError(getString(R.string.invalid_item_cost));
                }
            } else {
                FoodItemsManager updatedFoodItems = new FoodItemsManager(itemName.getText().toString(), Double.parseDouble(itemCost.getText().toString()), mainPath);
                Call<GeneralError> callUpdate = apiService.updateFoodItem(foodItemId, updatedFoodItems);

                callUpdate.enqueue(new Callback<GeneralError>() {
                    @Override
                    public void onResponse(Call<GeneralError> call, Response<GeneralError> response) {
                        GeneralError success = response.body();

                        Toast.makeText(root.getContext(), success.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<GeneralError> call, Throwable t) {
                        Toast.makeText(root.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return root;
    }
}
