package com.hexamind.uniquorestaurant.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hexamind.uniquorestaurant.Data.CustomerSuccess;

import static com.hexamind.uniquorestaurant.Utils.Utils.SHARED_PREFS_NAME;

public class SharedPreferencesUtils {
    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void saveStringToSharedPrefs(Context context, String name, String value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String getStringFromSharedPrefs(Context context, String name) {
        return getSharedPreference(context).getString(name, "");
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
