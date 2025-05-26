package com.example.food_selling_app.model;

import java.io.Serializable;

public class OrderItem  implements Serializable {
    private int foodId;
    private Integer quantity;

    // Getters and setters
    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}