
package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderSuccess {

    @Expose
    @SerializedName("id")
    private Long id;
    @Expose
    @SerializedName("totalCost")
    private Double totalCost;
    @Expose
    @SerializedName("table")
    private Table table;
    @Expose
    @SerializedName("orderPrepared")
    private boolean orderPrepared;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isOrderPrepared() {
        return orderPrepared;
    }

    public void setOrderPrepared(boolean orderPrepared) {
        this.orderPrepared = orderPrepared;
    }
}
