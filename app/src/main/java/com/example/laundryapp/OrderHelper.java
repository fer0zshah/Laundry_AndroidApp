package com.example.laundryapp;

public class OrderHelper {
    public String orderId;
    public String name;
    public String phone;
    public String details;
    public String status;
    public String price;
    public String date;

    public OrderHelper() { }
    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return name;
    }

    public String getUserPhone() {
        return phone;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDetails() {
        return details;
    }

    public String getTotalPrice() {
        return price;
    }
    public String getDate() { return  date;}
    public OrderHelper(String orderId, String name, String phone, String details, String status, String price,String date) {
        this.orderId = orderId;
        this.name = name;
        this.phone = phone;
        this.details = details;
        this.status = status;
        this.price = price;
        this.date=date;
    }
}