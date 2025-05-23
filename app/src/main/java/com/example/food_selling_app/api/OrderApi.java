package com.example.food_selling_app.api;

import com.example.food_selling_app.dto.request.OrderRequest;
import com.example.food_selling_app.dto.response.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

public interface OrderApi {
    @POST("api/orders")
    Call<ApiResponse<OrderRequest>> createOrder(@Body OrderRequest orderDTO);

}