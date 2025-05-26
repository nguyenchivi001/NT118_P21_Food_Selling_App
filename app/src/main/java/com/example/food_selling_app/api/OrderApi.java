package com.example.food_selling_app.api;

import com.example.food_selling_app.model.Order;
import com.example.food_selling_app.dto.response.ApiResponse;
import com.example.food_selling_app.dto.response.OrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderApi {
    @POST("api/orders")
    Call<ApiResponse<Order>> createOrder(@Body Order order);

    @GET("api/orders/search")
    Call<ApiResponse<OrderResponse>> searchOrders(
            @Query("userId") int userId,
            @Query("name") String name,
            @Query("status") String status,
            @Query("page") int page,
            @Query("size") int size
    );

    @PUT("api/orders/{id}/status")
    Call<ApiResponse<Order>> updateOrderStatus(
            @Path("id") int orderId,
            @Query("status") String status
    );
}