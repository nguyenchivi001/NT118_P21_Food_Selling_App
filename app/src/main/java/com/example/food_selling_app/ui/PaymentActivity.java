package com.example.food_selling_app.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_selling_app.R;

public class PaymentActivity extends AppCompatActivity {

    private LinearLayout paymentMethodLayout;
    private ImageView imgSelectedMethod;
    private TextView txtSelectedMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Gán ID đúng với layout
        paymentMethodLayout = findViewById(R.id.layoutPaymentMethodClickable);
        imgSelectedMethod = findViewById(R.id.imgSelectedMethod);
        txtSelectedMethod = findViewById(R.id.txtSelectedPaymentMethod);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish()); // Quay lại trang trước

        // Mở hộp thoại chọn phương thức thanh toán
        paymentMethodLayout.setOnClickListener(v -> showPaymentOptionsDialog());
    }

    private void showPaymentOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.item_payment_option, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.optionMastercard).setOnClickListener(v -> {
            dialog.dismiss();
            updateSelectedMethod(R.drawable.mastercard, "Thẻ MasterCard", "MasterCard");
        });
        dialogView.findViewById(R.id.optionVisa).setOnClickListener(v -> {
            dialog.dismiss();
            updateSelectedMethod(R.drawable.visa, "Thẻ Visa", "Visa");
        });
        dialogView.findViewById(R.id.optionMomo).setOnClickListener(v -> {
            dialog.dismiss();
            updateSelectedMethod(R.drawable.momo, "MoMo E-Wallet", "Momo");
        });
        dialogView.findViewById(R.id.optionZalopay).setOnClickListener(v -> {
            dialog.dismiss();
            updateSelectedMethod(R.drawable.zalopay, "ZaloPay", "ZaloPay");
        });
        dialogView.findViewById(R.id.optionCash).setOnClickListener(v -> {
            dialog.dismiss();
            updateSelectedMethod(R.drawable.cash, "Thanh toán khi nhận hàng", "Cash");
        });

        dialog.show();
    }

    private void updateSelectedMethod(int iconResId, String methodName, String methodKey) {
        imgSelectedMethod.setImageResource(iconResId);
        txtSelectedMethod.setText(methodName);

        // Sau khi chọn phương thức xong mở giao diện nhập thông tin
        Intent intent = new Intent(this, PaymentDetailActivity.class);
        intent.putExtra("method", methodKey);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            String confirmedMethod = data.getStringExtra("method");
            // Tuỳ chỉ update text, icon đã đệ ở trên updateSelectedMethod
            txtSelectedMethod.setText("Chọn: " + confirmedMethod);
        }
    }
}
