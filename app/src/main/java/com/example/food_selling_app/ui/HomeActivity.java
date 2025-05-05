package com.example.food_selling_app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.FoodAdapter;
import com.example.food_selling_app.api.FoodApi;
import com.example.food_selling_app.model.ApiResponse;
import com.example.food_selling_app.model.Food;
import com.example.food_selling_app.model.FoodResponse;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    RecyclerView rvFoodList;
    FoodAdapter adapter;
    List<Food> foodList;
    FoodApi foodApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Retrofit with token
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String token = prefs.getString("access_token", "");
        Log.d("HomeActivity", "Retrieved token: " + token);
        if (token.isEmpty()) {
            Log.w("HomeActivity", "No token found in SharedPreferences");
            Toast.makeText(this, "Vui lòng đăng nhập trước", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
            return;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        foodApi = retrofit.create(FoodApi.class);

        // Initialize RecyclerView
        rvFoodList = findViewById(R.id.rvFoodList);
        rvFoodList.setLayoutManager(new GridLayoutManager(this, 2));

        foodList = new ArrayList<>();
        adapter = new FoodAdapter(this, foodList);
        rvFoodList.setAdapter(adapter);

        // Fetch food data from backend
        fetchFoods();

        // Bottom navigation setup
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        bottomAppBar.replaceMenu(R.menu.bottom_nav_menu);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_message) {
                startActivity(new Intent(HomeActivity.this, MessageActivity.class));
                return true;
            }else if (id == R.id.nav_cart) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                return true;
            }else if (id == R.id.nav_favorites) {
                startActivity(new Intent(HomeActivity.this, FavoritesActivity.class));
                return true;
            }
            return false;
        });
    }

    private void fetchFoods() {
        Call<ApiResponse<FoodResponse>> call = foodApi.getAllFoods(0, 10, "name", "asc");
        call.enqueue(new Callback<ApiResponse<FoodResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<FoodResponse>> call, Response<ApiResponse<FoodResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().isSuccess()) {
                        FoodResponse foodResponse = response.body().getData();
                        if (foodResponse != null && foodResponse.getFoods() != null) {
                            List<Food> foods = foodResponse.getFoods();
                            foodList.clear();
                            foodList.addAll(foods);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("HomeActivity", "No foods found in response");
                            Toast.makeText(HomeActivity.this, "Không có món ăn nào", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String errorMsg = "Phản hồi API không thành công: body=" + (response.body() != null ? response.body().toString() : "null") + ", code=" + response.code();
                        Log.e("HomeActivity", errorMsg);
                        Toast.makeText(HomeActivity.this, "Không thể tải danh sách món ăn: Phản hồi không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMsg = "Yêu cầu API thất bại: code=" + response.code() + ", message=" + response.message();
                    Log.e("HomeActivity", errorMsg);
                    Toast.makeText(HomeActivity.this, "Không thể tải danh sách món ăn: HTTP " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<FoodResponse>> call, Throwable t) {
                Log.e("HomeActivity", "Lỗi khi tải danh sách món ăn: " + t.getMessage(), t);
                Toast.makeText(HomeActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}