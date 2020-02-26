package com.hexamind.uniquorestaurant.Data;

public class Cart {
    private int foodItemId;
    private int quantity;

    public Cart(int foodItemId, int quantity) {
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
