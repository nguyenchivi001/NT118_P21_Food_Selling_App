package com.example.food_selling_app.ui;

import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.CartAdapter;
import com.example.food_selling_app.model.Food;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private CartAdapter cartAdapter;
    private List<Food> cartList;
    private TextView tvSubtotal, tvTotal;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerCart = findViewById(R.id.recyclerCart);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        // Dữ liệu mẫu
        cartList = new ArrayList<>();
        cartList.add(new Food(1, "Dessert Flavors", "", new BigDecimal("466"), "burger1.png", 2, 0, true));
        cartList.add(new Food(2, "Naturally Flavored", "", new BigDecimal("250"), "burger2.png", 1, 0, true));
        cartList.add(new Food(3, "Regional Flavors", "", new BigDecimal("298"), "burger3.png", 1, 0, true));

        cartAdapter = new CartAdapter(this, cartList);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(cartAdapter);

        updateTotal();

        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                cartAdapter.removeItem(pos);
                updateTotal();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
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

    }

    private void updateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Food item : cartList) {
            BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getStockQuantity()));
            total = total.add(itemTotal);
        }

        String formatted = total + " VND";
        tvSubtotal.setText(formatted);
        tvTotal.setText(formatted);
    }
}
