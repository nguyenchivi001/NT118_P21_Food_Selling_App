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
import com.example.food_selling_app.model.FavoriteItem;
import com.example.food_selling_app.model.Food;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private final Context context;
    private final List<FavoriteItem> favoriteList;

    private final String baseImageUrl = "http://10.0.2.2:8080/api/foods/images/";

    public FavoritesAdapter(Context context, List<FavoriteItem> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteItem item = favoriteList.get(position);

        // Tên món
        holder.tvName.setText(item.getFoodName());

        // Format giá VND
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(item.getPrice()) + "đ";
        holder.tvPrice.setText(formattedPrice);

        // Load ảnh từ drawable
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
    }

    @Override
    public int getItemCount() {
        return favoriteList != null ? favoriteList.size() : 0;
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvName, tvPrice;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
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
