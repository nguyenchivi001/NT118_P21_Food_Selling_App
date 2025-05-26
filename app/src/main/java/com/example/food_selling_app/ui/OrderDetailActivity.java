package com.example.food_selling_app.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.OrderItemAdapter;
import com.example.food_selling_app.model.Order;
import com.example.food_selling_app.model.OrderItem;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvStatus, tvTotalPrice, tvCreatedAt, tvPaymentMethod, tvDeliveryAddress;
    private RecyclerView rvOrderItems;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        tvOrderId = findViewById(R.id.tvOrderId);
        tvStatus = findViewById(R.id.tvStatus);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        btnBack = findViewById(R.id.btnBack);

        Order order = (Order) getIntent().getSerializableExtra("order");
        if (order != null) {
            tvOrderId.setText("Mã đơn: #" + order.getId());
            tvStatus.setText("Trạng thái: " + order.getStatus());
            tvTotalPrice.setText("Tổng tiền: " + NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(order.getTotalPrice()) + "đ");
            tvCreatedAt.setText("Ngày tạo: " + order.getCreatedAt());
            tvPaymentMethod.setText("Thanh toán: " + (order.isPaid() ? "Đã thanh toán" : "Tiền mặt"));
            tvDeliveryAddress.setText("Địa chỉ: " + order.getDeliveryAddress());

            List<OrderItem> orderItems = order.getOrderItems();

            // Map giả lập cho tên, giá, hình ảnh món ăn
            Map<Integer, String> nameMap = new HashMap<>();
            nameMap.put(101, "Burger bò");
            nameMap.put(102, "Gà rán");
            nameMap.put(103, "Pepsi");

            Map<Integer, Double> priceMap = new HashMap<>();
            priceMap.put(101, 46000.0);
            priceMap.put(102, 35000.0);
            priceMap.put(103, 12000.0);

            Map<Integer, Integer> imageMap = new HashMap<>();
            imageMap.put(101, R.drawable.burger1);
            imageMap.put(102, R.drawable.burger2);
            imageMap.put(103, R.drawable.burger3);

            OrderItemAdapter adapter = new OrderItemAdapter(
                    this, orderItems, nameMap, priceMap, imageMap
            );
            rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
            rvOrderItems.setAdapter(adapter);
        }

        btnBack.setOnClickListener(v -> onBackPressed());
    }
}
