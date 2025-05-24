package com.example.food_selling_app.api;

import com.example.food_selling_app.dto.request.CartItemRequest;
import com.example.food_selling_app.dto.response.ApiResponse;
import com.example.food_selling_app.model.CartItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartApi {
    @GET("api/cart-items")
    Call<ApiResponse<List<CartItem>>> getCartItems();

    @POST("api/cart-items")
    Call<ApiResponse<CartItem>> addToCart(@Body CartItemRequest request);

    @PUT("api/cart-items/{foodId}")
    Call<ApiResponse<CartItem>> updateCartItemQuantity(@Path("foodId") Integer foodId, @Body CartItemRequest request);

    @DELETE("api/cart-items/{foodId}")
    Call<ApiResponse<Void>> removeFromCart(@Path("foodId") Integer foodId);

    @DELETE("api/cart-items")
    Call<ApiResponse<Void>> clearCart();
}