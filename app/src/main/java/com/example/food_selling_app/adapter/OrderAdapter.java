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
        holder.tvOrderCode.setText("Đơn #" + order.getOrderCode());
        holder.tvOrderStatus.setText(order.getStatus());
        holder.tvCustomerName.setText("Khách: " + order.getCustomerName());
        holder.tvOrderDate.setText("Ngày: " + order.getOrderDate());
        holder.tvFoodList.setText(order.getFoodList());
        holder.tvTotalAmount.setText("Tổng tiền: " + order.getTotalAmount());

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
            case "Đã huỷ": // phòng trường hợp nhập khác dấu
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
        TextView tvOrderCode, tvOrderStatus, tvCustomerName, tvOrderDate, tvFoodList, tvTotalAmount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvFoodList = itemView.findViewById(R.id.tvFoodList);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
        }
    }
}