package com.example.food_selling_app.api;

import com.example.food_selling_app.dto.request.AuthRequest;
import com.example.food_selling_app.dto.response.AuthResponse;
import com.example.food_selling_app.dto.request.LogoutRequest;
import com.example.food_selling_app.dto.request.RefreshTokenRequest;
import com.example.food_selling_app.dto.request.RegisterRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("/auth/refresh")
    Call<AuthResponse> refresh(@Body RefreshTokenRequest request);

    @POST("/auth/register")
    Call<Void> register(@Body RegisterRequest request);

    @POST("auth/logout")
    Call<ResponseBody> logout(@Body LogoutRequest request);
}
