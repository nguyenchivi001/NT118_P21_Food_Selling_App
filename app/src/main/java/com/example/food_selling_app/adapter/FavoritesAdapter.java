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
import com.example.food_selling_app.model.Food;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private final Context context;
    private final List<Food> favoriteList;

    public FavoritesAdapter(Context context, List<Food> favoriteList) {
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
        Food item = favoriteList.get(position);

        // Tên món
        holder.tvName.setText(item.getName());

        // Format giá VND
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvPrice.setText(format.format(item.getPrice()));  // ví dụ: 98.000 ₫

        // Ngày tạm hardcode
        holder.tvDate.setText("Feb 03, 2023");

        // Load ảnh từ drawable
        if (item.getImageFilename() != null && !item.getImageFilename().isEmpty()) {
            int resId = context.getResources().getIdentifier(
                    item.getImageFilename().replace(".png", ""), "drawable", context.getPackageName()
            );
            holder.imgFood.setImageResource(resId != 0 ? resId : R.drawable.placeholder);
        } else {
            holder.imgFood.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public void removeItem(int position) {
        favoriteList.remove(position);
        notifyItemRemoved(position);
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood, imgFavorite;
        TextView tvName, tvDate, tvPrice;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
        }
    }
}
