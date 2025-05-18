package com.example.food_selling_app.api;

import com.example.food_selling_app.dto.response.ApiResponse;
import com.example.food_selling_app.model.FavoriteItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FavoriteApi {

    @GET("api/favorite-items")
    Call<ApiResponse<List<FavoriteItem>>> getFavoriteItems();

    @DELETE("api/favorite-items/{foodId}")
    Call<ApiResponse<Void>> removeFromFavorite(@Path("foodId") Integer foodId);
}
