
package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;

public class BookTableSuccess {
    @Expose
    private Long customerId;
    @Expose
    private Object orderFood;
    @Expose
    private Table tableRestaurants;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Object getOrderFood() {
        return orderFood;
    }

    public void setOrderFood(Object orderFood) {
        this.orderFood = orderFood;
    }

    public Table getTableRestaurants() {
        return tableRestaurants;
    }

    public void setTableRestaurants(Table tableRestaurants) {
        this.tableRestaurants = tableRestaurants;
    }

}
