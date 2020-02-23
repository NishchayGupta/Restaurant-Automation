
package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;

public class Customer {

    @Expose
    private Long customerId;
    @Expose
    private Table tableRestaurants;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Table getTableRestaurants() {
        return tableRestaurants;
    }

    public void setTableRestaurants(Table tableRestaurants) {
        this.tableRestaurants = tableRestaurants;
    }

}
