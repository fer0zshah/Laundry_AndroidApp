package com.example.laundryapp;

public class FeedbackHelper {
    public String id;
    public String userName;
    public String userPhone;
    public String message;
    public String adminReply;

    public FeedbackHelper() { }

    public FeedbackHelper(String id, String userName, String userPhone, String message) {
        this.id = id;
        this.userName = userName;
        this.userPhone = userPhone;
        this.message = message;
        this.adminReply = "";
    }
}