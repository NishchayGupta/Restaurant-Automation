
package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;

public class GeneralError {
    @Expose
    private String details;
    @Expose
    private String message;
    @Expose
    private String status;
    @Expose
    private String timestamp;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
