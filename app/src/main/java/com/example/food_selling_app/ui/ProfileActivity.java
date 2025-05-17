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

        // Khởi tạo nút Back
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Khởi tạo nút Logout
        btnLogout = findViewById(R.id.btnLogout);

        // Lấy email và token từ TokenManager
        userEmail = TokenManager.getEmail(this);
        userToken = TokenManager.getAccessToken(this);

        // Kiểm tra email có tồn tại không
        if (userEmail == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            redirectToLogin();
            return;
        }

        fetchUserProfile();

        // Gắn sự kiện nhấn nút Logout
        btnLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    // Hiển thị dialog xác nhận đăng xuất
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> performLogout())
                .setNegativeButton("Hủy", null)
                .setCancelable(true).show();
    }

    // Thực hiện đăng xuất
    private void performLogout() {
        // Sử dụng ApiClient để tạo Retrofit instance
        AuthApi authApi = ApiClient.getClient(userToken).create(AuthApi.class);

        // Tạo request body
        LogoutRequest logoutRequest = new LogoutRequest(userEmail);

        // Gửi yêu cầu đăng xuất
        Call<ResponseBody> call = authApi.logout(logoutRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    TokenManager.clearTokensAndEmail(ProfileActivity.this);
                    redirectToLogin();
                    Toast.makeText(ProfileActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Đăng xuất thất bại: ", Toast.LENGTH_LONG).show();
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

    // Chuyển hướng về LoginActivity
    private void redirectToLogin() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}