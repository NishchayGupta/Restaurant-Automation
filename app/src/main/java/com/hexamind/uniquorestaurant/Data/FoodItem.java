
package com.hexamind.uniquorestaurant.Data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoodItem {
    public FoodItem(List<FoodItems> foodItems) {
        this.foodItems = foodItems;
    }

    @SerializedName("Food Items")
    @Expose
    private List<FoodItems> foodItems = null;

    public List<FoodItems> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(ArrayList<FoodItems> foodItems) {
        this.foodItems = foodItems;
    }

}
