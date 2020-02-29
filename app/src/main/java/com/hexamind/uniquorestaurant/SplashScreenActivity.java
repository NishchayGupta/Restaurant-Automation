package com.hexamind.uniquorestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jgabrielfreitas.core.BlurImageView;

public class SplashScreenActivity extends AppCompatActivity {
    private BlurImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        background = findViewById(R.id.background);
        background.setBlur(2);

        new Handler().postDelayed(() -> startActivity(new Intent(SplashScreenActivity.this, CoverActivity.class)), 1500);
    }
}
