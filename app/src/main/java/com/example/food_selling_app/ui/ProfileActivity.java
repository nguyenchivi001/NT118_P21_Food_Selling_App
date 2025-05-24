package com.example.food_selling_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.OrderHistoryAdapter;
import com.example.food_selling_app.api.ApiClient;
import com.example.food_selling_app.api.AuthApi;
import com.example.food_selling_app.api.OrderApi;
import com.example.food_selling_app.api.UserApi;
import com.example.food_selling_app.dto.request.LogoutRequest;
import com.example.food_selling_app.dto.response.ApiResponse;
import com.example.food_selling_app.dto.response.OrderResponse;
import com.example.food_selling_app.model.Order;
import com.example.food_selling_app.model.User;
import com.example.food_selling_app.utils.TokenManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    private static final String TAG = "ProfileActivity";

    private EditText edtName, edtEmail;
    private MaterialButton btnLogout, btnEditProfile, btnOrderHistory;
    private RecyclerView rvOrderHistory;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<Order> orderList;

    private String userEmail;
    private String userToken;
    private int userId;

    private boolean isOrderHistoryVisible = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    protected int getSelectedNavItem() {
        return R.id.nav_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        btnLogout = findViewById(R.id.btnLogout);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnOrderHistory = findViewById(R.id.btnOrderHistory);

        rvOrderHistory = findViewById(R.id.rvOrderHistory);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        userEmail = TokenManager.getEmail(this);
        userToken = TokenManager.getAccessToken(this);
        userId = getSharedPreferences("prefs", MODE_PRIVATE).getInt("user_id", 0);

        if (userEmail == null || userToken == null || userId == 0) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            redirectToLogin();
            return;
        }

        orderList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(this, orderList, this::cancelOrder);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
        rvOrderHistory.setAdapter(orderHistoryAdapter);

        // Mặc định ẩn lịch sử đơn hàng
        rvOrderHistory.setVisibility(View.GONE);

        btnOrderHistory.setOnClickListener(v -> {
            if (!isOrderHistoryVisible) {
                // Hiển thị và tải lại lịch sử đơn hàng
                fetchOrderHistory();
            } else {
                // Ẩn lịch sử đơn hàng khi nhấn lại
                rvOrderHistory.setVisibility(View.GONE);
                isOrderHistoryVisible = false;
                btnOrderHistory.setText("Lịch sử đơn hàng");
            }
        });

        btnLogout.setOnClickListener(v -> showLogoutConfirmationDialog());

        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng chỉnh sửa hồ sơ đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchUserProfile() {
        UserApi userApi = ApiClient.getClient(userToken).create(UserApi.class);
        Call<User> call = userApi.getUserProfile();

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    edtName.setText(user.getName());
                    edtEmail.setText(user.getEmail());
                } else {
                    Toast.makeText(ProfileActivity.this, "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchOrderHistory() {
        OrderApi orderApi = ApiClient.getClient(userToken).create(OrderApi.class);
        Call<ApiResponse<OrderResponse>> call = orderApi.searchOrders(userId, "", "", 0, 50);

        call.enqueue(new Callback<ApiResponse<OrderResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderResponse>> call, Response<ApiResponse<OrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    OrderResponse orderResponse = response.body().getData();
                    orderList.clear();
                    orderList.addAll(orderResponse.getOrders());
                    orderHistoryAdapter.updateOrders(orderList);
                    rvOrderHistory.setVisibility(View.VISIBLE);
                    isOrderHistoryVisible = true;
                    btnOrderHistory.setText("Ẩn lịch sử đơn hàng");
                } else {
                    Toast.makeText(ProfileActivity.this, "Không thể tải lịch sử đơn hàng", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Fetch orders failed: code=" + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderResponse>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Fetch orders failure: " + t.getMessage());
            }
        });
    }

    private void cancelOrder(int orderId) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận hủy đơn hàng")
                .setMessage("Bạn có chắc chắn muốn hủy đơn hàng #" + orderId + "?")
                .setPositiveButton("Hủy đơn", (dialog, which) -> performCancelOrder(orderId))
                .setNegativeButton("Không", null)
                .setCancelable(true)
                .show();
    }

    private void performCancelOrder(int orderId) {
        OrderApi orderApi = ApiClient.getClient(userToken).create(OrderApi.class);
        Call<ApiResponse<Order>> call = orderApi.updateOrderStatus(orderId, "cancel");

        call.enqueue(new Callback<ApiResponse<Order>>() {
            @Override
            public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(ProfileActivity.this, "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                    fetchOrderHistory();
                } else {
                    Toast.makeText(ProfileActivity.this, "Không thể hủy đơn hàng. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> performLogout())
                .setNegativeButton("Hủy", null)
                .setCancelable(true)
                .show();
    }

    private void performLogout() {
        AuthApi authApi = ApiClient.getClient(userToken).create(AuthApi.class);
        LogoutRequest logoutRequest = new LogoutRequest(userEmail);

        Call<ResponseBody> call = authApi.logout(logoutRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    TokenManager.clearTokensAndEmail(ProfileActivity.this);
                    redirectToLogin();
                    Toast.makeText(ProfileActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Đăng xuất thất bại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
