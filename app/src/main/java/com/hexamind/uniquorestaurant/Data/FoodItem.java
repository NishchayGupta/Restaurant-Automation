package com.hexamind.uniquorestaurant.Data;

public class FoodItem {
    private int foodItemID;
    private String foodItemName;
    private String foodItemDesc;
    private Double foodItemPrice;
    private String foodItemPicture;

    public FoodItem(int foodItemID, String foodItemName, String foodItemDesc, Double foodItemPrice, String foodItemPicture) {
        this.foodItemID = foodItemID;
        this.foodItemName = foodItemName;
        this.foodItemDesc = foodItemDesc;
        this.foodItemPrice = foodItemPrice;
        this.foodItemPicture = foodItemPicture;
    }

    public int getFoodItemID() {
        return foodItemID;
    }

    public void setFoodItemID(int foodItemID) {
        this.foodItemID = foodItemID;
    }

    public String getFoodItemName() {
        return foodItemName;
    }

    public void setFoodItemName(String foodItemName) {
        this.foodItemName = foodItemName;
    }

    public String getFoodItemDesc() {
        return foodItemDesc;
    }

    public void setFoodItemDesc(String foodItemDesc) {
        this.foodItemDesc = foodItemDesc;
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
