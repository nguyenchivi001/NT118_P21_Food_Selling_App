package com.example.food_selling_app.dto.response;

import com.example.food_selling_app.model.Order;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponse {
    @SerializedName("content")
    private List<Order> orders;

    @SerializedName("totalElements")
    private int totalElements;

    @SerializedName("totalPages")
    private int totalPages;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
