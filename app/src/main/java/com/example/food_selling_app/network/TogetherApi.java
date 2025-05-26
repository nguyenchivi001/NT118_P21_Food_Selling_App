package com.example.food_selling_app.network;

import com.example.food_selling_app.model.ChatRequest;
import com.example.food_selling_app.model.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TogetherApi {
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 02931763d306a4b98bdec804e7f4afe3f91bb33c13154c307c8cdbe6a879e613"
    })
    @POST("inference")
    Call<ChatResponse> chatWithAI(@Body ChatRequest request);

}