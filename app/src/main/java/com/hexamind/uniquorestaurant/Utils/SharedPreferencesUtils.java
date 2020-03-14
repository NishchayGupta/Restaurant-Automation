package com.hexamind.uniquorestaurant.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hexamind.uniquorestaurant.Data.CartFoodItems;
import com.hexamind.uniquorestaurant.Data.ChefOrders;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;
import com.hexamind.uniquorestaurant.Data.Manager;
import com.hexamind.uniquorestaurant.Data.UserOrder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;

import static com.hexamind.uniquorestaurant.Utils.Constants.SHARED_PREFS_NAME;

public class SharedPreferencesUtils {
    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void saveBooleanToSharedPrefs(Context context, String name, Boolean value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public static Boolean getBooleanFromSharedPrefs(Context context, String name) {
        return getSharedPreference(context).getBoolean(name, false);
    }

    public static void saveLongToSharedPrefs(Context context, String name, Long value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putLong(name, value);
        editor.apply();
    }

    public static Long getLongFromSharedPrefs(Context context, String name) {
        return getSharedPreference(context).getLong(name, 0);
    }

    public static void deleteLongFromSharedPrefs(Context context, String name) {
        getSharedPreference(context).edit().remove(name).apply();
    }

    public static void deleteBooleanFromSharedPrefs(Context context, String name) {
        getSharedPreference(context).edit().remove(name).apply();
    }

    public static void saveStringToSharedPrefs(Context context, String name, String value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String getStringFromSharedPrefs(Context context, String name) {
        return getSharedPreference(context).getString(name, "");
    }

    public static void saveFoodItemsToSharedPrefs(Context context, String objectName, List<CartFoodItems> foodItemList) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        Gson gson = new Gson();
        String object = gson.toJson(foodItemList);
        editor.putString(objectName, object);
        editor.apply();
    }

    public static List<CartFoodItems> getFoodItemsFromSharedPrefs(Context context, String objectName) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartFoodItems>>(){}.getType();

        String object = getSharedPreference(context).getString(objectName, "");

        return gson.fromJson(object, type);
    }

    public static void saveTableIdByCustomerToSharedPrefs(Context context, String objectName, Map<Long, Long> tableIdMap) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        Gson gson = new Gson();
        String object = gson.toJson(tableIdMap);
        editor.putString(objectName, object);
        editor.apply();
    }

    public static HashMap<Long, Long> getTableIdByCustomerFromSharedPrefs(Context context, String objectName) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<Long, Long>>(){}.getType();

        String object = getSharedPreference(context).getString(objectName, "");

        return gson.fromJson(object, type);
    }

    public static void removeTableId(Context context, String name) {
        getSharedPreference(context).edit().remove(name).apply();
    }

    public static void saveFoodItemsByCustomerToSharedPrefs(Context context, String objectName, Map<Long, List<CartFoodItems>> foodItemsMap) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        Gson gson = new Gson();
        String object = gson.toJson(foodItemsMap);
        editor.putString(objectName, object);
        editor.apply();
    }

    public static HashMap<Long, List<CartFoodItems>> getFoodItemsByCustomerFromSharedPrefs(Context context, String objectName) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<Long, List<CartFoodItems>>>(){}.getType();

        String object = getSharedPreference(context).getString(objectName, "");

        return gson.fromJson(object, type);
    }

    public static void removeCartItems(Context context, String name) {
        getSharedPreference(context).edit().remove(name).apply();
    }

    public static void saveUserOrdersToSharedPrefs(Context context, String objectName, List<UserOrder> userOrderList) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        Gson gson = new Gson();
        String object = gson.toJson(userOrderList);
        editor.putString(objectName, object);
        editor.apply();
    }

    public static Map<Long, CartFoodItems> getUserOrdersFromSharedPrefs(Context context, String objectName) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<UserOrder>>(){}.getType();

        String object = getSharedPreference(context).getString(objectName, "");

        return gson.fromJson(object, type);
    }

    public static void saveCustomerToSharedPrefs(Context context, String objectName, CustomerSuccess objectValue) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        Gson gson = new Gson();
        String object = gson.toJson(objectValue);
        editor.putString(objectName, object);
        editor.apply();
    }

    public static CustomerSuccess getCustomerFromSharedPrefs(Context context, String objectName) {
        Gson gson = new Gson();
        String object = getSharedPreference(context).getString(objectName, "");

        return gson.fromJson(object, CustomerSuccess.class);
    }
}
