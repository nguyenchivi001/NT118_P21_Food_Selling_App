package com.example.food_selling_app.api;

import com.example.food_selling_app.dto.ApiResponse;
import com.example.food_selling_app.model.Category;
import com.example.food_selling_app.model.Food;
import com.example.food_selling_app.dto.FoodResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface FoodApi {
    @GET("api/foods")
    Call<ApiResponse<FoodResponse>> getAllFoods(
            @Query("page") int page,
            @Query("size") int size,
            @Query("sortBy") String sortBy,
            @Query("direction") String direction
    );

    @GET("api/foods/{id}")
    Call<ApiResponse<Food>> getFoodById(@Path("id") int id);

    @GET("api/foods/search")
    Call<ApiResponse<FoodResponse>> searchFoods(
            @Query("name") String name,
            @Query("categoryId") Integer categoryId,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("api/categories")
    Call<ApiResponse<List<Category>>> getAllCategories();
}