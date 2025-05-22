package com.example.food_selling_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_selling_app.R;
import com.google.android.material.button.MaterialButton;

public class PaymentDetailActivity extends AppCompatActivity {

    private LinearLayout cardFieldsLayout, momoFieldsLayout, zalopayFieldsLayout;
    private TextView txtCashMessage;
    private EditText edtCardNumber, edtExpiry, edtCvv, edtMomoPhone, edtZaloPayId;
    private MaterialButton btnConfirm;
    private String selectedMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        // Nhận phương thức từ Intent
        selectedMethod = getIntent().getStringExtra("method");

        // Ánh xạ view
        cardFieldsLayout = findViewById(R.id.cardFieldsLayout);
        momoFieldsLayout = findViewById(R.id.momoFieldsLayout);
        zalopayFieldsLayout = findViewById(R.id.zalopayFieldsLayout);
        txtCashMessage = findViewById(R.id.txtCashMessage);

        edtCardNumber = findViewById(R.id.edtCardNumber);
        edtExpiry = findViewById(R.id.edtExpiry);
        edtCvv = findViewById(R.id.edtCvv);
        edtMomoPhone = findViewById(R.id.edtMomoPhone);
        edtZaloPayId = findViewById(R.id.edtZaloPayId);

        btnConfirm = findViewById(R.id.btnConfirmPayment);

        // Hiển thị layout phù hợp
        showFieldsForMethod(selectedMethod);

        // Xử lý nút xác nhận
        btnConfirm.setOnClickListener(v -> {
            // TODO: Có thể kiểm tra dữ liệu ở đây nếu cần
            Intent resultIntent = new Intent();
            resultIntent.putExtra("method", selectedMethod);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void showFieldsForMethod(String method) {
        // Ẩn toàn bộ trước
        cardFieldsLayout.setVisibility(View.GONE);
        momoFieldsLayout.setVisibility(View.GONE);
        zalopayFieldsLayout.setVisibility(View.GONE);
        txtCashMessage.setVisibility(View.GONE);

        // Hiển thị đúng layout theo phương thức
        if (method.equalsIgnoreCase("MasterCard") || method.equalsIgnoreCase("Visa") || method.contains("Card")) {
            cardFieldsLayout.setVisibility(View.VISIBLE);
        } else if (method.equalsIgnoreCase("Momo")) {
            momoFieldsLayout.setVisibility(View.VISIBLE);
        } else if (method.equalsIgnoreCase("ZaloPay")) {
            zalopayFieldsLayout.setVisibility(View.VISIBLE);
        } else if (method.equalsIgnoreCase("Cash") || method.toLowerCase().contains("thanh toán khi nhận hàng")) {
            txtCashMessage.setVisibility(View.VISIBLE);
        }
    }
}
