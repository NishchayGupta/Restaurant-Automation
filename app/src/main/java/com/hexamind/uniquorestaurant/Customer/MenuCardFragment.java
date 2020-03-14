package com.hexamind.uniquorestaurant.Customer;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hexamind.uniquorestaurant.Data.ChefOrders;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.Data.FoodItem;
import com.hexamind.uniquorestaurant.Data.FoodItems;
import com.hexamind.uniquorestaurant.R;
import com.hexamind.uniquorestaurant.Retrofit.ApiService;
import com.hexamind.uniquorestaurant.Retrofit.RetrofitClient;
import com.hexamind.uniquorestaurant.Utils.Constants;
import com.hexamind.uniquorestaurant.Utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuCardFragment extends Fragment {

    private RecyclerView recyclerView;
    private View root;
    private MenuCardAdapter adapter;
    private ArrayList<FoodItems> foodItemList = new ArrayList<>();
    private ProgressBar progress;
    private CustomerSuccess customer;
    private static boolean tableExistsAlready = false;
    private static Long tableId;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_menu_card, container, false);

        progress = root.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        customer = SharedPreferencesUtils.getCustomerFromSharedPrefs(root.getContext(), Constants.CUSTOMER_OBJ_NAME);
        ApiService api = RetrofitClient.getApiService();
        Call<ChefOrders> callTableExists = api.getCustomerTableExists(customer.getPerson().getCustomer().getCustomerId());
        callTableExists.enqueue(new Callback<ChefOrders>() {
            @Override
            public void onResponse(Call<ChefOrders> call, Response<ChefOrders> response) {
                ChefOrders order = response.body();

                if (response.code() == 200) {
                    tableExistsAlready = order.getExistingOrder();
                    if (SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(root.getContext(), Constants.TABLE_ID_MAP_CONST_STRING) != null) {
                        if (SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(root.getContext(), Constants.TABLE_ID_MAP_CONST_STRING).get(customer.getPerson().getCustomer().getCustomerId()) != null) {
                            tableId = SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(root.getContext(), Constants.TABLE_ID_MAP_CONST_STRING).get(customer.getPerson().getCustomer().getCustomerId());
                            if (tableId != 12 && tableId != 0) {
                                if (tableExistsAlready == false)
                                    ((CustomerHomeActivity) getActivity()).viewBookingDialog();
                            } else
                                ((CustomerHomeActivity) getActivity()).viewBookingDialog();
                        } else
                            ((CustomerHomeActivity) getActivity()).viewBookingDialog();
                    } else
                        ((CustomerHomeActivity) getActivity()).viewBookingDialog();
                } else {
                    //Toast.makeText(root.getContext(), getString(R.string.customer_table_exist_error_message_string), Toast.LENGTH_SHORT).show();
                    tableExistsAlready = false;
                    if (SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(root.getContext(), Constants.TABLE_ID_MAP_CONST_STRING) != null) {
                        if (SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(root.getContext(), Constants.TABLE_ID_MAP_CONST_STRING).get(customer.getPerson().getCustomer().getCustomerId()) != null) {
                            tableId = SharedPreferencesUtils.getTableIdByCustomerFromSharedPrefs(root.getContext(), Constants.TABLE_ID_MAP_CONST_STRING).get(customer.getPerson().getCustomer().getCustomerId());
                            if (tableId == 12 || tableId == 0)
                                //((CustomerHomeActivity) getActivity()).viewBookingDialog();
                            //else
                                ((CustomerHomeActivity) getActivity()).viewBookingDialog();
                        } else
                            ((CustomerHomeActivity) getActivity()).viewBookingDialog();
                    } else
                    ((CustomerHomeActivity) getActivity()).viewBookingDialog();

                    SharedPreferencesUtils.saveBooleanToSharedPrefs(root.getContext(), Constants.TABLE_EXISTS_ALREADY_STRING, tableExistsAlready);
                }
            }

            @Override
            public void onFailure(Call<ChefOrders> call, Throwable t) {
                Toast.makeText(root.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                tableExistsAlready = false;
            }
        });
        Call<List<FoodItems>> call = api.getAllFoodItems();

        call.enqueue(new Callback<List<FoodItems>>() {
            @Override
            public void onResponse(Call<List<FoodItems>> call, Response<List<FoodItems>> response) {
                progress.setVisibility(View.GONE);

                if (response.code() == 200) {
                    foodItemList = (ArrayList<FoodItems>) response.body();

                    recyclerView = root.findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    adapter = new MenuCardAdapter(root.getContext(), foodItemList);
                    recyclerView.setAdapter(adapter);
                } else if (response.code() == 400 || response.code() == 401 || response.code() == 404) {
                    Toast.makeText(root.getContext(), getString(R.string.four_error_message_string), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 500) {
                    Toast.makeText(root.getContext(), getString(R.string.five_error_message_string), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(root.getContext(), getString(R.string.general_error_message_string), Toast.LENGTH_SHORT).show();
                }
             }

            @Override
            public void onFailure(Call<List<FoodItems>> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(root.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private boolean getCustomerTableAlreadyExists() {
        ApiService api = RetrofitClient.getApiService();
        Call<ChefOrders> callTableExists = api.getCustomerTableExists(customer.getPerson().getCustomer().getCustomerId());
        callTableExists.enqueue(new Callback<ChefOrders>() {
            @Override
            public void onResponse(Call<ChefOrders> call, Response<ChefOrders> response) {
                ChefOrders order = response.body();

                if (response.code() == 200) {
                    tableExistsAlready = order.getExistingOrder();
                } else {
                    //Toast.makeText(root.getContext(), getString(R.string.customer_table_exist_error_message_string), Toast.LENGTH_SHORT).show();
                    tableExistsAlready = false;

                    SharedPreferencesUtils.saveBooleanToSharedPrefs(root.getContext(), Constants.TABLE_EXISTS_ALREADY_STRING, tableExistsAlready);
                }
            }

            @Override
            public void onFailure(Call<ChefOrders> call, Throwable t) {
                Toast.makeText(root.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                tableExistsAlready = false;
            }
        });

        return tableExistsAlready;
    }
}
