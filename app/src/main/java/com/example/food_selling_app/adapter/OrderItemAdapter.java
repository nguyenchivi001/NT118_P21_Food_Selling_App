package com.example.food_selling_app.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.food_selling_app.R;
import com.example.food_selling_app.api.ApiClient;
import com.example.food_selling_app.api.FoodApi;
import com.example.food_selling_app.dto.response.ApiResponse;
import com.example.food_selling_app.model.Food;
import com.example.food_selling_app.model.OrderItem;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private final Context context;
    private final List<OrderItem> orderItems;
    private final FoodApi foodApi;
    private final Map<Integer, Food> foodCache;
    private final String baseImageUrl = "http://10.0.2.2:8080/api/foods/images/";
    private static final String TAG = "OrderItemAdapter";

    public OrderItemAdapter(Context context, List<OrderItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
        this.foodCache = new HashMap<>();

        // Lấy token từ SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("access_token", null);

        // Khởi tạo FoodApi với ApiClient
        this.foodApi = ApiClient.getClient(token).create(FoodApi.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        int foodId = item.getFoodId();

        // Hiển thị giá và số lượng mặc định
        holder.tvPrice.setText(formatPrice(item.getPrice()));
        holder.tvQuantity.setText("x" + item.getQuantity());
        holder.tvName.setText("Đang tải...");
        holder.imgFood.setImageResource(R.drawable.burger1);

        // Kiểm tra cache
        Food cachedFood = foodCache.get(foodId);
        if (cachedFood != null) {
            updateFoodDetailsUI(holder, cachedFood, foodId);
            return;
        }

        // Gọi API nếu không có trong cache
        Call<ApiResponse<Food>> call = foodApi.getFoodById(foodId);
        call.enqueue(new Callback<ApiResponse<Food>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Food>> call, @NonNull Response<ApiResponse<Food>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Food food = response.body().getData();
                    Log.d(TAG, "API success for foodId: " + foodId + ", name: " + food.getName() + ", imageFilename: " + food.getImageFilename());

                    // Lưu vào cache
                    foodCache.put(foodId, food);
                    updateFoodDetailsUI(holder, food, foodId);
                } else {
                    Log.e(TAG, "API failed for foodId: " + foodId + ", code: " + response.code());
                    updateFoodDetailsUI(holder, null, foodId);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Food>> call, @NonNull Throwable t) {
                Log.e(TAG, "API call failed for foodId: " + foodId, t);
                updateFoodDetailsUI(holder, null, foodId);
            }
        });
    }

    private void updateFoodDetailsUI(ViewHolder holder, @Nullable Food food, int foodId) {
        if (food != null && food.getName() != null) {
            holder.tvName.setText(food.getName());

            String imageFilename = food.getImageFilename();
            if (imageFilename != null && !imageFilename.isEmpty()) {
                String imageUrl = baseImageUrl + imageFilename;
                Log.d(TAG, "Loading image from URL: " + imageUrl);

                SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                String token = prefs.getString("access_token", null);
                Log.d(TAG, "Access token: " + (token != null ? "exists" : "null"));

                Glide.with(context)
                        .load(getGlideUrlWithToken(imageUrl))
                        .placeholder(R.drawable.burger1)
                        .error(R.drawable.burger1)
                        .into(holder.imgFood);
            } else {
                Log.w(TAG, "No imageFilename for foodId: " + foodId);
                holder.imgFood.setImageResource(R.drawable.burger1);
            }
        } else {
            holder.tvName.setText("Món #" + foodId);
            holder.imgFood.setImageResource(R.drawable.burger1);
        }
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

    private GlideUrl getGlideUrlWithToken(String imageUrl) {
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("access_token", null);
        return new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + token)
                .build());
    }
}