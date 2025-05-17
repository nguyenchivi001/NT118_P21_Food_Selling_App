package com.example.food_selling_app.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.food_selling_app.R;

import java.text.NumberFormat;
import java.util.Locale;

public class FoodDetailActivity extends AppCompatActivity {

    private ImageView imgFood, btnBack;
    private TextView tvName, tvDescription, tvPrice, tvQuantity;
    private ImageButton btnPlus, btnMinus, btnFavorite;
    private AppCompatButton btnBuy;

    private int quantity = 1;
    private int unitPrice = 15000;
    private final String baseImageUrl = "http://10.0.2.2:8080/api/foods/images/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooddetail);

        // Ánh xạ view
        imgFood = findViewById(R.id.imgFood);
        btnBack = findViewById(R.id.btnBack);
        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnBuy = findViewById(R.id.btnBuy);
        btnFavorite = findViewById(R.id.btnFavorite);

        // Nhận dữ liệu từ Intent
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String imageFilename = getIntent().getStringExtra("imageFilename");
        unitPrice = getIntent().getIntExtra("price", 15000);

        // Gán dữ liệu
        tvName.setText(name);
        tvDescription.setText(description);
        updateQuantityDisplay();

        // Load ảnh bằng Glide
        if (imageFilename != null && !imageFilename.isEmpty()) {
            String imageUrl = baseImageUrl + imageFilename;
            Glide.with(this)
                    .load(getGlideUrlWithToken(imageUrl))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imgFood);
        } else {
            imgFood.setImageResource(R.drawable.placeholder);
        }

        // Tăng số lượng
        btnPlus.setOnClickListener(v -> {
            quantity++;
            updateQuantityDisplay();
        });

        // Giảm số lượng
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityDisplay();
            }
        });

        // Yêu thích
        btnFavorite.setOnClickListener(v ->
                Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show()
        );

        // Mua hàng
        btnBuy.setOnClickListener(v -> {
            int total = quantity * unitPrice;
            String formattedTotal = NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(total);
            Toast.makeText(this, "Đã mua " + quantity + " món với tổng: " + formattedTotal + " VND", Toast.LENGTH_SHORT).show();
        });

        // Quay lại
        btnBack.setOnClickListener(v -> finish());
    }

    private void updateQuantityDisplay() {
        tvQuantity.setText(String.valueOf(quantity));
        String formattedPrice = NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(unitPrice * quantity);
        tvPrice.setText(formattedPrice + " VND");
    }

    private GlideUrl getGlideUrlWithToken(String imageUrl) {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String token = prefs.getString("access_token", null);

        return new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + token)
                .build());
    }
}
