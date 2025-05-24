package com.example.food_selling_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Output {
    private List<Choice> choices;

    public Output(List<Choice> choices) {
        this.choices = choices;
    }

    // Getter (và Setter nếu cần)
    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}