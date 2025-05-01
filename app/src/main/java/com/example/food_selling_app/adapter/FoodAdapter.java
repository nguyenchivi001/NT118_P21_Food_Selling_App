// ================================
// 1. FoodAdapter.java
// ================================
package com.example.food_selling_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_selling_app.R;
import com.example.food_selling_app.model.Food;
import com.example.food_selling_app.ui.FoodDetailActivity;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private final Context context;
    private final List<Food> foodList;

    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_card, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food item = foodList.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.price.setText(item.getPrice() + "đ");

        String imageUrl = item.getImageUrl();

        if (imageUrl != null && imageUrl.endsWith(".png") && !imageUrl.startsWith("http")) {
            int resId = context.getResources().getIdentifier(
                    imageUrl.replace(".png", ""), "drawable", context.getPackageName()
            );
            if (resId != 0) {
                holder.image.setImageResource(resId);
            } else {
                holder.image.setImageResource(R.drawable.placeholder);
            }
        } else {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.image);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodDetailActivity.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("price", item.getPrice());

            int imageResId = context.getResources().getIdentifier(
                    item.getImageUrl().replace(".png", ""), "drawable", context.getPackageName()
            );
            intent.putExtra("imageResId", imageResId);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodList != null ? foodList.size() : 0;
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, description, price;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgFood);
            name = itemView.findViewById(R.id.tvName);
            description = itemView.findViewById(R.id.tvDescription);
            price = itemView.findViewById(R.id.tvPrice);
        }
    }
}