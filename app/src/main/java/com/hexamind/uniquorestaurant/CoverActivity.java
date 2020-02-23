package com.hexamind.uniquorestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.jgabrielfreitas.core.BlurImageView;

public class CoverActivity extends AppCompatActivity {
    private BlurImageView imageView;
    private MaterialButton login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);

        imageView = findViewById(R.id.imageView);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        imageView.setBlur(2);

        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Network not accessible at the moment. Please try again later...", Toast.LENGTH_SHORT).show();
            System.exit(0);
        }

        login.setOnClickListener(view -> startActivity(new Intent(CoverActivity.this, LoginActivity.class)));

        register.setOnClickListener(view -> startActivity(new Intent(CoverActivity.this, RegisterActivity.class)));
    }

    private boolean isNetworkAvailable(Context context) {
        boolean networkAvailable = false;

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = null;
        if (connManager != null) {
            capabilities = connManager.getNetworkCapabilities(connManager.getActiveNetwork());
        }
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                networkAvailable = true;
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                networkAvailable = true;
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                networkAvailable = true;
        }

        return networkAvailable;
    }
}
