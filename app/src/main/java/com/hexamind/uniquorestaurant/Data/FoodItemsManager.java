package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodItemsManager {
    @SerializedName("foodItemName")
    @Expose
    private String foodItemName;
    @SerializedName("foodItemPrice")
    @Expose
    private Double foodItemPrice;
    @SerializedName("foodItemPicture")
    @Expose
    private String foodItemPicture;

    public FoodItemsManager(String foodItemName, Double foodItemPrice, String foodItemPicture) {
        this.foodItemName = foodItemName;
        this.foodItemPrice = foodItemPrice;
        this.foodItemPicture = foodItemPicture;
    }

    public String getFoodItemName() {
        return foodItemName;
    }

    public void setFoodItemName(String foodItemName) {
        this.foodItemName = foodItemName;
    }

    public Double getFoodItemPrice() {
        return foodItemPrice;
    }

    public void setFoodItemPrice(Double foodItemPrice) {
        this.foodItemPrice = foodItemPrice;
    }

    public String getFoodItemPicture() {
        return foodItemPicture;
    }

    public void setFoodItemPicture(String foodItemPicture) {
        this.foodItemPicture = foodItemPicture;
    }
}
