package com.example.food_selling_app.dto.request;

public class LogoutRequest {
    private String email;

    public LogoutRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
