
package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;

public class CheckAvailabilitySuccess {
    @Expose
    private Long tableNumber;
    @Expose
    private Long waitingTimeInMinutes;

    public Long getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Long tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Long getWaitingTimeInMinutes() {
        return waitingTimeInMinutes;
    }

    public void setWaitingTimeInMinutes(Long waitingTimeInMinutes) {
        this.waitingTimeInMinutes = waitingTimeInMinutes;
    }

}
