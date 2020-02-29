package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;

public class OrderCart {

    @Expose
    private int foodItemId;
    @Expose
    private int quantity;

    public OrderCart(int foodItemId, int quantity) {
        this.foodItemId = foodItemId;
        this.quantity = quantity;
    }

    public int getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
