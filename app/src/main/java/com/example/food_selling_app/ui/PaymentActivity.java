package com.example.food_selling_app.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_selling_app.R;
import com.example.food_selling_app.api.ApiClient;
import com.example.food_selling_app.api.CartApi;
import com.example.food_selling_app.api.OrderApi;
import com.example.food_selling_app.dto.request.OrderRequest;
import com.example.food_selling_app.dto.request.OrderItemRequest;
import com.example.food_selling_app.dto.response.ApiResponse;
import com.example.food_selling_app.model.CartItem;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";
    private LinearLayout paymentMethodLayout;
    private ImageView imgSelectedMethod;
    private TextView txtSelectedMethod;
    private EditText edtShippingAddress;
    private TextView tvOrderTotal, tvShippingFee, tvTotal, tvFooterTotal;
    private MaterialButton btnPayNow;
    private String selectedPaymentMethod = "online"; // Mặc định là online
    private double totalPrice;
    private List<CartItem> cartItems;
    private OrderApi orderApi;
    private CartApi cartApi;
    private String accessToken;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Khởi tạo views
        paymentMethodLayout = findViewById(R.id.layoutPaymentMethodClickable);
        imgSelectedMethod = findViewById(R.id.imgSelectedMethod);
        txtSelectedMethod = findViewById(R.id.txtSelectedPaymentMethod);
        edtShippingAddress = findViewById(R.id.edtShippingAddress);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotal = findViewById(R.id.tvTotal);
        tvFooterTotal = findViewById(R.id.tvFooterTotal);
        btnPayNow = findViewById(R.id.btnPayNow);
        ImageView btnBack = findViewById(R.id.btnBack);

        // Lấy dữ liệu từ Intent
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        cartItems = getIntent().getParcelableArrayListExtra("cartItems");

        // Kiểm tra dữ liệu đầu vào
        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Lấy thông tin user và token
        accessToken = getSharedPreferences("prefs", MODE_PRIVATE).getString("access_token", "");
        userId = getSharedPreferences("prefs", MODE_PRIVATE).getInt("user_id", 0);
        if (accessToken.isEmpty() || userId == 0) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo API clients
        orderApi = ApiClient.getClient(accessToken).create(OrderApi.class);
        cartApi = ApiClient.getClient(accessToken).create(CartApi.class);

        // Hiển thị thông tin giá
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        String formattedTotal = formatter.format(totalPrice) + "đ";
        tvOrderTotal.setText("Đơn hàng: " + formattedTotal);
        tvShippingFee.setText("Phí giao hàng: 0đ");
        tvTotal.setText("Tổng cộng: " + formattedTotal);
        tvFooterTotal.setText("Tổng: " + formattedTotal);

        // Sự kiện nút Back
        btnBack.setOnClickListener(v -> finish());

        // Sự kiện chọn phương thức thanh toán
        paymentMethodLayout.setOnClickListener(v -> showPaymentOptionsDialog());

        // Sự kiện nút Pay Now
        btnPayNow.setOnClickListener(v -> processPayment());
    }

    private void showPaymentOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.item_payment_option, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.optionMastercard).setOnClickListener(v -> {
            dialog.dismiss();
            updateSelectedMethod(R.drawable.mastercard, "Thẻ MasterCard", "online");
        });
        dialogView.findViewById(R.id.optionVisa).setOnClickListener(v -> {
            dialog.dismiss();
            updateSelectedMethod(R.drawable.visa, "Thẻ Visa", "online");
        });
        dialogView.findViewById(R.id.optionMomo).setOnClickListener(v -> {
            dialog.dismiss();
            updateSelectedMethod(R.drawable.momo, "MoMo E-Wallet", "online");
        });
        dialogView.findViewById(R.id.optionZalopay).setOnClickListener(v -> {
            dialog.dismiss();
            updateSelectedMethod(R.drawable.zalopay, "ZaloPay", "online");
        });
        dialogView.findViewById(R.id.optionCash).setOnClickListener(v -> {
            dialog.dismiss();
            updateSelectedMethod(R.drawable.cash, "Thanh toán khi nhận hàng", "offline");
        });

        dialog.show();
    }

    private void updateSelectedMethod(int iconResId, String methodName, String methodKey) {
        imgSelectedMethod.setImageResource(iconResId);
        txtSelectedMethod.setText(methodName);
        selectedPaymentMethod = methodKey;
    }

    private void processPayment() {
        String shippingAddress = edtShippingAddress.getText().toString().trim();
        if (shippingAddress.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo OrderRequest
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(userId);
        orderRequest.setDeliveryAddress(shippingAddress);
        orderRequest.setPaymentMethod(selectedPaymentMethod);

        List<OrderItemRequest> orderItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            OrderItemRequest orderItem = new OrderItemRequest();
            orderItem.setFoodId(item.getFoodId());
            orderItem.setQuantity(item.getQuantity());
            orderItems.add(orderItem);
        }
        orderRequest.setOrderItems(orderItems);

        // Gọi API tạo đơn hàng
        Call<ApiResponse<OrderRequest>> call = orderApi.createOrder(orderRequest);
        call.enqueue(new Callback<ApiResponse<OrderRequest>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderRequest>> call, Response<ApiResponse<OrderRequest>> response) {
                Log.d(TAG, "createOrder response: code=" + response.code() + ", body=" + (response.body() != null ? response.body().isSuccess() : "null"));
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    // Xóa giỏ hàng
                    clearCart();
                } else {
                    String errorMessage = "Không thể tạo đơn hàng. Mã lỗi: " + response.code();
                    Log.e(TAG, "createOrder failed: " + errorMessage);
                    Toast.makeText(PaymentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderRequest>> call, Throwable t) {
                Log.e(TAG, "createOrder failure: " + t.getMessage());
                Toast.makeText(PaymentActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearCart() {
        Call<ApiResponse<Void>> call = cartApi.clearCart();
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                Log.d(TAG, "clearCart response: code=" + response.code() + ", body=" + (response.body() != null ? response.body().isSuccess() : "null"));
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    // Hiển thị thông báo thành công
                    new AlertDialog.Builder(PaymentActivity.this)
                            .setTitle("Thành công")
                            .setMessage("Đơn hàng của bạn đã được đặt thành công!")
                            .setPositiveButton("OK", (dialog, which) -> {
                                // Quay lại HomeActivity
                                Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            })
                            .setCancelable(false)
                            .show();
                } else {
                    String errorMessage = "Không thể xóa giỏ hàng. Mã lỗi: " + response.code();
                    Log.e(TAG, "clearCart failed: " + errorMessage);
                    Toast.makeText(PaymentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Log.e(TAG, "clearCart failure: " + t.getMessage());
                Toast.makeText(PaymentActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}