package com.hexamind.uniquorestaurant.Data;

public class CartFoodItems {
    private FoodItems foodItem;
    private int quantity;

    public CartFoodItems(FoodItems foodItem, int quantity) {
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    public FoodItems getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItems foodItem) {
        this.foodItem = foodItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
