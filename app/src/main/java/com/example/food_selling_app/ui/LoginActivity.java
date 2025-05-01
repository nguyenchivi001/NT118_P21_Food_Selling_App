package com.example.food_selling_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_selling_app.R;
import com.example.food_selling_app.api.ApiClient;
import com.example.food_selling_app.api.AuthApi;
import com.example.food_selling_app.api.UserApi;
import com.example.food_selling_app.dto.AuthRequest;
import com.example.food_selling_app.dto.AuthResponse;
import com.example.food_selling_app.model.User;
import com.example.food_selling_app.util.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView tvRegisterNow, tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterNow = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnLogin.setOnClickListener(v -> {
            String email = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthApi authApi = ApiClient.getClient(null).create(AuthApi.class);
            AuthRequest authRequest = new AuthRequest(email, password);

            authApi.login(authRequest).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AuthResponse authResponse = response.body();
                        TokenManager.saveToken(LoginActivity.this, authResponse.getAccessToken(), authResponse.getRefreshToken());

                        getUserProfile(authResponse.getAccessToken());
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
    }

    private void getUserProfile(String accessToken) {
        UserApi userApi = ApiClient.getClient(accessToken).create(UserApi.class);

        userApi.getUserProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Toast.makeText(LoginActivity.this, "Xin chào " + user.getName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Log.e("USER_ME_FAIL", "Code: " + response.code());
                    Toast.makeText(LoginActivity.this, "Không lấy được thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("PROFILE_FAIL", t.getMessage(), t);
                Toast.makeText(LoginActivity.this, "Lỗi khi lấy profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
