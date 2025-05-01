package com.example.food_selling_app.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_selling_app.R;

public class FoodDetailActivity extends AppCompatActivity {

    ImageView imgFood;
    TextView tvName, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooddetail);

        imgFood = findViewById(R.id.imgFood);
        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);

        // Lấy dữ liệu từ Intent
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        int imageResId = getIntent().getIntExtra("imageResId", R.drawable.placeholder);

        // Gán dữ liệu
        tvName.setText(name);
        tvDescription.setText(description);
        imgFood.setImageResource(imageResId);
    }
}
