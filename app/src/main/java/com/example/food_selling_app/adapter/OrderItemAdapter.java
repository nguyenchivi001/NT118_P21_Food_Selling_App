package com.example.food_selling_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.food_selling_app.R;
import com.example.food_selling_app.model.OrderItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private final Context context;
    private final List<OrderItem> orderItems;
    private final Map<Integer, String> foodNameMap;
    private final Map<Integer, Double> foodPriceMap;
    private final Map<Integer, Integer> foodImageMap;

    public OrderItemAdapter(Context context,
                            List<OrderItem> orderItems,
                            Map<Integer, String> foodNameMap,
                            Map<Integer, Double> foodPriceMap,
                            Map<Integer, Integer> foodImageMap) {
        this.context = context;
        this.orderItems = orderItems;
        this.foodNameMap = foodNameMap;
        this.foodPriceMap = foodPriceMap;
        this.foodImageMap = foodImageMap;
    }

    @NonNull
    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.ViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        int foodId = item.getFoodId();

        String name = foodNameMap.getOrDefault(foodId, "Món #" + foodId);
        double price = foodPriceMap.getOrDefault(foodId, 0.0);
        int imageRes = foodImageMap.getOrDefault(foodId, R.drawable.burger1);

        holder.tvName.setText(name);
        holder.tvPrice.setText(formatPrice(price));
        holder.imgFood.setImageResource(imageRes);
        holder.tvQuantity.setText("x" + item.getQuantity());
    }

    @Override
    public int getItemCount() {
        return orderItems != null ? orderItems.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvName, tvPrice, tvQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }

    private String formatPrice(double price) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return formatter.format(price) + "đ";
    }
}
