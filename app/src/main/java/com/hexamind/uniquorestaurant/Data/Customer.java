
package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;

public class Customer {

    @Expose
    private Long customerId;
    @Expose
    private Table tableRestaurant;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Table getTableRestaurant() {
        return tableRestaurant;
    }

    public void setTableRestaurant(Table tableRestaurant) {
        this.tableRestaurant = tableRestaurant;
    }

}
