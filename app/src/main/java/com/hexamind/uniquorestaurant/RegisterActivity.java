package com.hexamind.uniquorestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.hexamind.uniquorestaurant.Data.GeneralError;
import com.hexamind.uniquorestaurant.Data.RegisterPost;
import com.hexamind.uniquorestaurant.Data.RegisterSuccess;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;
import com.hexamind.uniquorestaurant.Utils.Constants;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;
import com.jgabrielfreitas.core.BlurImageView;

public class RegisterActivity extends AppCompatActivity {
    private BlurImageView imageView;
    private MaterialCheckBox termsConditions;
    private TextView loginHere;
    private TextInputEditText name, email, password, phoneNumber;
    private boolean usernameValid;
    private MaterialButton register;
    private static final String TAG = RegisterActivity.class.getName();
    private ConstraintLayout progress;
    private ProgressBar progressBar;
    private TextView emailExistsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageView = findViewById(R.id.imageView);
        termsConditions = findViewById(R.id.termsConditions);
        loginHere = findViewById(R.id.loginHere);
        name = findViewById(R.id.name);
        email =  findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phoneNumber);
        register = findViewById(R.id.register);
        progress = findViewById(R.id.progress);
        progressBar = findViewById(R.id.progressBar);
        emailExistsText = findViewById(R.id.emailExistsText);

        imageView.setBlur(2);
        Spannable termsString = new SpannableString(getString(R.string.terms_and_conditions_check_string));
        termsString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.white, null)), 0, termsString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsConditions.setText(termsString + " ");

        Spannable termsSubstring = new SpannableString(getString(R.string.terms_and_conditions_check_substring));
        termsSubstring.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent, null)), 0, termsSubstring.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsConditions.append(termsSubstring);

        Spannable loginString = new SpannableString(getString(R.string.login_here_text));
        loginString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.white, null)), 0, loginString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        loginHere.setText(loginString + " ");

        Spannable loginSubstring = new SpannableString(getString(R.string.login_here_substring_text));
        loginSubstring.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent, null)), 0, loginSubstring.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginHere.append(loginSubstring);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        email.setOnFocusChangeListener((view, hasFocus) -> {
            if (!email.getText().toString().isEmpty()) {
                if (!hasFocus) {
                    if  (isValidUsername(email.getText().toString())) {
                        progressBar.setVisibility(View.VISIBLE);

                        ApiService api = RetrofitClient.getApiService();
                        Call<GeneralError> call = api.checkEmailExists(email.getText().toString());

                        call.enqueue(new Callback<GeneralError>() {
                            @Override
                            public void onResponse(Call<GeneralError> call, Response<GeneralError> response) {
                                if (email.getText().toString().isEmpty() || !isValidUsername(email.getText())) {
                                    Toast.makeText(RegisterActivity.this, getString(R.string.invalid_email_error_string), Toast.LENGTH_SHORT).show();
                                } else {
                                    if (response.code() == 200) {
                                        emailExistsText.setVisibility(View.GONE);
                                    } else if (response.code() == 400) {
                                        emailExistsText.setText(getString(R.string.email_exists_message_string));
                                        emailExistsText.setVisibility(View.VISIBLE);
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<GeneralError> call, Throwable t) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }  else {
                        Toast.makeText(this, getString(R.string.invalid_email_error_string), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (name.getText().length() > 0 && isValidUsername(email.getText().toString()) && password.getText().toString().equals(charSequence))
                    register.setEnabled(true);
                else
                    register.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loginHere.setOnClickListener(view ->
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class))
        );

        termsConditions.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked && name.getText().length() > 0 && isValidUsername(email.getText().toString()) && phoneNumber.getText().length() == 10)
                register.setEnabled(true);
            else
                register.setEnabled(false);
        });

        register.setOnClickListener(view -> {
            if (emailExistsText.getVisibility() == View.GONE) {
                if (!name.getText().toString().equals("") || !email.getText().toString().equals("") || !password.getText().toString().equals("") || phoneNumber.getText().toString().length() == 10) {
                    progress.setVisibility(View.VISIBLE);
                    RegisterPost post = new RegisterPost(name.getText().toString(), email.getText().toString(), Long.parseLong(phoneNumber.getText().toString()), password.getText().toString(), Constants.CUSTOMER_STRING);
                    ApiService api = RetrofitClient.getApiService();
                    Call<RegisterSuccess> call = api.registerUser(post);
                    call.enqueue(new Callback<RegisterSuccess>() {
                        @Override
                        public void onResponse(Call<RegisterSuccess> call, Response<RegisterSuccess> response) {
                            RegisterSuccess customer = response.body();

                            System.out.println(response.message());

                            if (response.code() == 200) {
                                SharedPreferencesUtils.deleteLongFromSharedPrefs(RegisterActivity.this, Constants.TABLE_ID_CONST_STRING);
                                progress.setVisibility(View.GONE);
                                if (customer.getPerson() != null) {
                                    Toast.makeText(RegisterActivity.this, getString(R.string.register_success_string), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, getString(R.string.problem_registering_customer_details_string), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progress.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, getString(R.string.problem_registering_customer_details_string), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterSuccess> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    if (name.getText().toString().equals("")) {
                        name.setError(getString(R.string.invalid_name_error_string));
                    }
                    if (email.getText().toString().equals("")) {
                        email.setError(getString(R.string.invalid_email_error_string));
                    }
                    if (password.getText().toString().equals("")) {
                        password.setError(getString(R.string.invalid_password_error_string));
                    }
                    if (phoneNumber.getText().length() < 10) {
                        phoneNumber.setError(getString(R.string.invalid_phone_number_error_string));
                    }
                }
            } else {
                email.setError(getString(R.string.email_exists_message_string));
            }
        }
        );
    }

    private boolean isValidUsername(CharSequence username)  {
        return (!TextUtils.isEmpty(username) && Patterns.EMAIL_ADDRESS.matcher(username).matches());
    }
}
