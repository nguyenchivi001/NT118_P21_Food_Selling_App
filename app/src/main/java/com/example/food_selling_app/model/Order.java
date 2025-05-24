package com.example.food_selling_app.model;

public class Order {
    private String orderCode;
    private String status;
    private String customerName;
    private String orderDate;
    private String foodList;
    private int totalAmount;

    public Order(String orderCode, String status, String customerName, String orderDate, String foodList, int totalAmount) {
        this.orderCode = orderCode;
        this.status = status;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.foodList = foodList;
        this.totalAmount = totalAmount;
    }

    public String getOrderCode() {
        return orderCode;
    }
    public String getStatus() {
        return status;
    }
    public String getCustomerName() {
        return customerName;
    }
    public String getOrderDate() {
        return orderDate;
    }
    public String getFoodList() {
        return foodList;
    }
    public int getTotalAmount() {
        return totalAmount;
    }
}