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
import com.example.food_selling_app.adapter.AdminOptionAdapter;
import com.example.food_selling_app.model.AdminOption;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    private RecyclerView rvAdminOptions;
    private List<AdminOption> optionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.admin_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvAdminOptions = findViewById(R.id.rvAdminOptions);
        setupOptions();
    }

    private void setupOptions() {
        optionList = new ArrayList<>();
        optionList.add(new AdminOption("Thống kê", R.drawable.ic_dashboard));
        optionList.add(new AdminOption("Quản lý món ăn", R.drawable.ic_food));
        optionList.add(new AdminOption("Quản lý đơn hàng", R.drawable.ic_order));
        optionList.add(new AdminOption("Tài khoản người dùng", R.drawable.ic_user1));

        AdminOptionAdapter adapter = new AdminOptionAdapter(this, optionList, option -> {
            switch (option.getTitle()) {
                case "Quản lý món ăn":
                    startActivity(new Intent(this, ManageFoodActivity.class));
                    break;
                case "Quản lý đơn hàng":
                    startActivity(new Intent(this, ManageOrderActivity.class));
                    break;
                case "Tài khoản người dùng":
                    startActivity(new Intent(this, ManageUserActivity.class));
                    break;
                case "Thống kê":
                    startActivity(new Intent(this, DashboardActivity.class));
                    break;
            }
        });

        rvAdminOptions.setLayoutManager(new GridLayoutManager(this, 2));
        rvAdminOptions.setAdapter(adapter);
    }
}
