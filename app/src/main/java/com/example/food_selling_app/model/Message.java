package com.example.food_selling_app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Message  implements Serializable {
    @SerializedName("role")
    private String role;
    @SerializedName("content")
    private String content;


    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public boolean isSentByUser() {
        return "user".equalsIgnoreCase(role);
    }
}