
package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class Table {
    @Expose
    private Date bookingDateTime;
    @Expose
    private Object customers;
    @Expose
    private Date endDateTime;
    @Expose
    private Long id;
    @Expose
    private Object orderFood;
    @Expose
    private Date startDateTime;
    @Expose
    private Long waitingTime;

    public Date getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(Date bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public Object getCustomers() {
        return customers;
    }

    public void setCustomers(Object customers) {
        this.customers = customers;
    }

    public Object getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getOrderFood() {
        return orderFood;
    }

    public void setOrderFood(Object orderFood) {
        this.orderFood = orderFood;
    }

    public Object getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Long getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Long waitingTime) {
        this.waitingTime = waitingTime;
    }

}
