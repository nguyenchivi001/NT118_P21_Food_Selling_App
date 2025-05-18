package com.example.food_selling_app.api;

import com.example.food_selling_app.dto.request.FavoriteItemRequest;
import com.example.food_selling_app.dto.response.ApiResponse;
import com.example.food_selling_app.model.FavoriteItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoriteApi {

    @GET("api/favorite-items")
    Call<ApiResponse<List<FavoriteItem>>> getFavoriteItems();

    @POST("api/favorite-items")
    Call<ApiResponse<FavoriteItem>> addToFavorites(@Body FavoriteItemRequest request);

    @DELETE("api/favorite-items/{foodId}")
    Call<ApiResponse<Void>> removeFromFavorite(@Path("foodId") Integer foodId);
}
