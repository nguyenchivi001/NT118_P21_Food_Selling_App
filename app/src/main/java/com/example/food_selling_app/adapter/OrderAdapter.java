package com.example.food_selling_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.model.Order;
import com.example.food_selling_app.model.OrderItem;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvOrderCode.setText("Đơn #" + order.getId());
        holder.tvOrderStatus.setText(order.getStatus());
        holder.tvOrderDate.setText("Ngày: " + order.getCreatedAt());
        holder.tvDeliveryAddress.setText("Địa chỉ: " + order.getDeliveryAddress());

        // Gộp danh sách món từ orderItems (dùng foodId thay vì foodName)
        List<OrderItem> items = order.getOrderItems();
        if (items != null && !items.isEmpty()) {
            StringBuilder foodListBuilder = new StringBuilder();
            for (OrderItem item : items) {
                foodListBuilder.append("- Món ID: ")
                        .append(item.getFoodId())
                        .append(" x")
                        .append(item.getQuantity())
                        .append("\n");
            }
            holder.tvFoodList.setText(foodListBuilder.toString().trim());
        } else {
            holder.tvFoodList.setText("Không có món ăn");
        }

        holder.tvTotalAmount.setText("Tổng tiền: " + String.format("%,.0f₫", order.getTotalPrice()));

        switch (order.getStatus()) {
            case "Xác nhận":
            case "Đang xử lý":
            case "Đang giao":
                holder.tvOrderStatus.setBackgroundResource(R.drawable.bg_status_processing);
                break;

            case "Hoàn tất":
                holder.tvOrderStatus.setBackgroundResource(R.drawable.bg_status_done);
                break;

            case "Đã hủy":
            case "Đã huỷ":
                holder.tvOrderStatus.setBackgroundResource(R.drawable.bg_status_cancel);
                break;

            default:
                holder.tvOrderStatus.setBackgroundResource(R.drawable.bg_status_processing);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderCode, tvOrderStatus, tvOrderDate, tvFoodList, tvTotalAmount, tvDeliveryAddress;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvFoodList = itemView.findViewById(R.id.tvFoodList);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);
        }
    }
}
