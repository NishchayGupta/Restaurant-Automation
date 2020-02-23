package com.hexamind.uniquorestaurant.Data;

import java.util.List;

public class Order {
    private int orderID;
    private Double totalCost;
    private String orderType;
    private String orderDate;
    private List<FoodItem> foodItems;
    private Customer customer;
    private Table table;

    public Order(int orderID, Double totalCost, String orderType, String orderDate, List<FoodItem> foodItems, Customer customer, Table table) {
        this.orderID = orderID;
        this.totalCost = totalCost;
        this.orderType = orderType;
        this.orderDate = orderDate;
        this.foodItems = foodItems;
        this.customer = customer;
        this.table = table;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
