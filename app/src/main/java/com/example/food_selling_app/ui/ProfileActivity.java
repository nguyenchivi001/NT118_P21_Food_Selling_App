package com.example.food_selling_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_selling_app.R;
import com.example.food_selling_app.api.ApiClient;
import com.example.food_selling_app.api.AuthApi;
import com.example.food_selling_app.api.UserApi;
import com.example.food_selling_app.dto.request.LogoutRequest;
import com.example.food_selling_app.model.User;
import com.example.food_selling_app.util.TokenManager;
import com.google.android.material.button.MaterialButton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private MaterialButton btnLogout;
    private String userEmail;
    private String userToken;

    private EditText edtName, edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnLogout = findViewById(R.id.btnLogout);

        userEmail = TokenManager.getEmail(this);
        userToken = TokenManager.getAccessToken(this);

        if (userEmail == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            redirectToLogin();
            return;
        }

        fetchUserProfile();

        btnLogout.setOnClickListener(v -> showLogoutConfirmationDialog());

        // Xử lý sổ xuống Chi tiết thanh toán & Lịch sử đơn hàng
        View expandPaymentDetails = findViewById(R.id.expandPaymentDetails);
        View expandOrderHistory = findViewById(R.id.expandOrderHistory);
        View tvPaymentDetails = findViewById(R.id.tvPaymentDetails);
        View tvOrderHistory = findViewById(R.id.tvOrderHistory);

        tvPaymentDetails.setOnClickListener(v -> {
            if (expandPaymentDetails.getVisibility() == View.GONE) {
                expandPaymentDetails.setVisibility(View.VISIBLE);
            } else {
                expandPaymentDetails.setVisibility(View.GONE);
            }
        });

        tvOrderHistory.setOnClickListener(v -> {
            if (expandOrderHistory.getVisibility() == View.GONE) {
                expandOrderHistory.setVisibility(View.VISIBLE);
            } else {
                expandOrderHistory.setVisibility(View.GONE);
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

    private void redirectToLogin() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
