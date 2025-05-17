package com.example.food_selling_app.dto.request;

import android.util.Patterns;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;

    public RegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Validate đầu vào
    public String validate() {
        if (name == null || name.trim().isEmpty()) {
            return "Vui lòng nhập họ tên";
        }

        if (!name.matches("^[a-zA-ZÀ-ỹ\\s]{2,100}$")) {
            return "Tên chỉ được chứa chữ cái và khoảng trắng, độ dài 2-100 ký tự";
        }

        if (email == null || email.trim().isEmpty()) {
            return "Vui lòng nhập email";
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email không hợp lệ";
        }

        if (password == null || password.trim().isEmpty()) {
            return "Vui lòng nhập mật khẩu";
        }

        if (password.length() < 6) {
            return "Mật khẩu phải từ 6 ký tự trở lên";
        }

        // Regex: ít nhất 1 số, 1 thường, 1 HOA, 1 ký tự đặc biệt
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
        if (!password.matches(passwordRegex)) {
            return "Mật khẩu phải gồm chữ HOA, thường, số và ký tự đặc biệt";
        }

        return null; // hợp lệ
    }

    // Getter
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
