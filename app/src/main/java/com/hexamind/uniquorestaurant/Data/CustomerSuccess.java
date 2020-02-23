
package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;

public class CustomerSuccess {
    @Expose
    private String message;
    @Expose
    private Person person;
    @Expose
    private String status;
    @Expose
    private String timestamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
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
