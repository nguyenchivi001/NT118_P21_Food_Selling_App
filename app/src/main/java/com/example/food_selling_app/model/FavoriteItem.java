package com.example.food_selling_app.model;

import com.google.gson.annotations.SerializedName;

public class FavoriteItem {
    @SerializedName("id")
    private Integer id;

    @SerializedName("foodId")
    private Integer foodId;

    @SerializedName("foodName")
    private String foodName;

    @SerializedName("price")
    private Double price;

    @SerializedName("imageFilename")
    private String imageFilename;

    @SerializedName("createdAt")
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
