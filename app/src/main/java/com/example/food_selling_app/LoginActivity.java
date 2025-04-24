package com.example.food_selling_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
            String user = edtUsername.getText().toString();
            String pass = edtPassword.getText().toString();

            if (user.equals("admin") && pass.equals("123456")) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegisterNow.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Reset password link sent.", Toast.LENGTH_SHORT).show();
        });
    }
}
