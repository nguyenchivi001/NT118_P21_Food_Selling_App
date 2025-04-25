package com.example.food_selling_app.dto;

public class RefreshTokenRequest {
    private String refreshToken;

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() { return refreshToken; }
}
