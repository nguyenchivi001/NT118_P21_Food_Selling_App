package com.example.food_selling_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_selling_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseAdminActivity extends AppCompatActivity {

    protected abstract int getLayoutId();           // Layout nội dung cụ thể của mỗi Activity con
    protected abstract int getSelectedNavItem();    // ID menu tương ứng với tab hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_admin);        // Layout chung đã có bottom nav và content frame

        // Chèn layout con vào FrameLayout
        FrameLayout contentFrame = findViewById(R.id.admin_content_frame);
        LayoutInflater.from(this).inflate(getLayoutId(), contentFrame, true);

        // Áp dụng inset cho status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.admin_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.admin_bottom_navigation);
        bottomNav.setSelectedItemId(getSelectedNavItem());

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == getSelectedNavItem()) return true;

            if (id == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardActivity.class));
            } else if (id == R.id.nav_manage_food) {
                startActivity(new Intent(this, ManageFoodActivity.class));
            } else if (id == R.id.nav_manage_orders) {
                startActivity(new Intent(this, ManageOrderActivity.class));
            } else if (id == R.id.nav_users) {
                startActivity(new Intent(this, ManageUserActivity.class));
            } else {
                return false;
            }

            overridePendingTransition(0, 0);
            finish();
            return true;
        });

    }
}
