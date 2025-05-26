package com.example.food_selling_app.ui;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.AdminOrderAdapter;
import com.example.food_selling_app.model.Order;
import com.example.food_selling_app.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class ManageOrderActivity extends BaseAdminActivity {

    private RecyclerView rvOrders;
    private List<Order> orderList;
    private AdminOrderAdapter orderAdapter;

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

        // Đơn hàng 1
        Order order1 = new Order();
        order1.setId(10001);
        order1.setUserId(1); // giả định userId
        order1.setCreatedAt("20/05/2025");
        order1.setDeliveryAddress("123 Nguyễn Trãi, Q1, TP.HCM");
        order1.setStatus("Đã giao");
        order1.setTotalPrice(250000);
        order1.setPaid(true);
        List<OrderItem> items1 = new ArrayList<>();
        items1.add(createOrderItem(101, 2)); // Burger bò
        items1.add(createOrderItem(102, 1)); // Gà rán
        items1.add(createOrderItem(103, 3)); // Pepsi
        order1.setOrderItems(items1);

        // Đơn hàng 2
        Order order2 = new Order();
        order2.setId(10002);
        order2.setUserId(2);
        order2.setCreatedAt("21/05/2025");
        order2.setDeliveryAddress("45 Lê Duẩn, Q1, TP.HCM");
        order2.setStatus("Đã hủy");
        order2.setTotalPrice(180000);
        order2.setPaid(false);
        List<OrderItem> items2 = new ArrayList<>();
        items2.add(createOrderItem(104, 1)); // Pizza
        items2.add(createOrderItem(105, 2)); // Coca
        order2.setOrderItems(items2);

        // Đơn hàng 3
        Order order3 = new Order();
        order3.setId(10003);
        order3.setUserId(3);
        order3.setCreatedAt("22/05/2025");
        order3.setDeliveryAddress("78 Trường Chinh, Q12, TP.HCM");
        order3.setStatus("Đang xử lý");
        order3.setTotalPrice(120000);
        order3.setPaid(false);
        List<OrderItem> items3 = new ArrayList<>();
        items3.add(createOrderItem(106, 3)); // Bánh mì
        items3.add(createOrderItem(107, 1)); // Nước cam
        order3.setOrderItems(items3);

        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);

        orderAdapter = new AdminOrderAdapter(this, orderList);
        rvOrders.setAdapter(orderAdapter);
    }

    private OrderItem createOrderItem(int foodId, int quantity) {
        OrderItem item = new OrderItem();
        item.setFoodId(foodId);
        item.setQuantity(quantity);
        return item;
    }
}
