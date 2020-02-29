package com.hexamind.uniquorestaurant.Data;

import java.util.List;
import com.google.gson.annotations.Expose;

public class Order {
    @Expose
    private Long customerId;
    @Expose
    private List<OrderCart> orderCart;
    @Expose
    private Long tableId;

    public Order(Long customerId, List<OrderCart> orderCart, Long tableId) {
        this.customerId = customerId;
        this.orderCart = orderCart;
        this.tableId = tableId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderCart> getOrderCart() {
        return orderCart;
    }

    public void setOrderCart(List<OrderCart> orderCart) {
        this.orderCart = orderCart;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

}
