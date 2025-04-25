package com.example.food_selling_app.api;

import com.example.food_selling_app.model.User;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApi {
    @GET("/user/me")
    Call<User> getUserProfile();
}