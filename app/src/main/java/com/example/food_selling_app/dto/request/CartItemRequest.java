package com.example.food_selling_app.dto.request;

public class CartItemRequest {
    private Integer foodId;
    private Integer quantity;

    public CartItemRequest() {}

    public CartItemRequest(Integer foodId, Integer quantity) {
        this.quantity = quantity;
        this.foodId = foodId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
