package com.example.food_selling_app.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.food_selling_app.R;
import com.example.food_selling_app.model.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private final List<CartItem> cartList;
    private final OnQuantityChangeListener listener;
    private final String baseImageUrl = "http://10.0.2.2:8080/api/foods/images/";

    public interface OnQuantityChangeListener {
        void onQuantityChange(int position, int newQuantity);
    }

    public CartAdapter(Context context, List<CartItem> cartList, OnQuantityChangeListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartList.get(position);
        holder.tvName.setText(item.getFoodName());

        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(item.getPrice()) + "đ";
        holder.tvPrice.setText(formattedPrice);

        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        String imageFilename = item.getImageFilename();
        if (imageFilename != null && !imageFilename.isEmpty()) {
            String imageUrl = baseImageUrl + imageFilename;
            Glide.with(context)
                    .load(getGlideUrlWithToken(imageUrl))
                    .placeholder(R.drawable.burger1)
                    .error(R.drawable.burger1)
                    .into(holder.imgFood);
        } else {
            holder.imgFood.setImageResource(R.drawable.burger1);
        }

        holder.btnMinus.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() - 1;
            if (newQuantity > 0) {
                listener.onQuantityChange(position, newQuantity);
            }
        });

        holder.btnPlus.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            listener.onQuantityChange(position, newQuantity);
        });
    }

    @Override
    public int getItemCount() {
        return cartList != null ? cartList.size() : 0;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvName, tvPrice, tvQuantity, btnMinus, btnPlus;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
        }
    }

    private GlideUrl getGlideUrlWithToken(String imageUrl) {
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("access_token", null);

        return new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + token)
                .build());
    }
}