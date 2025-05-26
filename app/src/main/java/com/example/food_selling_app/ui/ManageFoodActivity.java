package com.example.food_selling_app.ui;

import android.os.Bundle;

import com.example.food_selling_app.R;

public class ManageFoodActivity extends BaseAdminActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manage_food;
    }

    @Override
    protected int getSelectedNavItem() {
        return R.id.nav_manage_food;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Xử lý logic nếu có
    }
}
