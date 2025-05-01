package com.example.food_selling_app.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.FoodAdapter;
import com.example.food_selling_app.model.Food;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView rvFoodList;
    FoodAdapter adapter;
    List<Food> foodList;

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

        rvFoodList = findViewById(R.id.rvFoodList);
        rvFoodList.setLayoutManager(new GridLayoutManager(this, 2));

        foodList = new ArrayList<>();
        foodList.add(new Food(1, "Cheeseburger", "Wendy's Burger", 49000, "burger1.png", 1));
        foodList.add(new Food(2, "Veggie Burger", "Healthy & Green", 48000, "burger2.png", 1));
        foodList.add(new Food(3, "Chicken Burger", "Fried Chicken Inside", 46000, "burger3.png", 1));
        foodList.add(new Food(4, "Spicy Burger", "Fried Chicken Spicy", 45000, "burger4.png", 1));

        adapter = new FoodAdapter(this, foodList);
        rvFoodList.setAdapter(adapter);

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
            }
            return false;
        });
    }
}
