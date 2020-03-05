package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChefOrders {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("totalCost")
    @Expose
    private Double totalCost;
    @SerializedName("table")
    @Expose
    private Table table;
    @SerializedName("orderPrepared")
    @Expose
    private Boolean orderPrepared;
    @SerializedName("foodItemOrder")
    @Expose
    private List<CartFoodItems> foodItemOrder = null;
    @SerializedName("existingOrder")
    @Expose
    private Boolean existingOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Boolean getOrderPrepared() {
        return orderPrepared;
    }

    public void setOrderPrepared(Boolean orderPrepared) {
        this.orderPrepared = orderPrepared;
    }

    public List<CartFoodItems> getFoodItemOrder() {
        return foodItemOrder;
    }

    public void setFoodItemOrder(List<CartFoodItems> foodItemOrder) {
        this.foodItemOrder = foodItemOrder;
    }

    public Boolean getExistingOrder() {
        return existingOrder;
    }

    public void setExistingOrder(Boolean existingOrder) {
        this.existingOrder = existingOrder;
    }
}
