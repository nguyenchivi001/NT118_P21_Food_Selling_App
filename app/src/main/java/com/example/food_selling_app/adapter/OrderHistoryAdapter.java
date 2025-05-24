package com.example.food_selling_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.model.Order;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.ParseException;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;
    private OnOrderCancelListener cancelListener;

    public interface OnOrderCancelListener {
        void onCancelOrder(int orderId);
    }

    public OrderHistoryAdapter(Context context, List<Order> orderList, OnOrderCancelListener cancelListener) {
        this.context = context;
        this.orderList = orderList;
        this.cancelListener = cancelListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvOrderId.setText("Mã đơn: #" + order.getId());
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        holder.tvTotalPrice.setText("Tổng: " + formatter.format(order.getTotalPrice()) + "đ");

        // Định dạng createdAt
        String createdAtText = order.getCreatedAt();
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(order.getCreatedAt());
            createdAtText = "Ngày: " + outputFormat.format(date);
        } catch (ParseException e) {
            createdAtText = "Ngày: " + order.getCreatedAt(); // Giữ nguyên nếu parse lỗi
        }
        holder.tvCreatedAt.setText(createdAtText);

        String statusText;
        int statusColor;
        switch (order.getStatus().toLowerCase()) {
            case "pending":
                statusText = "Đang chờ";
                statusColor = ContextCompat.getColor(context, android.R.color.holo_orange_light);
                holder.btnCancelOrder.setVisibility(View.VISIBLE);
                break;
            case "delivering":
                statusText = "Đang giao";
                statusColor = ContextCompat.getColor(context, android.R.color.holo_blue_light);
                holder.btnCancelOrder.setVisibility(View.GONE);
                break;
            case "completed":
                statusText = "Hoàn thành";
                statusColor = ContextCompat.getColor(context, android.R.color.holo_green_light);
                holder.btnCancelOrder.setVisibility(View.GONE);
                break;
            case "cancel":
                statusText = "Đã hủy";
                statusColor = ContextCompat.getColor(context, android.R.color.holo_red_light);
                holder.btnCancelOrder.setVisibility(View.GONE);
                break;
            default:
                statusText = order.getStatus();
                statusColor = ContextCompat.getColor(context, android.R.color.black);
                holder.btnCancelOrder.setVisibility(View.GONE);
        }
        holder.tvStatus.setText("Trạng thái: " + statusText);
        holder.tvStatus.setTextColor(statusColor);

        holder.tvPaymentMethod.setText("Thanh toán: " + (order.isPaid() ? "Đã thanh toán" : "Tiền mặt"));

        holder.btnCancelOrder.setOnClickListener(v -> cancelListener.onCancelOrder(order.getId()));
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public void updateOrders(List<Order> newOrders) {
        this.orderList = newOrders;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvTotalPrice, tvCreatedAt, tvStatus, tvPaymentMethod;
        MaterialButton btnCancelOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
        }
    }
}