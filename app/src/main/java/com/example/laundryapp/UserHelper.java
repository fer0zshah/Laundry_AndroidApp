package com.example.laundryapp;

public class UserHelper {
    public String name;
    public String phone;
    public String address; // <--- ADDED THIS
    public String password;
    public String role;

    public UserHelper() {
    }

    public UserHelper(String name, String phone, String address, String password, String role) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.role = role;
    }
}