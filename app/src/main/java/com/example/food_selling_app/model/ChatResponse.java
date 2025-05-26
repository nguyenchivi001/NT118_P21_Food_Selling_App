package com.example.food_selling_app.model;

public class ChatResponse {
    private Output output;

    public ChatResponse(Output output) {
        this.output = output;
    }

    // Getter (và Setter nếu cần)
    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }
}