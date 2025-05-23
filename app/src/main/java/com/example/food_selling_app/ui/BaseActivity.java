package com.example.food_selling_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_selling_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutId();
    protected abstract int getSelectedNavItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_base);

        // Đổ layout riêng vào content_frame
        FrameLayout contentFrame = findViewById(R.id.content_frame);
        LayoutInflater.from(this).inflate(getLayoutId(), contentFrame, true);

        // Áp dụng insets (chỉ đẩy padding top cho status bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0); // Chỉ top
            return insets;
        });

        setupBottomNav();
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(getSelectedNavItem());

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == getSelectedNavItem()) return true;

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (id == R.id.nav_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
            } else if (id == R.id.nav_message) {
                startActivity(new Intent(this, MessageActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            }

            overridePendingTransition(0, 0);
            finish(); // Đảm bảo không bị lặp Activity
            return true;
        });
    }
}
