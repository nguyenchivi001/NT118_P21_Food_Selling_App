package com.example.food_selling_app.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.food_selling_app.R;

import java.text.NumberFormat;
import java.util.Locale;

public class FoodDetailActivity extends AppCompatActivity {

    private ImageView imgFood;
    private TextView tvName, tvDescription, tvPrice, tvQuantity;
    private ImageButton btnPlus, btnMinus;
    private Button btnBuy, btnFavorite;

    private int quantity = 1;
    private int unitPrice = 15000; // default

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
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnBuy = findViewById(R.id.btnBuy);
        btnFavorite = findViewById(R.id.btnFavorite);

        // Nhận dữ liệu từ intent
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String imageFilename = getIntent().getStringExtra("imageFilename");
        unitPrice = getIntent().getIntExtra("price", 15000);

        // Gán dữ liệu
        tvName.setText(name);
        tvDescription.setText(description);
        updateQuantityDisplay();

        // Load ảnh bằng Glide (nếu dùng ảnh từ server)
        if (imageFilename != null && !imageFilename.isEmpty()) {
            String imageUrl = "http://10.0.2.2:8080/api/foods/images/" + imageFilename;
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imgFood);
        } else {
            imgFood.setImageResource(R.drawable.placeholder);
        }

        // Xử lý tăng/giảm số lượng
        btnPlus.setOnClickListener(v -> {
            quantity++;
            updateQuantityDisplay();
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityDisplay();
            }
        });

        // Nút yêu thích
        btnFavorite.setOnClickListener(v -> {
            Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
        });

        // Nút mua
        btnBuy.setOnClickListener(v -> {
            int total = quantity * unitPrice;
            String formattedTotal = NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(total);
            Toast.makeText(this, "Đã mua " + quantity + " món với tổng: " + formattedTotal + " VND", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateQuantityDisplay() {
        tvQuantity.setText(String.valueOf(quantity));
        String formattedPrice = NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(unitPrice * quantity);
        tvPrice.setText(formattedPrice + " VND");
    }
}
