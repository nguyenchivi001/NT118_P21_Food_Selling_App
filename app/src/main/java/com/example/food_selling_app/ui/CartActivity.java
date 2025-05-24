package com.example.food_selling_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.CartAdapter;
import com.example.food_selling_app.api.ApiClient;
import com.example.food_selling_app.api.CartApi;
import com.example.food_selling_app.dto.request.CartItemRequest;
import com.example.food_selling_app.dto.response.ApiResponse;
import com.example.food_selling_app.model.CartItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends BaseActivity {

    private RecyclerView recyclerCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartList;
    private TextView tvSubtotal, tvTotal, tvEmptyCart;
    private Button btnCheckout;
    private CartApi cartApi;
    private String accessToken;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart;
    }

    @Override
    protected int getSelectedNavItem() {
        return R.id.nav_cart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerCart = findViewById(R.id.recyclerCart);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);

        accessToken = getSharedPreferences("prefs", MODE_PRIVATE).getString("access_token", "");
        cartApi = ApiClient.getClient(accessToken).create(CartApi.class);

        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartList, new CartAdapter.OnQuantityChangeListener() {
            @Override
            public void onQuantityChange(int position, int newQuantity) {
                updateCartItemQuantity(position, newQuantity);
            }
        });
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(cartAdapter);

        loadCartItems();

        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteCartItem(position);
            }

            @Override
            public void onChildDraw(android.graphics.Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(CartActivity.this, R.color.redAccent))
                        .addActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerCart);

        btnCheckout.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(CartActivity.this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            double totalPrice = 0.0;
            for (CartItem item : cartList) {
                totalPrice += item.getPrice() * item.getQuantity();
            }
            intent.putExtra("totalPrice", totalPrice);
            intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartList));
            startActivity(intent);
        });
    }

    private void loadCartItems() {
        Call<ApiResponse<List<CartItem>>> call = cartApi.getCartItems();
        call.enqueue(new Callback<ApiResponse<List<CartItem>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CartItem>>> call, Response<ApiResponse<List<CartItem>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    cartList.clear();
                    cartList.addAll(response.body().getData());
                    cartAdapter.notifyDataSetChanged();
                    updateTotal();
                    updateEmptyState();
                } else {
                    Toast.makeText(CartActivity.this, "Failed to load cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CartItem>>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartItemQuantity(int position, int newQuantity) {
        CartItem item = cartList.get(position);
        CartItemRequest request = new CartItemRequest();
        request.setFoodId(item.getFoodId());
        request.setQuantity(newQuantity);

        Call<ApiResponse<CartItem>> call = cartApi.updateCartItemQuantity(item.getFoodId(), request);
        call.enqueue(new Callback<ApiResponse<CartItem>>() {
            @Override
            public void onResponse(Call<ApiResponse<CartItem>> call, Response<ApiResponse<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    cartList.set(position, response.body().getData());
                    cartAdapter.notifyItemChanged(position);
                    updateTotal();
                } else {
                    Toast.makeText(CartActivity.this, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CartItem>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCartItem(int position) {
        CartItem item = cartList.get(position);
        Call<ApiResponse<Void>> call = cartApi.removeFromCart(item.getFoodId());
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    cartList.remove(position);
                    cartAdapter.notifyItemRemoved(position);
                    updateTotal();
                    updateEmptyState();
                } else {
                    Toast.makeText(CartActivity.this, "Failed to remove item", Toast.LENGTH_SHORT).show();
                    cartAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                cartAdapter.notifyItemChanged(position);
            }
        });
    }

    private void updateEmptyState() {
        if (cartList.isEmpty()) {
            tvEmptyCart.setVisibility(TextView.VISIBLE);
            recyclerCart.setVisibility(RecyclerView.GONE);
            btnCheckout.setEnabled(false);
        } else {
            tvEmptyCart.setVisibility(TextView.GONE);
            recyclerCart.setVisibility(RecyclerView.VISIBLE);
            btnCheckout.setEnabled(true);
        }
    }

    private void updateTotal() {
        double total = 0.0;
        for (CartItem item : cartList) {
            total += item.getPrice() * item.getQuantity();
        }
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        String formattedTotal = formatter.format(total) + "đ";
        tvSubtotal.setText(formattedTotal);
        tvTotal.setText(formattedTotal);
    }
}