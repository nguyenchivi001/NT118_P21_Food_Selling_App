package com.example.food_selling_app.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.food_selling_app.R;

import java.text.NumberFormat;
import java.util.Locale;

public class FoodDetailActivity extends AppCompatActivity {

    ImageView imgFood, btnBack;
    TextView tvName, tvDescription, tvQuantity;
    Button btnPrice, btnOrderNow;
    ImageButton btnMinus, btnPlus;
    SeekBar seekSpicy;
    int quantity = 1;
    double price = 0.0;
    final String baseImageUrl = "http://10.0.2.2:8080/api/foods/images/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooddetail);

        // Ánh xạ view
        imgFood = findViewById(R.id.imgFood);
        btnBack = findViewById(R.id.btnBack);
        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnPrice = findViewById(R.id.btnPrice);
        btnOrderNow = findViewById(R.id.btnOrderNow);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        seekSpicy = findViewById(R.id.seekSpicy);

        // Nhận dữ liệu từ Intent
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String imageFilename = getIntent().getStringExtra("imageFilename");
        String priceStr = getIntent().getStringExtra("price");

        if (priceStr != null) {
            price = Double.parseDouble(priceStr);
        }

        // Gán dữ liệu vào view
        tvName.setText(name);
        tvDescription.setText(description);
        tvQuantity.setText(String.valueOf(quantity));
        updateTotalPrice();

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
            tvQuantity.setText(String.valueOf(quantity));
            updateTotalPrice();
        });

        // Giảm số lượng
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
                updateTotalPrice();
            }
        });

        // Xử lý đặt hàng (có thể mở rộng)
        btnOrderNow.setOnClickListener(v -> {
            Toast.makeText(this,
                    "Đã đặt " + quantity + " " + name,
                    Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void updateTotalPrice() {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        String formatted = formatter.format(price * quantity) + "đ";
        btnPrice.setText(formatted);
    }

    private GlideUrl getGlideUrlWithToken(String imageUrl) {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String token = prefs.getString("access_token", null);

        return new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + token)
                .build());
    }
}
