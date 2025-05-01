package com.example.food_selling_app.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.food_selling_app.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Nhấn icon Back thì quay về HomeActivity
        ImageView btnBack = findViewById(R.id.btnBack); // id của icon back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // quay lại activity trước (HomeActivity)
            }
        });
    }
}
