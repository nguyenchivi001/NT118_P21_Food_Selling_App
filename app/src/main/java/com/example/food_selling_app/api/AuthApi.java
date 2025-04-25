package com.example.food_selling_app.api;

import com.example.food_selling_app.dto.AuthRequest;
import com.example.food_selling_app.dto.AuthResponse;
import com.example.food_selling_app.dto.RefreshTokenRequest;
import com.example.food_selling_app.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("/auth/register")
    Call<Void> register(@Body AuthRequest request);

    @POST("/auth/refresh")
    Call<AuthResponse> refresh(@Body RefreshTokenRequest request);
}
