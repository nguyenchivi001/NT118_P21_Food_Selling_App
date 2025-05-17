package com.example.food_selling_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_selling_app.R;
import com.example.food_selling_app.api.ApiClient;
import com.example.food_selling_app.api.AuthApi;
import com.example.food_selling_app.dto.request.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText edtFullName, edtUsername, edtPassword;
    private Button btnSignUp;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Ánh xạ view
        edtFullName = findViewById(R.id.edtFullName);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        // Chuyển sang màn Login
        tvGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });

        // Xử lý đăng ký
        btnSignUp.setOnClickListener(v -> {
            String name = edtFullName.getText().toString().trim();
            String email = edtUsername.getText().toString().trim(); // dùng username làm email
            String password = edtPassword.getText().toString().trim();

            // Kiểm tra thông tin đầu vào
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
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

            // Nếu tất cả kiểm tra đều hợp lệ, gửi yêu cầu đăng ký
            RegisterRequest request = new RegisterRequest(name, email, password);
            AuthApi authApi = ApiClient.getClient(null).create(AuthApi.class);

            authApi.register(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        finish(); // Quay lại login
                    } else if (response.code() == 409) {
                        Toast.makeText(SignupActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String errorMessage = response.errorBody().string();
                            Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(SignupActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(SignupActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}