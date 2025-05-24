package com.example.food_selling_app.ui;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.OrderAdapter;
import com.example.food_selling_app.model.Order;

import java.util.ArrayList;
import java.util.List;

public class ManageOrderActivity extends BaseAdminActivity {

    private RecyclerView rvOrders;
    private List<Order> orderList;
    private OrderAdapter orderAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manage_order;
    }

    @Override
    protected int getSelectedNavItem() {
        return R.id.nav_manage_orders;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));

        // Dữ liệu mẫu
        orderList = new ArrayList<>();
        orderList.add(new Order("#10001", "Nguyễn Văn A", "20/05/2025", "• Burger bò x2\n• Gà rán x1\n• Pepsi x3", "Đã giao", 250000));
        orderList.add(new Order("#10002", "Trần Thị B", "21/05/2025", "• Pizza x1\n• Coca x2", "Đã hủy", 180000));
        orderList.add(new Order("#10003", "Lê Văn C", "22/05/2025", "• Bánh mì x3\n• Nước cam x1", "Đang xử lý", 120000));

        orderAdapter = new OrderAdapter(this, orderList);
        rvOrders.setAdapter(orderAdapter);
    }
}
