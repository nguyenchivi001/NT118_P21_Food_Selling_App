package com.example.food_selling_app.ui;

import android.os.Bundle;
import com.example.food_selling_app.R;

public class ManageUserActivity extends BaseAdminActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manage_user;
    }

    @Override
    protected int getSelectedNavItem() {
        return R.id.nav_users;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Xử lý logic nếu có
    }
}
