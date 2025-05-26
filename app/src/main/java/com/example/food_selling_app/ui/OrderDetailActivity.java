package com.example.food_selling_app.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.OrderItemAdapter;
import com.example.food_selling_app.model.Order;
import com.example.food_selling_app.model.OrderItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvStatus, tvTotalPrice, tvCreatedAt, tvPaymentMethod, tvDeliveryAddress;
    private RecyclerView rvOrderItems;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Khởi tạo views
        tvOrderId = findViewById(R.id.tvOrderId);
        tvStatus = findViewById(R.id.tvStatus);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        btnBack = findViewById(R.id.btnBack);

        // Lấy order từ Intent
        Order order = (Order) getIntent().getSerializableExtra("order");
        if (order != null) {
            // Cập nhật thông tin đơn hàng
            tvOrderId.setText("Mã đơn: #" + order.getId());
            String statusText;
            int statusColor;
            switch (order.getStatus().toLowerCase()) {
                case "pending":
                    statusText = "Đang chờ";
                    statusColor = ContextCompat.getColor(this, android.R.color.holo_orange_light);
                    break;
                case "delivering":
                    statusText = "Đang giao";
                    statusColor = ContextCompat.getColor(this, android.R.color.holo_blue_light);
                    break;
                case "completed":
                    statusText = "Hoàn thành";
                    statusColor = ContextCompat.getColor(this, android.R.color.holo_green_light);
                    break;
                case "cancel":
                    statusText = "Đã hủy";
                    statusColor = ContextCompat.getColor(this, android.R.color.holo_red_light);
                    break;
                default:
                    statusText = order.getStatus();
                    statusColor = ContextCompat.getColor(this, android.R.color.black);
            }
            tvStatus.setText("Trạng thái: " + statusText);
            tvStatus.setTextColor(statusColor);

            tvTotalPrice.setText("Tổng tiền: " + NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(order.getTotalPrice()) + "đ");
            tvCreatedAt.setText("Ngày tạo: " + order.getCreatedAt());
            tvPaymentMethod.setText("Thanh toán: " + (order.isPaid() ? "Đã thanh toán" : "Tiền mặt"));
            tvDeliveryAddress.setText("Địa chỉ: " + order.getDeliveryAddress());

            // Lấy danh sách orderItems
            List<OrderItem> orderItems = order.getOrderItems();

            // Khởi tạo adapter
            OrderItemAdapter adapter = new OrderItemAdapter(this, orderItems);
            rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
            rvOrderItems.setAdapter(adapter);
        }

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> onBackPressed());
    }
}