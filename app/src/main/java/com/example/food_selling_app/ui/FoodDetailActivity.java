package com.example.food_selling_app.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.food_selling_app.R;
import com.example.food_selling_app.api.ApiClient;
import com.example.food_selling_app.api.CartApi;
import com.example.food_selling_app.api.FavoriteApi;
import com.example.food_selling_app.api.FoodApi;
import com.example.food_selling_app.dto.request.CartItemRequest;
import com.example.food_selling_app.dto.request.FavoriteItemRequest;
import com.example.food_selling_app.dto.response.ApiResponse;
import com.example.food_selling_app.model.CartItem;
import com.example.food_selling_app.model.FavoriteItem;
import com.example.food_selling_app.model.Food;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FoodDetailActivity extends AppCompatActivity {

    private ImageView imgFood, btnBack;
    private TextView tvName, tvDescription, tvPrice, tvQuantity;
    private ImageButton btnDecrease, btnIncrease;
    private Button btnBuy;
    private ImageButton btnFavorite;

    private int foodId;
    private int quantity = 1;
    private Double unitPrice = null;

    private FoodApi foodApi;
    private CartApi cartApi;
    private FavoriteApi favoriteApi;

    private String baseImageUrl = "http://10.0.2.2:8080/api/foods/images/";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooddetail);

        // Ánh xạ view
        imgFood = findViewById(R.id.imgFood);
        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnDecrease = findViewById(R.id.btnMinus);
        btnIncrease = findViewById(R.id.btnPlus);
        btnBuy = findViewById(R.id.btnBuy);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnBack = findViewById(R.id.btnBack);

        foodId = getIntent().getIntExtra("foodId", -1);
        if (foodId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy món ăn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String token = getAccessToken();
        Retrofit retrofit = ApiClient.getClient(token);
        foodApi = retrofit.create(FoodApi.class);
        cartApi = retrofit.create(CartApi.class);
        favoriteApi = retrofit.create(FavoriteApi.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        loadFoodDetail(foodId);

        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityDisplay();
            }
        });

        btnIncrease.setOnClickListener(v -> {
            quantity++;
            updateQuantityDisplay();
        });

        btnBuy.setOnClickListener(v -> {
            if (unitPrice == null) {
                Toast.makeText(FoodDetailActivity.this, "Vui lòng đợi tải món ăn trước khi thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            CartItemRequest request = new CartItemRequest(foodId, quantity);
            cartApi.addToCart(request).enqueue(new Callback<ApiResponse<CartItem>>() {
                @Override
                public void onResponse(Call<ApiResponse<CartItem>> call, Response<ApiResponse<CartItem>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toast.makeText(FoodDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FoodDetailActivity.this, "Không thể thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<CartItem>> call, Throwable t) {
                    Toast.makeText(FoodDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnFavorite.setOnClickListener(v -> {
            if (unitPrice == null) {
                Toast.makeText(FoodDetailActivity.this, "Vui lòng đợi tải món ăn trước khi thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                return;
            }
            FavoriteItemRequest request = new FavoriteItemRequest(foodId);
            favoriteApi.addToFavorites(request).enqueue(new Callback<ApiResponse<FavoriteItem>>() {
                @Override
                public void onResponse(Call<ApiResponse<FavoriteItem>> call, Response<ApiResponse<FavoriteItem>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toast.makeText(FoodDetailActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FoodDetailActivity.this, "Không thể thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<FavoriteItem>> call, Throwable t) {
                    Toast.makeText(FoodDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadFoodDetail(int id) {
        foodApi.getFoodById(id).enqueue(new Callback<ApiResponse<Food>>() {
            @Override
            public void onResponse(Call<ApiResponse<Food>> call, Response<ApiResponse<Food>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Food food = response.body().getData();
                    if (food != null) {
                        displayFoodDetails(food);
                    } else {
                        Toast.makeText(FoodDetailActivity.this, "Không tìm thấy dữ liệu món ăn", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(FoodDetailActivity.this, "Không thể tải chi tiết món ăn", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Food>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(FoodDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayFoodDetails(Food food) {
        tvName.setText(food.getName());
        tvDescription.setText(food.getDescription());
        unitPrice = food.getPrice();
        quantity = 1;
        updateQuantityDisplay();

        String imageUrl = baseImageUrl + food.getImageFilename();
        Glide.with(this)
                .load(getGlideUrlWithToken(imageUrl))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imgFood);
    }

    private void updateQuantityDisplay() {
        tvQuantity.setText(String.valueOf(quantity));
        if (unitPrice != null) {
            double totalPrice = unitPrice * quantity;
            tvPrice.setText(String.format(Locale.getDefault(), "%,.0fđ", totalPrice));
        }
    }

    private GlideUrl getGlideUrlWithToken(String url) {
        String token = getAccessToken();
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + token)
                .build());
    }

    private String getAccessToken() {
        return getSharedPreferences("prefs", MODE_PRIVATE).getString("access_token", "");
    }
}
