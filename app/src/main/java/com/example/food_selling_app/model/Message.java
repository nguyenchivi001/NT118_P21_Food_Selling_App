package com.example.food_selling_app.model;

public class Message {
    private String text;
    private boolean sentByUser;

    public Message(String text, boolean sentByUser) {
        this.text = text;
        this.sentByUser = sentByUser;
    }

    public String getText() {
        return text;
    }

    public boolean isSentByUser() {
        return sentByUser;
    }
}
