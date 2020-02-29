package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodItems {
    @SerializedName("foodItemName")
    @Expose
    private String foodItemName;
    @SerializedName("foodItemPrice")
    @Expose
    private Double foodItemPrice;
    @SerializedName("foodItemPicture")
    @Expose
    private String foodItemPicture;
    @SerializedName("foodItemOrders")
    @Expose
    private List<Object> foodItemOrders = null;
    @SerializedName("id")
    @Expose
    private Integer foodItemId;

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

    public List<Object> getFoodItemOrders() {
        return foodItemOrders;
    }

    public void setFoodItemOrders(List<Object> foodItemOrders) {
        this.foodItemOrders = foodItemOrders;
    }

    public Integer getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Integer foodItemId) {
        this.foodItemId = foodItemId;
    }
}
