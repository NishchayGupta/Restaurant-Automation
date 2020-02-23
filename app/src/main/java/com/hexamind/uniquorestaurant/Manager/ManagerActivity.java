package com.hexamind.uniquorestaurant.Manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.hexamind.uniquorestaurant.Customer.CustomerHomeActivity;
import com.hexamind.uniquorestaurant.LoginActivity;
import com.hexamind.uniquorestaurant.R;

import java.util.Set;

public class ManagerActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private AppBarConfiguration  appbarConfig;
    private NavController navController;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        setSupportActionBar(findViewById(R.id.toolbar));
        drawerLayout = findViewById(R.id.drawer);
        navView = findViewById(R.id.navView);

        final Set<Integer> menuItems = new ArraySet<>();
        menuItems.add(R.id.menu_add_item);
        menuItems.add(R.id.menu_edit_item);
        menuItems.add(R.id.menu_delete_item);

        appbarConfig = new AppBarConfiguration.Builder(menuItems)
                .setDrawerLayout(drawerLayout)
                .build();

        navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appbarConfig);
        NavigationUI.setupWithNavController(navView, navController);

        MenuItem logout = navView.getMenu().findItem(R.id.menu_logout);
        logout.setOnMenuItemClickListener(menuItem -> {
            startActivity(new Intent(ManagerActivity.this, LoginActivity.class));
            Toast.makeText(ManagerActivity.this, getString(R.string.logout_success_message_string), Toast.LENGTH_SHORT).show();
            finish();
            return true;
        });
    }
}
