
package com.hexamind.uniquorestaurant.Data;

import com.google.gson.annotations.Expose;

public class Person {
    @Expose
    private Object cashier;
    @Expose
    private Object chef;
    @Expose
    private Customer customer;
    @Expose
    private String email;
    @Expose
    private Object manager;
    @Expose
    private String name;
    @Expose
    private String password;
    @Expose
    private Long personId;
    @Expose
    private Long phoneNumber;
    @Expose
    private String userType;

    public Object getCashier() {
        return cashier;
    }

    public void setCashier(Object cashier) {
        this.cashier = cashier;
    }

    public Object getChef() {
        return chef;
    }

    public void setChef(Object chef) {
        this.chef = chef;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getManager() {
        return manager;
    }

    public void setManager(Object manager) {
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
