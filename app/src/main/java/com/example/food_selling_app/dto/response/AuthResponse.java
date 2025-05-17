package com.example.food_selling_app.dto.response;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("refreshToken")
    private String refreshToken;

    public String getAccessToken() { return token; }
    public String getRefreshToken() { return refreshToken; }
}