package com.example.food_selling_app.dto.request;

public class FavoriteItemRequest {
    private Integer foodId;

    public FavoriteItemRequest() {}

    public FavoriteItemRequest(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }
}
