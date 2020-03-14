package com.hexamind.uniquorestaurant.Manager;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hexamind.uniquorestaurant.Data.FoodItemsManager;
import com.hexamind.uniquorestaurant.Data.GeneralError;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;

import static android.app.Activity.RESULT_OK;

public class AddItemFragment extends Fragment {
    private View root;
    private ImageView addFoodImage, foodImage;
    private View imageBg;
    private EditText itemName, itemCost;
    private AppCompatButton addFoodItem;
    private StorageReference ref;
    private static final int GALLERY_REQUEST_CODE = 1;
    private String mainPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_item, container, false);

        addFoodImage = root.findViewById(R.id.addFoodImage);
        foodImage = root.findViewById(R.id.imageView2);
        imageBg = root.findViewById(R.id.imageBg);
        itemName = root.findViewById(R.id.itemName);
        itemCost = root.findViewById(R.id.itemCost);
        addFoodItem = root.findViewById(R.id.addFoodItem);

        ref = FirebaseStorage.getInstance().getReference();
        addFoodImage.setOnClickListener(view -> pickFromGallery());
        addFoodItem.setOnClickListener(view -> {
            if (addFoodImage.getVisibility() == View.VISIBLE || itemName.getText().toString().isEmpty() || itemCost.getText().toString().isEmpty()){
                if (addFoodImage.getVisibility() == View.VISIBLE) {
                    Toast.makeText(root.getContext(), getString(R.string.food_item_image_unsuccess_string), Toast.LENGTH_SHORT).show();
                } else if (itemName.getText().toString().isEmpty()) {
                    itemName.setError(getString(R.string.invalid_item_name));
                } else if (itemCost.getText().toString().isEmpty()) {
                    itemCost.setError(getString(R.string.invalid_item_cost));
                }
            } else {
                FoodItemsManager foodItem = new FoodItemsManager(itemName.getText().toString(), Double.parseDouble(itemCost.getText().toString()), mainPath);
                ApiService apiService = RetrofitClient.getApiService();
                Call<GeneralError> call = apiService.addFoodItem(foodItem);

                call.enqueue(new Callback<GeneralError>() {
                    @Override
                    public void onResponse(Call<GeneralError> call, Response<GeneralError> response) {
                        GeneralError success = response.body();

                        Toast.makeText(root.getContext(), success.getMessage(), Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getActivity(), R.id.managerNavHostFragment).navigate(R.id.action_menu_add_item_to_menu_add_item_success);
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

    private void pickFromGallery() {
        Intent chooseImage = new Intent(Intent.ACTION_PICK);
        chooseImage.setType("image/*");
        String[] mimeTypes = {"images/jpeg", "images/png"};
        chooseImage.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(chooseImage, GALLERY_REQUEST_CODE);
    }

    private void addImageToFirebase(Uri file, String path) {
        String imgName = path.split("/")[path.split("/").length - 1];
        StorageReference imgRef = ref.child(imgName);

        imgRef.putFile(file).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return imgRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                mainPath = downloadUri.toString();
            } else {
                Toast.makeText(root.getContext(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    foodImage.setImageURI(selectedImage);
                    mainPath = getPath(selectedImage);
                    addImageToFirebase(selectedImage, mainPath);
                    addFoodImage.setVisibility(View.GONE);
                    imageBg.setVisibility(View.GONE);

                    break;
            }
    }

    private String getPath(Uri contentUri) {
        String[] column = { MediaStore.Images.Media.DATA };
        Cursor cursor = root.getContext().getContentResolver().query(contentUri, column, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(columnIndex);
        cursor.close();

        return path;
    }
}
