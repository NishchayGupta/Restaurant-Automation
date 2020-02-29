package com.hexamind.uniquorestaurant.Customer;

import com.hexamind.uniquorestaurant.Data.CartFoodItems;

import java.util.List;

public interface OnTotalComputedListener {
    void onTotalComputed(Double total);
    void saveFoodItems(String objectName, List<CartFoodItems> foodItems);
}
