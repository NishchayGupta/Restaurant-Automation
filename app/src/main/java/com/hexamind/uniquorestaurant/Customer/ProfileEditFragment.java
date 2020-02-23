package com.hexamind.uniquorestaurant.Customer;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.hexamind.uniquorestaurant.R;

public class ProfileEditFragment extends Fragment {
    private View root;
    private CircleImageView profileImage;
    private TextInputEditText name, email, password, phoneNumber;
    private AppCompatButton done;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        profileImage = root.findViewById(R.id.profileImage);
        name = root.findViewById(R.id.name);
        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        phoneNumber = root.findViewById(R.id.phoneNumber);
        done = root.findViewById(R.id.done);

        done.setOnClickListener(view -> {
            if (name.getText().length() > 0 || email.getText().length() > 0 || password.getText().length() > 0 || phoneNumber.getText().length() > 0) {

            } else {
                if (name.getText().length() == 0) {
                    name.setError(getString(R.string.invalid_name_error_string));
                } else if (email.getText().length() == 0) {
                    email.setError(getString(R.string.invalid_email_error_string));
                } else if (password.getText().length() == 0) {
                    password.setError(getString(R.string.invalid_password_error_string));
                } else if (phoneNumber.getText().length() == 0) {
                    phoneNumber.setError(getString(R.string.invalid_phone_number_error_string));
                }
            }
        });

        return root;
    }

}
