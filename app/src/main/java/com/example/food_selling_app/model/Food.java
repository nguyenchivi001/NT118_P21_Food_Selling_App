package com.example.food_selling_app.model;

import java.math.BigDecimal;

public class Food {
    private int id;
    private String name;
    private String description;
    private BigDecimal price; // Changed to BigDecimal to match backend
    private String imageFilename; // Changed to imageFilename to match backend field name
    private int categoryId; // Simplified to categoryId (assuming frontend only needs the ID)
    private int stockQuantity; // Added to match backend
    private boolean available; // Added to match backend

    // Constructor
    public Food(int id, String name, String description, BigDecimal price, String imageFilename,
                int categoryId, int stockQuantity, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageFilename = imageFilename;
        this.categoryId = categoryId;
        this.stockQuantity = stockQuantity;
        this.available = available;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public boolean isAvailable() {
        return available;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}