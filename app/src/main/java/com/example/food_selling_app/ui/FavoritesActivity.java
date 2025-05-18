package com.example.food_selling_app.ui;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.FavoritesAdapter;
import com.example.food_selling_app.api.ApiClient;
import com.example.food_selling_app.api.CartApi;
import com.example.food_selling_app.api.FavoriteApi;
import com.example.food_selling_app.dto.response.ApiResponse;
import com.example.food_selling_app.model.FavoriteItem;
import com.example.food_selling_app.model.Food;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerFavorites;
    private FavoritesAdapter favoriteAdapter;
    private List<FavoriteItem> favoriteList;
    private FavoriteApi favoriteApi;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerFavorites = findViewById(R.id.recyclerFavorites);
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Khởi tạo API service và lấy access token
        accessToken = getSharedPreferences("prefs", MODE_PRIVATE).getString("access_token", "");
        favoriteApi = ApiClient.getClient(accessToken).create(FavoriteApi.class);

        favoriteList = new ArrayList<>();
        favoriteAdapter = new FavoritesAdapter(this, favoriteList);

        recyclerFavorites.setLayoutManager(new LinearLayoutManager(this));
        recyclerFavorites.setAdapter(favoriteAdapter);

        loadFavoriteItems();

        // Xử lý vuốt để xóa
        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteFavoriteItem(position);
            }

            @Override
            public void onChildDraw(android.graphics.Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(FavoritesActivity.this, R.color.redAccent))
                        .addActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerFavorites);

    }

    private void loadFavoriteItems() {
        Call<ApiResponse<List<FavoriteItem>>> call = favoriteApi.getFavoriteItems();
        call.enqueue(new Callback<ApiResponse<List<FavoriteItem>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<FavoriteItem>>> call, Response<ApiResponse<List<FavoriteItem>>> response) {
                if(response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    favoriteList.clear();
                    favoriteList.addAll(response.body().getData());
                    favoriteAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(FavoritesActivity.this, "Failed to load favorites", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<FavoriteItem>>> call, Throwable t) {
                Toast.makeText(FavoritesActivity.this, "Error: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFavoriteItem(int position) {
        FavoriteItem item = favoriteList.get(position);
        Call<ApiResponse<Void>> call = favoriteApi.removeFromFavorite(item.getFoodId());
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if(response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    favoriteList.remove(position);
                    favoriteAdapter.notifyItemRemoved(position);
                } else {
                    Toast.makeText(FavoritesActivity.this, "Failes to remove item", Toast.LENGTH_SHORT).show();
                    favoriteAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(FavoritesActivity.this, "Error: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                favoriteAdapter.notifyItemChanged(position);
            }
        });
    }
}
