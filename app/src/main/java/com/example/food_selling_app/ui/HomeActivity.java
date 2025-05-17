package com.example.food_selling_app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.FoodAdapter;
import com.example.food_selling_app.api.ApiClient;
import com.example.food_selling_app.api.FoodApi;
import com.example.food_selling_app.dto.response.ApiResponse;
import com.example.food_selling_app.dto.response.FoodResponse;
import com.example.food_selling_app.model.Category;
import com.example.food_selling_app.model.Food;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvFoodList;
    private TextView tvNoProducts;
    private FoodAdapter adapter;
    private List<Food> foodList = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private FoodApi foodApi;
    private LinearLayout tabContainer;
    private int selectedCategoryId = -1; // -1: All
    private EditText edtSearch;

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

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String token = prefs.getString("access_token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Vui lòng đăng nhập trước", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Retrofit retrofit = ApiClient.getClient(token);
        foodApi = retrofit.create(FoodApi.class);

        rvFoodList = findViewById(R.id.rvFoodList);
        tvNoProducts = findViewById(R.id.tvNoProducts);
        rvFoodList.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new FoodAdapter(this, foodList);
        rvFoodList.setAdapter(adapter);

        tabContainer = findViewById(R.id.tabContainer);
        edtSearch = findViewById(R.id.edtSearch);

        setupBottomNavigation();
        setupSearch();
        loadCategories();
        loadFoodsByCategory(selectedCategoryId, "");
    }

    private void setupBottomNavigation() {
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        bottomAppBar.replaceMenu(R.menu.bottom_nav_menu);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_message) {
                startActivity(new Intent(this, MessageActivity.class));
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            } else if (id == R.id.nav_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim();
                String nonAccentKeyword = removeAccents(keyword);
                loadFoodsByCategory(selectedCategoryId, nonAccentKeyword);
            }
        });
    }

    private void loadCategories() {
        foodApi.getAllCategories().enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    categories.clear();
                    categories.addAll(response.body().getData());
                    createCategoryTabs();
                    // Nếu không có danh mục, hiển thị thông báo
                    if (categories.isEmpty()) {
                        rvFoodList.setVisibility(View.GONE);
                        tvNoProducts.setVisibility(View.VISIBLE);
                    }
                } else {
                    rvFoodList.setVisibility(View.GONE);
                    tvNoProducts.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Lỗi tải loại món: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                rvFoodList.setVisibility(View.GONE);
                tvNoProducts.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadFoodsByCategory(int categoryId, String keyword) {
        Integer apiCategoryId = (categoryId == -1) ? null : categoryId;

        foodApi.searchFoods(keyword, apiCategoryId, 0, 10).enqueue(new Callback<ApiResponse<FoodResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<FoodResponse>> call, Response<ApiResponse<FoodResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    foodList.clear();
                    List<Food> foods = response.body().getData().getFoods();
                    foodList.addAll(foods != null ? foods : new ArrayList<>());
                    adapter.notifyDataSetChanged();
                    // Kiểm tra danh sách món ăn
                    if (foodList.isEmpty()) {
                        rvFoodList.setVisibility(View.GONE);
                        tvNoProducts.setVisibility(View.VISIBLE);
                    } else {
                        rvFoodList.setVisibility(View.VISIBLE);
                        tvNoProducts.setVisibility(View.GONE);
                    }
                } else {
                    rvFoodList.setVisibility(View.GONE);
                    tvNoProducts.setVisibility(View.VISIBLE);
                    Log.e("HomeActivity", "Response không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<FoodResponse>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeActivity", "Lỗi", t);
                rvFoodList.setVisibility(View.GONE);
                tvNoProducts.setVisibility(View.VISIBLE);
            }
        });
    }

    private void createCategoryTabs() {
        tabContainer.removeAllViews();
        addCategoryTab(-1, "Tất cả");

        for (Category category : categories) {
            addCategoryTab(category.getId(), category.getName());
        }
    }

    private void addCategoryTab(int categoryId, String name) {
        AppCompatButton button = new AppCompatButton(this);
        button.setText(name);
        button.setTextSize(14);
        button.setTextColor(categoryId == selectedCategoryId ? Color.WHITE : Color.GRAY);
        button.setBackgroundResource(categoryId == selectedCategoryId ? R.drawable.tab_selected : R.drawable.tab_unselected);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMarginEnd(16);
        button.setLayoutParams(params);

        button.setOnClickListener(v -> {
            selectedCategoryId = categoryId;
            createCategoryTabs();
            loadFoodsByCategory(categoryId, removeAccents(edtSearch.getText().toString().trim()));
        });

        tabContainer.addView(button);
    }

    private String removeAccents(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }
}