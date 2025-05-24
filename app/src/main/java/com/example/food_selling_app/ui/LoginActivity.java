package com.example.food_selling_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_selling_app.R;
import com.example.food_selling_app.api.ApiClient;
import com.example.food_selling_app.api.AuthApi;
import com.example.food_selling_app.api.UserApi;
import com.example.food_selling_app.dto.request.AuthRequest;
import com.example.food_selling_app.dto.response.AuthResponse;
import com.example.food_selling_app.model.User;
import com.example.food_selling_app.utils.JwtUtils;
import com.example.food_selling_app.utils.TokenManager;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView tvRegisterNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterNow = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> {
            String email = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // Kiểm tra thông tin đầu vào
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra định dạng email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Định dạng email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra độ dài mật khẩu
            if (password.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            // Nếu tất cả kiểm tra đều hợp lệ, gửi yêu cầu đăng nhập
            AuthApi authApi = ApiClient.getClient(null).create(AuthApi.class);
            AuthRequest authRequest = new AuthRequest(email, password);

            authApi.login(authRequest).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AuthResponse authResponse = response.body();
                        TokenManager.saveToken(LoginActivity.this, authResponse.getAccessToken(), authResponse.getRefreshToken());
                        TokenManager.saveEmail(LoginActivity.this, email);
                        getUserRole(authResponse.getAccessToken());
                    } else {
                        // Kiểm tra mã lỗi và thông báo chi tiết từ server
                        try {
                            String errorMessage = response.errorBody().string(); // lấy thông báo lỗi từ response
                            Log.e("LOGIN_ERROR", errorMessage); // log thông báo lỗi
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("LOGIN_FAIL", e.getMessage(), e);
                            Toast.makeText(LoginActivity.this, "Đăng nhập không thành công. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Log.e("LOGIN_FAIL", t.getMessage(), t);
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                }
            });
        });

        tvRegisterNow.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }

    private void getUserRole(String accessToken) {
        JSONArray roles = JwtUtils.getRolesFromToken(accessToken);

        fetchAndShowUserName(accessToken);

        if (roles != null) {
            for (int i = 0; i < roles.length(); i++) {
                try {
                    String role = roles.getString(i);

                    if ("ROLE_ADMIN".equalsIgnoreCase(role)) {
                        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                        finish();
                        return;
                    } else if ("ROLE_USER".equalsIgnoreCase(role)) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(LoginActivity.this, "Vai trò không hợp lệ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "Không thể đọc vai trò từ token", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAndShowUserName(String accessToken) {
        UserApi userApi = ApiClient.getClient(accessToken).create(UserApi.class);
        userApi.getUserProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    String name = user.getName();
                    int userId = user.getId();

                    TokenManager.saveUserId(LoginActivity.this, userId);
                    Toast.makeText(LoginActivity.this, "Xin chào " + name, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("USER_NAME_FAIL", "Không lấy được tên người dùng");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("USER_NAME_ERROR", t.getMessage(), t);
            }
        });
    }
}