package com.example.food_selling_app.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ChatRequest {

    @SerializedName("model")
    private String model;

    @SerializedName("messages")
    private List<Message> messages;

    @SerializedName("top_k")
    private int top_k;

    @SerializedName("max_tokens")
    private int max_tokens;

    @SerializedName("temperature")
    private double temperature;

    // Constructor với giá trị mặc định
    public ChatRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
        this.top_k = 20;
        this.max_tokens = 2000;
        this.temperature = 0.7;
    }

    // Constructor đầy đủ
    public ChatRequest(String model, List<Message> messages, int top_k, int max_tokens, double temperature) {
        this.model = model;
        this.messages = messages;
        this.top_k = top_k;
        this.max_tokens = max_tokens;
        this.temperature = temperature;
    }

    // Getters
    public String getModel() {
        return model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public int getTop_k() {
        return top_k;
    }

    public int getMax_tokens() {
        return max_tokens;
    }

    public double getTemperature() {
        return temperature;
    }

    // Setters (nếu cần)
    public void setModel(String model) {
        this.model = model;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setTop_k(int top_k) {
        this.top_k = top_k;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
