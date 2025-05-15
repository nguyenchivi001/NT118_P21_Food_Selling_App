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
import com.example.food_selling_app.model.AdminOption;

import java.util.List;

/**
 * Adapter hiển thị danh sách các tùy chọn trong trang Home của Admin
 */
public class AdminOptionAdapter extends RecyclerView.Adapter<AdminOptionAdapter.OptionViewHolder> {

    private final Context context;
    private final List<AdminOption> options;
    private final OnItemClickListener listener;

    // Interface xử lý sự kiện khi người dùng nhấn vào item
    public interface OnItemClickListener {
        void onItemClick(AdminOption option);
    }

    public AdminOptionAdapter(Context context, List<AdminOption> options, OnItemClickListener listener) {
        this.context = context;
        this.options = options;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_option, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        AdminOption option = options.get(position);
        holder.title.setText(option.getTitle());
        holder.icon.setImageResource(option.getIconResId());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(option));
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public static class OptionViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        public OptionViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.option_icon);
            title = itemView.findViewById(R.id.option_title);
        }
    }
}
