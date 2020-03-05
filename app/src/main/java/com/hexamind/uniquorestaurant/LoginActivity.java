package com.hexamind.uniquorestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.hexamind.uniquorestaurant.Chef.ChefOrdersActivity;
import com.hexamind.uniquorestaurant.Customer.CustomerHomeActivity;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.Data.GeneralError;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;
import com.hexamind.uniquorestaurant.Utils.Constants;
import com.jgabrielfreitas.core.BlurImageView;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private BlurImageView imageView;
    private TextInputEditText username;
    private TextInputEditText password;
    private boolean usernameValid = false;
    private MaterialButton login;
    private TextView register;
    private ConstraintLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageView = findViewById(R.id.imageView);
        username = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        progress = findViewById(R.id.progress);

        imageView.setBlur(2);

        login.setOnClickListener(view -> {
            if (!username.getText().toString().equals("") || !password.getText().toString().equals("")) {
                progress.setVisibility(View.VISIBLE);
                ApiService api = RetrofitClient.getApiService();
                Call<CustomerSuccess> call = api.getCustomerDetails(username.getText().toString(), password.getText().toString());

                call.enqueue(new Callback<CustomerSuccess>() {
                    @Override
                    public void onResponse(Call<CustomerSuccess> call, Response<CustomerSuccess> response) {
                        CustomerSuccess customer = response.body();

                        if (response.code() == 200) {
                            progress.setVisibility(View.GONE);
                            if (customer.getPerson().getCustomer() != null) {
                                SharedPreferencesUtils.saveCustomerToSharedPrefs(LoginActivity.this, Constants.CUSTOMER_OBJ_NAME, customer);
                                startActivity(new Intent(LoginActivity.this, CustomerHomeActivity.class));
                                finish();
                            } else if (customer.getPerson().getChef() != null) {
                                startActivity(new Intent(LoginActivity.this, ChefOrdersActivity.class));
                                finish();
                            }  else if (customer.getPerson().getCashier() != null) {

                            }  else if (customer.getPerson().getManager() != null) {

                            } else
                                Toast.makeText(LoginActivity.this, getString(R.string.problem_getting_customer_details_string), Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 404) {
                            progress.setVisibility(View.GONE);
                            try {
                                Gson gson = new Gson();
                                GeneralError error = gson.fromJson(response.errorBody().string(), GeneralError.class);
                                System.out.println("Error: " + error.getMessage());

                                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                username.setText("");
                                password.setText("");
                                username.requestFocus();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            progress.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, getString(R.string.problem_getting_customer_details_string), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CustomerSuccess> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                if (username.getText().toString().equals("")) {
                    username.setError(getString(R.string.invalid_username_error_string));
                }
                if (password.getText().toString().equals("")) {
                    password.setError(getString(R.string.invalid_password_error_string));
                }
            }
        });

        register.setOnClickListener(view  -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}
