package com.hexamind.uniquorestaurant.Manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.hexamind.uniquorestaurant.Customer.CustomerHomeActivity;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.LoginActivity;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Utils.Constants;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;

import java.util.Set;

public class ManagerActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private AppBarConfiguration  appbarConfig;
    private NavController navController;
    private NavigationView navView;
    private CustomerSuccess customer;

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

        customer = SharedPreferencesUtils.getCustomerFromSharedPrefs(this, Constants.CUSTOMER_OBJ_NAME);
        navController = Navigation.findNavController(this, R.id.managerNavHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appbarConfig);
        NavigationUI.setupWithNavController(navView, navController);
        View headView = navView.getHeaderView(0);
        TextView name = headView.findViewById(R.id.name);
        TextView email = headView.findViewById(R.id.email);
        name.setText(customer.getPerson().getName());
        email.setText(customer.getPerson().getEmail());

        MenuItem logout = navView.getMenu().findItem(R.id.menu_logout);
        logout.setOnMenuItemClickListener(menuItem -> {
            startActivity(new Intent(ManagerActivity.this, LoginActivity.class));
            Toast.makeText(ManagerActivity.this, getString(R.string.logout_success_message_string), Toast.LENGTH_SHORT).show();
            finish();
            return true;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appbarConfig) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(this, getString(R.string.back_press_diable_issue), Toast.LENGTH_SHORT).show();
        }
    }
}
